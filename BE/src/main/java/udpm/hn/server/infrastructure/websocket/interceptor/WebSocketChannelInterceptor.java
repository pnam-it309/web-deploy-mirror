package udpm.hn.server.infrastructure.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import udpm.hn.server.infrastructure.security.jwt.JwtService;
import udpm.hn.server.infrastructure.security.service.CustomUserDetailsService;

/**
 * Interceptor cho các kênh WebSocket
 * - Ghi log các sự kiện kết nối/ngắt kết nối
 * - Xử lý xác thực và ủy quyền cho kết nối WebSocket
 * - Có thể thêm các logic kiểm tra bảo mật khác
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Xử lý tin nhắn trước khi gửi đi
     * - Kiểm tra và xử lý các lệnh STOMP (CONNECT, DISCONNECT, SUBSCRIBE, etc.)
     * - Ghi log các sự kiện kết nối/ngắt kết nối
     * - Xác thực user từ token JWT trong header Authorization
     * 
     * @param message Tin nhắn WebSocket
     * @param channel Kênh tin nhắn
     * @return Tin nhắn đã được xử lý
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Xử lý lệnh kết nối
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtService.extractUsername(token);

                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (jwtService.isTokenValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        accessor.setUser(authentication);
                        log.info("WebSocket authenticated user: {}", username);
                    }
                }
            } else {
                log.warn("WebSocket connection attempt without valid Authorization header");
            }
            log.info("Kết nối WebSocket mới: sessionID={}", accessor.getSessionId());
        }

        // Xử lý lệnh ngắt kết nối
        else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("Ngắt kết nối WebSocket: sessionID={}", accessor.getSessionId());
        }

        return message;
    }
}
