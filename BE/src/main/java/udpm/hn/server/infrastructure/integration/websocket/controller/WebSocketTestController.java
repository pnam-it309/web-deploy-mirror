package udpm.hn.server.infrastructure.integration.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.infrastructure.integration.websocket.model.WebSocketMessage;

@Controller
@RequestMapping("/ws")
@RequiredArgsConstructor
public class WebSocketTestController {

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public WebSocketMessage handleTestMessage(WebSocketMessage message) {
        return WebSocketMessage.builder()
                .type("TEST_RESPONSE")
                .message("Server received: " + message.getMessage())
                .data(message.getData())
                .build();
    }
}
