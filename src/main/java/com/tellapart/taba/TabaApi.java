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
package com.tellapart.taba;

import com.tellapart.taba.engine.TabaClientEngine;
import com.tellapart.taba.event.EventNumberPayload;
import com.tellapart.taba.event.EventPayload;
import com.tellapart.taba.event.EventStringLongPayload;
import com.tellapart.taba.event.EventStringPayload;

import javax.inject.Inject;

/**
 * Main API point for users of the Taba Client.
 *
 * Provides an interface for:
 *   1) Starting and stopping the client engine.
 *   2) Recording raw Events.
 *   3) Convenience methods for recording to common types.
 */
public class TabaApi {

  protected TabaClientEngine mEngine;

  @Inject
  public TabaApi(TabaClientEngine engine) {
    mEngine = engine;
  }

  /*-------------------------------------------------------
   * Client Management
   *-----------------------------------------------------*/

  public void Start() {
    if(mEngine != null) {
      mEngine.Start();
    }
  }

  public void Stop() {
    if(mEngine != null) {
      mEngine.Stop();
    }
  }

  /*-------------------------------------------------------
   * Event Recording Methods and Convenience Wrappers
   *-----------------------------------------------------*/

  public void RecordEvent(String name, String type, EventPayload payload) {
    if(mEngine != null) {
      mEngine.RecordEvent(name, type, payload);
    }
  }

  public void RecordEvent(String name, TabType type, EventPayload payload) {
    RecordEvent(name, type.toString(), payload);
  }

  public void RecordCounter(String name, Number value) {
    RecordEvent(name, TabType.CounterGroup, new EventNumberPayload(value));
  }

  public void RecordCounter(String name) {
    RecordCounter(name, 1);
  }

  public void RecordPercentile(String name, Number value) {
    RecordEvent(name, TabType.PercentileGroup, new EventNumberPayload(value));
  }

  public void RecordPercentile(String name) {
    RecordPercentile(name, 1);
  }

  public void RecordGauge(String name, String value) {
    RecordEvent(name, TabType.Gauge, new EventStringPayload(value));
  }

  public void RecordGauge(String name, String value, Long expiration) {
    RecordEvent(name, TabType.ExpiryGauge, new EventStringLongPayload(value, expiration));
  }

  public void RecordBuffer(String name, String value) {
    RecordEvent(name, TabType.Buffer, new EventStringPayload(value));
  }

}
