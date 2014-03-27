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

import com.google.inject.*;
import com.tellapart.taba.engine.TabaClientEngine;
import com.tellapart.taba.inject.DummyClientEngineProvider;
import com.tellapart.taba.inject.TabaApiProvider;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for dependency injection style initialization.
 */
public class InjectionTest {

  public static class TestModule implements Module {

    @Override
    public void configure(Binder binder) {

      binder
          .bind(TabaClientEngine.class)
          .toProvider(DummyClientEngineProvider.class)
          .in(Scopes.SINGLETON);

      binder
          .bind(TabaApi.class)
          .toProvider(TabaApiProvider.class)
          .in(Scopes.SINGLETON);

    }

    @Provides
    TabaClientProperties GetClientProperties() {
      return new TabaClientProperties("test_client_id", 1, "http://localhost:1234/post");
    }

  }

  public static class TestInjectee {
    public TabaApi mApi;
    @Inject
    public TestInjectee(TabaApi api) {
      mApi = api;
    }
  }

  @Test
  public void testInjection() {
    Injector injector = Guice.createInjector(new TestModule());
    TabaApi api = injector.getInstance(TabaApi.class);
    Assert.assertNotNull(api);

  }

  @Test
  public void testInjectee() {
    Injector injector = Guice.createInjector(new TestModule());
    TestInjectee injectee = injector.getInstance(TestInjectee.class);
    Assert.assertNotNull(injectee);
    Assert.assertNotNull(injectee.mApi);
  }

}
