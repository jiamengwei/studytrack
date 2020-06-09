package com.websocket.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic");
    config.setApplicationDestinationPrefixes("/app");
  }

  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("*").withSockJS();
  }
}


//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//  @Bean
//  public WebSocketHandler myHandler() {
//    return new MyHandler();
//  }
//
//  public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
//    webSocketHandlerRegistry.addHandler(myHandler(), "/gs-guide-websocket");
//  }
//}