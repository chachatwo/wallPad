package com.wallpad.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 서버 → 클라이언트로 전달할 경로
		config.enableSimpleBroker("/topic");

		// 클라이언트 → 서버로 보낼 경로 접두사
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// WebSocket 연결 엔드포인트
		registry.addEndpoint("/ws").setAllowedOriginPatterns("http://localhost:8080", "http://3.34.226.110:8080")
				.withSockJS();

	}
}
