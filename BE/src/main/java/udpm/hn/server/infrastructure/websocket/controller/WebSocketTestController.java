package udpm.hn.server.infrastructure.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.websocket.model.WebSocketMessage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ResponseObject<?> sendMessage(Map<String, String> message) {
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        List<String> lt = new ArrayList<>();
        // Đảm bảo trả về JSON hợp lệ
        Map<String, String> response = new HashMap<>();
        response.put("sender", message.get("sender"));
        response.put("content", message.get("content"));
        response.put("timestamp", timestamp);

        return new ResponseObject(response, HttpStatus.OK, "đã nhận");
    }
}
