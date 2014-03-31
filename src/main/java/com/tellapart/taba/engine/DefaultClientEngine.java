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

import com.tellapart.taba.TabaClientProperties;
import com.tellapart.taba.event.Event;
import com.tellapart.taba.event.EventPayload;
import com.tellapart.taba.Transport;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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
  private final Logger logger = LoggerFactory.getLogger(DefaultClientEngine.class);

  private final String clientId;
  private final int flushPeriod;
  private final String eventPostUrl;

  private CloseableHttpClient httpClient;

  private final ScheduledExecutorService flushScheduler;
  private ScheduledFuture<?> flusherHandle;

  private final Object lock = new Object();
  private @GuardedBy("lock") Map<String, List<Event>> buffer;
  private long bufferSize;

  @Inject
  public DefaultClientEngine(
      TabaClientProperties properties,
      CloseableHttpClient client,
      ScheduledExecutorService executor
  ) {
    this.clientId = properties.getClientId();
    this.flushPeriod = properties.getFlushPeriod();
    this.eventPostUrl = properties.getPostUrl();

    httpClient = client;
    flushScheduler = executor;

    // Buffer to hold events until they are flushed.
    buffer = new HashMap<>();
    bufferSize = 0;

  }

  @Override
  public synchronized void start() {
    if(flusherHandle != null) {
      throw new IllegalStateException("Cannot call start multiple times.");
    }

    final Runnable flusher = new Runnable() {
      @Override
      public void run() {
        flush();
      }
    };

    flusherHandle = flushScheduler.scheduleAtFixedRate(
        flusher, flushPeriod, flushPeriod, TimeUnit.SECONDS);
  }

  @Override
  public void stop() {
    // Cancel the scheduled runnables.
    flusherHandle.cancel(false);

    // Disable new tasks from being submitted and clean up the scheduler;
    flushScheduler.shutdown();

    try {
      // Wait for existing tasks to terminate.
      if (!flushScheduler.awaitTermination(flushPeriod, TimeUnit.SECONDS)) {
        flushScheduler.shutdownNow();
        // Wait for tasks to respond to being canceled.
        if (!flushScheduler.awaitTermination(flushPeriod, TimeUnit.SECONDS)) {
          logger.error("Taba Client flush scheduler did not terminate.");
        }
      }

      // Process any leftover buffer synchronously.
      flush();


    } catch (InterruptedException e) {
      // Re-cancel if current thread also interrupted.
      flushScheduler.shutdownNow();

      // Preserve interrupt status.
      Thread.currentThread().interrupt();
    }

    // Close any HTTP sessions.
    try {
      httpClient.close();
    } catch(IOException e) {
      logger.error("Error stopping Taba Client HTTP Client");
    }

  }

  @Override
  public void recordEvent(String name, String type, EventPayload payload) {
    // Lock while we record the event into the buffer.
    synchronized (lock) {
      if (!buffer.containsKey(name)) {
        buffer.put(name, new ArrayList<Event>());
      }
      buffer.get(name).add(new Event(type, System.currentTimeMillis() / 1000, payload));
      bufferSize += 1;
    }

  }

  /**
   * Flush all buffered Events to the remote end-point.
   */
  protected void flush() {
    if (bufferSize == 0) {
      return;
    }

    // Clear the existing buffer.
    Map<String, List<Event>> flushBuffer;
    synchronized (lock) {
      flushBuffer = buffer;
      buffer = new HashMap<>();
      bufferSize = 0;
    }

    String body = Transport.encode(clientId, flushBuffer);
    HttpPost httpPost = new HttpPost(eventPostUrl);

    try {
      StringEntity entity;
      entity = new StringEntity(body);
      entity.setContentType("application/json");
      httpPost.setEntity(entity);

      try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
          logger.error(String.format("Bad status code: %d", statusCode));
        }

      } catch (IOException e) {
        logger.error("Error flushing Taba Client buffer", e);
      }

    } catch (UnsupportedEncodingException e) {
      logger.error("Error encoding Taba Client buffer", e);
    }

  }

}
