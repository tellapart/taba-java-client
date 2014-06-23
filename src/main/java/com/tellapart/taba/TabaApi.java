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

import com.google.common.base.Preconditions;
import com.tellapart.taba.engine.TabaClientEngine;
import com.tellapart.taba.event.EventNumberPayload;
import com.tellapart.taba.event.EventPayload;
import com.tellapart.taba.event.EventStringLongPayload;
import com.tellapart.taba.event.EventStringPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  protected final TabaClientEngine engine;

  @Inject
  public TabaApi(TabaClientEngine engine) {
    Preconditions.checkNotNull(engine, "engine cannot be null");
    this.engine = engine;
  }

  /*-------------------------------------------------------
   * Client Management
   *-----------------------------------------------------*/

  public void start() {
    engine.start();
  }

  public void stop() {
    engine.stop();
  }

  /*-------------------------------------------------------
   * Event Recording Methods and Convenience Wrappers
   *-----------------------------------------------------*/

  public void recordEvent(String name, String type, EventPayload payload) {
    engine.recordEvent(name, type, payload);
  }

  public void recordEvent(String name, TabType type, EventPayload payload) {
    recordEvent(name, type.toString(), payload);
  }

  public void recordCounter(String name, Number value) {
    recordEvent(name, TabType.CounterGroup, new EventNumberPayload(value));
  }

  public void recordCounter(String name) {
    recordCounter(name, 1);
  }

  public void recordPercentile(String name, Number value) {
    recordEvent(name, TabType.PercentileGroup, new EventNumberPayload(value));
  }

  public void recordPercentile(String name) {
    recordPercentile(name, 1);
  }

  public void recordGauge(String name, String value) {
    recordEvent(name, TabType.Gauge, new EventStringPayload(value));
  }

  public void recordGauge(String name, String value, Long expiration) {
    recordEvent(name, TabType.ExpiryGauge, new EventStringLongPayload(value, expiration));
  }

  public void recordBuffer(String name, String value) {
    recordEvent(name, TabType.Buffer, new EventStringPayload(value));
  }

}
