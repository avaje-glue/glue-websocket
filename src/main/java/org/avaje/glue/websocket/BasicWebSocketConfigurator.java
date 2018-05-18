package org.avaje.glue.websocket;

import javax.websocket.server.ServerEndpointConfig;

/**
 * Implementation of Configurator that takes an already instantiated instance
 * of the WebSocket server endpoint (typically created by Guice) and always
 * returns that.
 * <p>
 *   Used to register a singleton WebSocket server endpoint with the servlet container.
 * </p>
 */
public class BasicWebSocketConfigurator extends ServerEndpointConfig.Configurator {

  private final Object instance;

  public BasicWebSocketConfigurator(Object instance) {
    this.instance = instance;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getEndpointInstance(Class<T> endpointClass) {
    return (T)instance;
  }

}
