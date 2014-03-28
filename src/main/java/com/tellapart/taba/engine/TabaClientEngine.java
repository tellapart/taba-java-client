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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Interface for Client Engine objects, which are responsible for taking Record
 * Event requests, and sending them to a remote service (i.e. a Taba Agent or
 * Taba Server.)
 */
public interface TabaClientEngine {

  /**
   * start this Client Engine, starting any background operations.
   */
  @PostConstruct
  public void start();

  /**
   * stop any background operations.
   */
  @PreDestroy
  public void stop();

  /**
   * Record a Taba Event.
   *
   * @param name    Tab name.
   * @param type    The TabType string to record.
   * @param payload The event to encode.
   */
  public void recordEvent(String name, String type, EventPayload payload);

}
