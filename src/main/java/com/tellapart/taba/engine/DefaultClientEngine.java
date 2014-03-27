/* Copyright 2014 TellApart, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tellapart.taba.engine;

import com.tellapart.taba.event.Event;
import com.tellapart.taba.event.EventPayload;
import com.tellapart.taba.Transport;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Default implementation of the TabaClientEngine. Buffers Events in memory and
 * uses a background Thread to flush the buffer to a remote HTTP Agent or
 * Server.
 */
public class DefaultClientEngine implements TabaClientEngine {

  private String mClientId;
  private int mFlushPeriod;
  private String mEventPostUrl;
  private ScheduledExecutorService mFlushScheduler;
  private CloseableHttpClient mHttpClient;
  private final Object mLock = new Object();

  private Map<String, List<Event>> mBuffer;
  private long mBufferSize;
  private ScheduledFuture<?> mFlusherHandle;

  public DefaultClientEngine(String clientId, int flushPeriod, String eventPostUrl) {
    mClientId = clientId;
    mFlushPeriod = flushPeriod;
    mEventPostUrl = eventPostUrl;

    // Buffer to hold events until they are flushed.
    mBuffer = new HashMap<>();
    mBufferSize = 0;

    mHttpClient = HttpClients.createDefault();
    mFlushScheduler = Executors.newScheduledThreadPool(1);

  }

  @Override
  public void Start() {
    final Runnable flusher = new Runnable() {
      @Override
      public void run() {
        Flush();
      }
    };
    mFlusherHandle = mFlushScheduler.scheduleAtFixedRate(
        flusher, mFlushPeriod, mFlushPeriod, TimeUnit.SECONDS);

  }

  @Override
  public void Stop() {
    // Cancel the scheduled runnables.
    mFlusherHandle.cancel(false);

    // Disable new tasks from being submitted and clean up the scheduler;
    mFlushScheduler.shutdown();

    try {
      // Wait for existing tasks to terminate.
      if (!mFlushScheduler.awaitTermination(mFlushPeriod, TimeUnit.SECONDS)) {
        mFlushScheduler.shutdownNow();
        // Wait for tasks to respond to being canceled.
        if (!mFlushScheduler.awaitTermination(mFlushPeriod, TimeUnit.SECONDS)) {
          System.err.println("Taba Client flush scheduler did not terminate.");
        }
      }

      // Process any leftover buffer synchronously.
      Flush();

      // Close any HTTP sessions.
      mHttpClient.close();

    } catch (InterruptedException e) {
      // Re-cancel if current thread also interrupted.
      mFlushScheduler.shutdownNow();

      // Preserve interrupt status.
      Thread.currentThread().interrupt();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void RecordEvent(String name, String type, EventPayload payload) {
    // Lock while we record the event into the buffer.
    synchronized (mLock) {
      if (!mBuffer.containsKey(name)) {
        mBuffer.put(name, new ArrayList<Event>());
      }
      mBuffer.get(name).add(new Event(type, System.currentTimeMillis() / 1000, payload));
      mBufferSize += 1;
    }

  }

  /**
   * Flush all buffered Events to the remote end-point.
   */
  protected void Flush() {
    if (mBufferSize == 0) {
      return;
    }

    // Clear the existing buffer.
    Map<String, List<Event>> flushBuffer;
    synchronized (mLock) {
      flushBuffer = mBuffer;
      mBuffer = new HashMap<>();
      mBufferSize = 0;
    }

    String body = Transport.Encode(mClientId, flushBuffer);
    HttpPost httpPost = new HttpPost(mEventPostUrl);

    try {
      StringEntity entity;
      entity = new StringEntity(body);
      entity.setContentType("application/json");
      httpPost.setEntity(entity);

      CloseableHttpResponse response = null;
      try {
        response = mHttpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
          System.err.println(String.format("Bad status code: %d", statusCode));
        }

      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          if (response != null) {
            response.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

  }

}
