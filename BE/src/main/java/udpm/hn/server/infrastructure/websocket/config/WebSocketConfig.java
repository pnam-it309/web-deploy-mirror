package udpm.hn.server.infrastructure.websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.*;
import udpm.hn.server.infrastructure.websocket.interceptor.WebSocketChannelInterceptor;
import org.springframework.beans.factory.annotation.Value;

/**
 * Cấu hình WebSocket cho ứng dụng
 * - Kích hoạt WebSocket với STOMP protocol
 * - Cấu hình message broker và các endpoint
 * - Quản lý kết nối và bảo mật WebSocket
 */

@Configuration
@EnableWebSocketMessageBroker // Kích hoạt WebSocket message broker
@EnableScheduling // Cho phép lên lịch các tác vụ
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // Độ ưu tiên cao cho cấu hình WebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Interceptor để xử lý các kênh WebSocket
    private final WebSocketChannelInterceptor webSocketChannelInterceptor;

    @Value("${frontend.url}")
    public String ALLOWED_ORIGIN;

    @Value("${ws.applicationPrefix}")
    private String applicationPrefix;

    @Value("${ws.topicPrefix}")
    private String topicPrefix;

    @Value("${ws.registerEndpoint}")
    private String registerEndpoint;

    /**
     * Cấu hình message broker cho WebSocket
     * - Định nghĩa các tiền tố cho đích đến tin nhắn
     * - Cấu hình broker đơn giản trong bộ nhớ
     * 
     * @param config Đối tượng cấu hình message broker
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Kích hoạt simple broker cho các tiền tố này to send messages back to the
        // client
        config.enableSimpleBroker(topicPrefix, "/topic", "/queue");

        // Tiền tố cho các @MessageMapping endpoints
        config.setApplicationDestinationPrefixes(applicationPrefix, "/app");

        // Tiền tố cho private user messages
        config.setUserDestinationPrefix("/user");
    }

    @org.springframework.context.annotation.Bean
    public WebSocketInterceptor webSocketInterceptor() {
        return new WebSocketInterceptor();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register WebSocket endpoint
        registry.addEndpoint("/ws")
                .setAllowedOrigins(ALLOWED_ORIGIN) // Allow frontend origin
                .addInterceptors(webSocketInterceptor()) // Add handshake interceptor
                .withSockJS(); // Enable SockJS fallback
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Add interceptor for WebSocket channels
        registration.interceptors(webSocketChannelInterceptor);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        // Configure WebSocket timeouts and limits
        registry.setSendTimeLimit(15 * 1000) // 15 seconds
                .setSendBufferSizeLimit(512 * 1024) // 512KB
                .setMessageSizeLimit(128 * 1024); // 128KB
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        // Configure outbound channel thread pool
        registration.taskExecutor()
                .corePoolSize(4)
                .maxPoolSize(10)
                .queueCapacity(25);
    }
}
