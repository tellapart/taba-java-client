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

import com.tellapart.taba.engine.DummyClientEngine;
import com.tellapart.taba.engine.TabaClientEngine;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for factory style initialization.
 */
public class TabaApiFactoryTest {

  @Test
  public void testFactoryPropertiesInitialization() {
    TabaClientProperties properties = new TabaClientProperties();
    properties.SetClientId("client_id");
    properties.SetFlushPeriod(1);
    properties.SetPostUrl("http://localhost:1234/post");

    TabaApiFactory.Initialize(properties);

    TabaApi taba = TabaApiFactory.GetApi();
    Assert.assertNotNull(taba);

  }

  @Test
  public void testFactoryEngineInitialization() {
    TabaClientEngine engine = new DummyClientEngine("client_id", 1, "http://localhost:1234/post");
    TabaApiFactory.Initialize(engine);

    TabaApi taba = TabaApiFactory.GetApi();
    Assert.assertNotNull(taba);
  }

}
