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
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * EventPayload for any single Number object.
 */
public class EventNumberPayload implements EventPayload {

  private Number payload;

  public EventNumberPayload(Number value) {
    payload = value;
  }

  @Override
  public void serialize(JsonGenerator generator) throws JsonGenerationException, IOException {
    if(payload instanceof Integer) {
      generator.writeNumber((Integer) payload);
    }
    else if(payload instanceof Long) {
      generator.writeNumber((Long) payload);
    }
    else if(payload instanceof Short) {
      generator.writeNumber((Short) payload);
    }
    else if(payload instanceof Float) {
      generator.writeNumber((Float) payload);
    }
    else if(payload instanceof Double) {
      generator.writeNumber((Double) payload);
    }
    else if(payload instanceof Byte) {
      generator.writeNumber((Byte) payload);
    }
    else if(payload instanceof BigInteger) {
      generator.writeNumber((BigInteger) payload);
    }
    else if(payload instanceof BigDecimal) {
      generator.writeNumber((BigDecimal) payload);
    }
  }

}
