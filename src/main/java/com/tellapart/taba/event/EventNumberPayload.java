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

  private Number mNumber;

  public EventNumberPayload(Number value) {
    mNumber = value;
  }

  @Override
  public void Serialize(JsonGenerator generator) throws JsonGenerationException, IOException {
    if(mNumber instanceof Integer) {
      generator.writeNumber((Integer)mNumber);
    }
    else if(mNumber instanceof Long) {
      generator.writeNumber((Long)mNumber);
    }
    else if(mNumber instanceof Short) {
      generator.writeNumber((Short)mNumber);
    }
    else if(mNumber instanceof Float) {
      generator.writeNumber((Float)mNumber);
    }
    else if(mNumber instanceof Double) {
      generator.writeNumber((Double)mNumber);
    }
    else if(mNumber instanceof Byte) {
      generator.writeNumber((Byte)mNumber);
    }
    else if(mNumber instanceof BigInteger) {
      generator.writeNumber((BigInteger)mNumber);
    }
    else if(mNumber instanceof BigDecimal) {
      generator.writeNumber((BigDecimal)mNumber);
    }
  }

}
