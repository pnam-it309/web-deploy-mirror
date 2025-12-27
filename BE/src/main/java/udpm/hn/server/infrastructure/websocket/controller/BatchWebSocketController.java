package udpm.hn.server.infrastructure.websocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import udpm.hn.server.infrastructure.websocket.model.WebSocketMessage;

@Controller
public class BatchWebSocketController {

    @MessageMapping("/batch/status/{jobId}")
    @SendTo("/topic/batch/{jobId}")
    public WebSocketMessage subscribeToBatchJob(String jobId) {
        return new WebSocketMessage("SUBSCRIBE", "Subscribed to batch job: " + jobId, null);
    }
}
