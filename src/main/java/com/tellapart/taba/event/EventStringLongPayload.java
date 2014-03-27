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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

/**
 * EventPayload for a (String, Long) Tuple.
 */
public class EventStringLongPayload implements EventPayload {

  private String mString;
  private Long mLong;

  public EventStringLongPayload(String stringValue, Long longValue) {
    mString = stringValue;
    mLong = longValue;
  }

  @Override
  public void Serialize(JsonGenerator generator) throws JsonGenerationException, IOException {
    generator.writeStartArray();
    generator.writeString(mString);
    generator.writeNumber(mLong);
    generator.writeEndArray();
  }

}
