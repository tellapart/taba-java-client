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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.tellapart.taba.event.Event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implements the Transport encoding for Taba Events before they are sent to
 * the Taba Agent.
 */
public class Transport {

  protected static final Joiner partJoiner = Joiner.on('\n');

  // Static only class.
  protected Transport() {}

  /**
   * Encode the events for this client id.
   *
   * @param clientId      The client id that generated the nameEventsMap.
   * @param nameEventsMap Map from Tab Name to List of Event for that Tab Name.
   *
   * @return A string denoting the event to be uploaded to the Taba Agent.
   */
  public static String encode(String clientId, Map<String, List<Event>> nameEventsMap) {
    return encodeMultiClient(ImmutableMap.of(clientId, nameEventsMap));
  }

  /**
   * Encode a bundle of Events for multiple Client IDs.
   *
   * @param clientToNameToEvents Map from Client ID -> Tab Name -> List of Event objects.
   *
   * @return A string denoting the event to be uploaded to the Taba Agent.
   */
  public static String encodeMultiClient(
      Map<String, Map<String, List<Event>>> clientToNameToEvents) {
    List<String> parts = new ArrayList<>();

    // TODO(mike): Use Multimap for default list.
    // TODO(mike): join java and python PROTOCOL_VERSION_CURRENT together ('1');
    JsonFactory factory = new JsonFactory();
    parts.add("1");
    parts.add("");

    for (Map.Entry<String, Map<String, List<Event>>> entry : clientToNameToEvents.entrySet()) {
      String client = entry.getKey();
      Map<String, List<Event>> nameToEvents = entry.getValue();
      parts.add(client);
      parts.add("");
      for (Map.Entry<String, List<Event>> entry2 : nameToEvents.entrySet()) {
        String name = entry2.getKey();
        List<Event> events = entry2.getValue();
        parts.add(name);
        for (Event event : events) {
          try {
            parts.add(encodeEvent(event, factory));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        parts.add("");
      }
      parts.add("");
    }
    parts.add("");

    return partJoiner.join(parts);
  }

  /**
   * Encode a single Event.
   *
   * @param event   Event to encode.
   * @param factory
   *
   * @return  Encoded event String.
   */
  protected static String encodeEvent(Event event, JsonFactory factory) throws IOException {
    ByteArrayOutputStream encodedPayload = new ByteArrayOutputStream();
    JsonGenerator payloadGenerator = factory.createGenerator(encodedPayload);
    event.getPayload().serialize(payloadGenerator);
    payloadGenerator.close();

    ByteArrayOutputStream encodedEvent = new ByteArrayOutputStream();
    JsonGenerator eventGenerator = factory.createGenerator(encodedEvent);
    eventGenerator.writeStartArray();
    eventGenerator.writeString(event.getTabType());
    eventGenerator.writeNumber(event.getTimestamp());
    eventGenerator.writeString(encodedPayload.toString());
    eventGenerator.writeEndArray();
    eventGenerator.close();

    return encodedEvent.toString();
  }

}
