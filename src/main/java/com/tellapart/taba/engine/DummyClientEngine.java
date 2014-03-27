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

import com.tellapart.taba.event.EventPayload;

/**
 * Client Engine implementation that simply drops all events. Useful for
 * testing or development deployments.
 */
public class DummyClientEngine implements TabaClientEngine {

  private String mClientId;
  private int mFlushPeriod;
  private String mEventPostUrl;

  public DummyClientEngine(String clientId, int flushPeriod, String eventPostUrl) {
    mClientId = clientId;
    mFlushPeriod = flushPeriod;
    mEventPostUrl = eventPostUrl;
  }

  @Override
  public void Start() { }

  @Override
  public void Stop() { }

  @Override
  public void RecordEvent(String name, String type, EventPayload payload) { }

}
