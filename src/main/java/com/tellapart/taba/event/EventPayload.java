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
 * Generic Event payload. Offloads the JSON serialization to specific payload
 * type implementations.
 */
public interface EventPayload {

  /**
   * Serialize the payload through the given JsonGenerator.
   *
   * @param generator   JSON serializer to serialize to.
   *
   * @throws JsonGenerationException
   * @throws IOException
   */
  public void serialize(JsonGenerator generator) throws JsonGenerationException, IOException;

}
