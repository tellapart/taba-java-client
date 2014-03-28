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

/**
 * Container class for variables needed to initialize TabaClientEngine objects.
 */
public class TabaClientProperties {

  protected final String clientId;
  protected final int flushPeriod;
  protected final String postUrl;

  public TabaClientProperties(String clientId, int flushPeriod, String postUrl) {
    Preconditions.checkNotNull(clientId, "clientId cannot be null");
    Preconditions.checkArgument(flushPeriod > 0, "flushPeriod must be > 0");
    Preconditions.checkNotNull(postUrl, "postUrl Trcannot be null");

    this.clientId = clientId;
    this.flushPeriod = flushPeriod;
    this.postUrl = postUrl;
  }

  public String getClientId() {
    return clientId;
  }

  public int getFlushPeriod() {
    return flushPeriod;
  }

  public String getPostUrl() {
    return postUrl;
  }

}
