package org.avaje.glue.websocket;

import org.avaje.glue.config.WebSocketProvider;
import org.avaje.glue.core.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultEndpointProvider implements WebSocketProvider {

  private static final Logger log = LoggerFactory.getLogger(DefaultEndpointProvider.class);

  @Override
  public List<ServerEndpointConfig> endpoints() {

    ApplicationContext context = SpringContext.get();

    List<ServerEndpointConfig> collect =
      context.getBeansWithAnnotation(ServerEndpoint.class)
        .values().stream().map(this::registerEndpoint)
        .collect(Collectors.toList());

    return collect;
  }

  private ServerEndpointConfig registerEndpoint(Object wsBean) {
    try {
      // register with the servlet container such that the Guice created singleton instance
      // is the instance that is used (and not an instance created per request etc)
      log.debug("registering ServerEndpoint [{}] with servlet container", wsBean);
      BasicWebSocketConfigurator configurator = new BasicWebSocketConfigurator(wsBean);

      Class<?> wsBeanClass = wsBean.getClass();
      ServerEndpoint serverEndpoint = wsBeanClass.getAnnotation(ServerEndpoint.class);

      return new BasicWebSocketEndpointConfig(wsBeanClass, serverEndpoint, configurator);

    } catch (Exception e) {
      throw new IllegalStateException("Error creating BasicWebSocketEndpointConfig for " + wsBean, e);
    }
  }
}
