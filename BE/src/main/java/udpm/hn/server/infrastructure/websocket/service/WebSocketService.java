package udpm.hn.server.infrastructure.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import udpm.hn.server.infrastructure.websocket.model.WebSocketMessage;

@Service
@RequiredArgsConstructor
public class    WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendBatchProgress(String jobId, String message, Object data) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(
                "BATCH_PROGRESS",
                message,
                data
        );
        messagingTemplate.convertAndSend("/topic/batch/" + jobId, webSocketMessage);
    }

    public void sendBatchComplete(String jobId, String message, Object data) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(
                "BATCH_COMPLETE",
                message,
                data
        );
        messagingTemplate.convertAndSend("/topic/batch/" + jobId, webSocketMessage);
    }

    public void sendBatchError(String jobId, String message, Object data) {
        WebSocketMessage webSocketMessage = new WebSocketMessage(
                "BATCH_ERROR",
                message,
                data
        );
        messagingTemplate.convertAndSend("/topic/batch/" + jobId + "/error", webSocketMessage);
    }
}
