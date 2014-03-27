package com.tellapart.taba.inject;

import com.tellapart.taba.TabaClientProperties;
import com.tellapart.taba.engine.DefaultClientEngine;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Dependency injection Provider for DefaultClientEngine objects.
 */
public class DefaultClientEngineProvider implements Provider<DefaultClientEngine> {

  protected DefaultClientEngine mEngine;

  @Inject
  public DefaultClientEngineProvider(TabaClientProperties properties) {
    mEngine = new DefaultClientEngine(
        properties.GetClientId(),
        properties.GetFlushPeriod(),
        properties.GetPostUrl());
  }

  @Override
  public DefaultClientEngine get() {
    return mEngine;
  }

}
