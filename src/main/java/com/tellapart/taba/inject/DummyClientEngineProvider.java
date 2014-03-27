package com.tellapart.taba.inject;

import com.tellapart.taba.TabaClientProperties;
import com.tellapart.taba.engine.DummyClientEngine;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Dependency injection Provider for DummyClientEngine objects.
 */
public class DummyClientEngineProvider implements Provider<DummyClientEngine> {

  protected DummyClientEngine mEngine;

  @Inject
  public DummyClientEngineProvider(TabaClientProperties properties) {
    mEngine = new DummyClientEngine(
        properties.GetClientId(),
        properties.GetFlushPeriod(),
        properties.GetPostUrl());
  }

  @Override
  public DummyClientEngine get() {
    return mEngine;
  }

}
