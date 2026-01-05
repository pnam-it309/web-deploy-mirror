package udpm.hn.server.infrastructure.websocket.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Lớp lắng nghe các sự kiện WebSocket
 * - Ghi log khi có kết nối mới hoặc ngắt kết nối
 * - Có thể mở rộng để xử lý các sự kiện khác liên quan đến phiên WebSocket
 */

@Slf4j  // Tự động tạo logger
@Component  // Đánh dấu là một Spring component
public class WebSocketEventListener {

    /**
     * Xử lý sự kiện khi có kết nối WebSocket mới
     * 
     * @param event Sự kiện kết nối WebSocket
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        log.info("Nhận kết nối WebSocket mới: {}", sessionId);
    }

    /**
     * Xử lý sự kiện khi ngắt kết nối WebSocket
     * 
     * @param event Sự kiện ngắt kết nối WebSocket
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("Người dùng đã ngắt kết nối: {}", event.getSessionId());
        // TODO: Có thể thêm xử lý dọn dẹp tài nguyên hoặc cập nhật trạng thái người dùng
    }
}
