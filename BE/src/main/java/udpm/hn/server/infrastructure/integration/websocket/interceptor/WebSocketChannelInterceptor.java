package udpm.hn.server.infrastructure.integration.websocket.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

/**
 * Interceptor cho các kênh WebSocket
 * - Ghi log các sự kiện kết nối/ngắt kết nối
 * - Xử lý xác thực và ủy quyền cho kết nối WebSocket
 * - Có thể thêm các logic kiểm tra bảo mật khác
 */

@Slf4j  // Tự động tạo logger
@Component  // Đánh dấu là một Spring component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    /**
     * Xử lý tin nhắn trước khi gửi đi
     * - Kiểm tra và xử lý các lệnh STOMP (CONNECT, DISCONNECT, SUBSCRIBE, etc.)
     * - Ghi log các sự kiện kết nối/ngắt kết nối
     * 
     * @param message Tin nhắn WebSocket
     * @param channel Kênh tin nhắn
     * @return Tin nhắn đã được xử lý
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // Tạo đối tượng truy cập header STOMP
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        
        // Xử lý lệnh kết nối
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("Kết nối WebSocket mới: {}", accessor.getSessionId());
            // TODO: Thêm logic xác thực/ủy quyền tại đây
            // Ví dụ: Kiểm tra token JWT trong header
        } 
        // Xử lý lệnh ngắt kết nối
        else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("Ngắt kết nối WebSocket: {}", accessor.getSessionId());
            // TODO: Xử lý dọn dẹp tài nguyên khi ngắt kết nối
        }
        
        return message;  // Trả về tin nhắn để tiếp tục xử lý
    }
}
