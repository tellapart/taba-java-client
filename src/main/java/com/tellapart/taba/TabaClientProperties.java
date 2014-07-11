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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Container class for variables needed to initialize TabaClientEngine objects.
 */
public class TabaClientProperties {

  protected final String clientId;
  protected final long flushPeriodMillis;
  protected final String postUrl;

  public TabaClientProperties(String clientId, long flushPeriodMillis, String postUrl) {
    checkNotNull(clientId, "clientId cannot be null");
    checkArgument(flushPeriodMillis >= 100, "flushPeriodMillis must be >= 100ms");
    checkNotNull(postUrl, "postUrl cannot be null");

    this.clientId = clientId;
    this.flushPeriodMillis = flushPeriodMillis;
    this.postUrl = postUrl;
  }

  public String getClientId() {
    return clientId;
  }

  public long getFlushPeriodMillis() {
    return flushPeriodMillis;
  }

  public String getPostUrl() {
    return postUrl;
  }

}
