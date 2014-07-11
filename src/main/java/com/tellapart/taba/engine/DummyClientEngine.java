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
import com.tellapart.taba.event.EventPayload;

import javax.inject.Inject;

/**
 * Client Engine implementation that simply drops all events. Useful for
 * testing or development deployments.
 */
public class DummyClientEngine implements TabaClientEngine {

  private String clientId;
  private long flushPeriodMillis;
  private String eventPostUrl;

  @Inject
  public DummyClientEngine(TabaClientProperties properties) {
    this.clientId = properties.getClientId();
    this.flushPeriodMillis = properties.getFlushPeriodMillis();
    this.eventPostUrl = properties.getPostUrl();
  }

  @Override
  public void start() { }

  @Override
  public void stop() { }

  @Override
  public void recordEvent(String name, String type, EventPayload payload) { }

}
