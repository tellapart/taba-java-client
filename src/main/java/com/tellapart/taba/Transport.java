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
import com.tellapart.taba.event.Event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements the Transport encoding for Taba Events before they are sent to
 * the Taba Agent.
 */
public class Transport {

  /**
   * Encode the events for this client id.
   *
   * @param clientId      The client id that generated the nameEventsMap.
   * @param nameEventsMap Map from Tab Name to List of Event for that Tab Name.
   *
   * @return A string denoting the event to be uploaded to the Taba Agent.
   */
  public static String Encode(String clientId, Map<String, List<Event>> nameEventsMap) {
    Map<String, Map<String, List<Event>>> clientToNameToEvents = new HashMap<>();
    clientToNameToEvents.put(clientId, nameEventsMap);
    return EncodeMultiClient(clientToNameToEvents);
  }

  /**
   * Encode a bundle of Events for multiple Client IDs.
   *
   * @param clientToNameToEvents Map from Client ID -> Tab Name -> List of Event objects.
   *
   * @return A string denoting the event to be uploaded to the Taba Agent.
   */
  public static String EncodeMultiClient(
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
            ByteArrayOutputStream encodedPayload = new ByteArrayOutputStream();
            JsonGenerator payloadGenerator = factory.createGenerator(encodedPayload);
            event.payload.Serialize(payloadGenerator);
            payloadGenerator.close();

            ByteArrayOutputStream encodedEvent = new ByteArrayOutputStream();
            JsonGenerator eventGenerator = factory.createGenerator(encodedEvent);
            eventGenerator.writeStartArray();
            eventGenerator.writeString(event.tabType);
            eventGenerator.writeNumber(event.timestamp);
            eventGenerator.writeString(encodedPayload.toString());
            eventGenerator.writeEndArray();
            eventGenerator.close();

            parts.add(encodedEvent.toString());
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        parts.add("");
      }
      parts.add("");
    }
    parts.add("");
    return Joiner.on('\n').join(parts);
  }

}
