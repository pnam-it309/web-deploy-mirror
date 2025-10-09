package udpm.hn.server.infrastructure.integration.websocket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.*;
import udpm.hn.server.infrastructure.integration.websocket.interceptor.WebSocketChannelInterceptor;
import org.springframework.beans.factory.annotation.Value;

/**
 * Cấu hình WebSocket cho ứng dụng
 * - Kích hoạt WebSocket với STOMP protocol
 * - Cấu hình message broker và các endpoint
 * - Quản lý kết nối và bảo mật WebSocket
 */

@Configuration
@EnableWebSocketMessageBroker  // Kích hoạt WebSocket message broker
@EnableScheduling  // Cho phép lên lịch các tác vụ
@Order(Ordered.HIGHEST_PRECEDENCE + 99)  // Độ ưu tiên cao cho cấu hình WebSocket
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
        // Kích hoạt message broker đơn giản trong bộ nhớ
        config.enableSimpleBroker(
            "/topic",  // Cho các kênh đăng ký công khai
            "/queue"   // Cho các tin nhắn riêng tư
        );
        
        // Tiền tố cho các đích đến từ phía client
        config.setApplicationDestinationPrefixes("/app");

        config.enableSimpleBroker(topicPrefix);
        config.setApplicationDestinationPrefixes(applicationPrefix);
        // Tiền tố cho các đích đến cụ thể của người dùng
        config.setUserDestinationPrefix("/user");
        
        // Cấu hình message broker relay cho môi trường production (RabbitMQ/ActiveMQ)
        // config.enableStompBrokerRelay("/topic", "/queue")
        //     .setRelayHost("localhost")
        //     .setRelayPort(61613)
        //     .setClientLogin("guest")
        //     .setClientPasscode("guest");
    }

    /**
     * Đăng ký các endpoint WebSocket
     * - Định nghĩa endpoint kết nối WebSocket
     * - Cấu hình CORS và hỗ trợ SockJS
     * 
     * @param registry Đối tượng đăng ký endpoint
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint(registerEndpoint)
                .addInterceptors(new WebSocketInterceptor())
                .setAllowedOrigins(ALLOWED_ORIGIN)
                .withSockJS();
    }
    
    /**
     * Cấu hình interceptor cho kênh nhận tin nhắn
     * - Thêm WebSocketChannelInterceptor để xử lý xác thực và ghi log
     * 
     * @param registration Đối tượng đăng ký kênh
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketChannelInterceptor);
    }
    
    /**
     * Cấu hình các thông số vận chuyển WebSocket
     * - Giới hạn kích thước tin nhắn và bộ đệm
     * - Thiết lập thời gian chờ gửi
     * 
     * @param registry Đối tượng đăng ký vận chuyển WebSocket
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry
            .setMessageSizeLimit(1024 * 1024)  // Giới hạn kích thước tin nhắn 1MB
            .setSendBufferSizeLimit(1024 * 1024 * 10)  // Giới hạn kích thước bộ đệm gửi 10MB
            .setSendTimeLimit(20000);  // Thời gian chờ gửi tối đa 20 giây
    }
}
