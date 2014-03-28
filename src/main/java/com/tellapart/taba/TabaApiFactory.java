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
import com.tellapart.taba.engine.DefaultClientEngine;
import com.tellapart.taba.engine.TabaClientEngine;

/**
 * Singleton management for the TabaApi, for use cases without dependency
 * injection.
 */
public class TabaApiFactory {

  protected static TabaClientEngine engine;
  protected static TabaApi api;

  // Class is static only.
  private TabaApiFactory() {}

  public static synchronized void initialize(TabaClientProperties properties) {
    Preconditions.checkState(engine == null, "Already initialized.");
    TabaClientEngine clientEngine = new DefaultClientEngine(properties);
    initialize(clientEngine);
  }

  public static synchronized void initialize(TabaClientEngine clientEngine) {
    Preconditions.checkState(engine == null, "Already initialized.");
    engine = clientEngine;
    api = new TabaApi(engine);
  }

  public static TabaApi getApi() {
    return api;
  }

}
