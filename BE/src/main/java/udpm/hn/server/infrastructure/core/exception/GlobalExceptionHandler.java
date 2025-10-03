package udpm.hn.server.infrastructure.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import udpm.hn.server.infrastructure.integration.websocket.model.WebSocketMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Xử lý ngoại lệ toàn cục cho ứng dụng
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<WebSocketMessage> handleBaseException(BaseException ex) {
        log.error("Lỗi xử lý: {}", ex.getMessage(), ex);
        WebSocketMessage errorResponse = new WebSocketMessage(
            "ERROR",
            ex.getMessage(),
            new HashMap<>(Map.of("errorCode", ex.getErrorCode()))
        );
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebSocketMessage> handleException(Exception ex) {
        log.error("Lỗi không xác định: {}", ex.getMessage(), ex);
        WebSocketMessage errorResponse = new WebSocketMessage(
            "ERROR",
            "Đã xảy ra lỗi không xác định",
            new HashMap<>(Map.of("error", ex.getMessage()))
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
