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
package com.tellapart.taba.event;

import com.tellapart.taba.event.EventPayload;

/**
 * Wrapper for Taba Event tuples.
 */
public class Event {

  protected final String tabType;
  protected final long timestamp;
  protected final EventPayload payload;

  /**
   * @param tabType   TabType string.
   * @param timestamp Epoch timestamp.
   * @param payload   Event data.
   */
  public Event(String tabType, long timestamp, EventPayload payload) {
    this.tabType = tabType;
    this.timestamp = timestamp;
    this.payload = payload;
  }

  public String getTabType() {
    return tabType;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public EventPayload getPayload() {
    return payload;
  }

}
