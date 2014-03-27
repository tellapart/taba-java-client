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

/**
 * Container class for variables needed to initialize TabaClientEngine objects.
 */
public class TabaClientProperties {

  protected String mClientId;
  protected int mFlushPeriod;
  protected String mPostUrl;


  public TabaClientProperties() {}

  public TabaClientProperties(String clientId, int flushPeriod, String postUrl) {
    mClientId = clientId;
    mFlushPeriod = flushPeriod;
    mPostUrl = postUrl;
  }


  public void SetClientId(String clientId) {
    mClientId = clientId;
  }
  public String GetClientId() {
    return mClientId;
  }

  public void SetFlushPeriod(int flushPeriod) {
    mFlushPeriod = flushPeriod;
  }
  public int GetFlushPeriod() {
    return mFlushPeriod;
  }

  public void SetPostUrl(String postUrl) {
    mPostUrl = postUrl;
  }
  public String GetPostUrl() {
    return mPostUrl;
  }

}
