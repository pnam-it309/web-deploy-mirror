package udpm.hn.server.infrastructure.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import udpm.hn.server.infrastructure.websocket.model.WebSocketMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * Xử lý lỗi validation (@NotBlank, @NotNull, v.v.)
     * Cần dùng @Valid trong Controller để kích hoạt
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebSocketMessage> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation error: {}", message);
        WebSocketMessage errorResponse = new WebSocketMessage(
            "VALIDATION_ERROR",
            message,
            new HashMap<>(Map.of("fields", message))
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Xử lý lỗi dữ liệu không hợp lệ (tên trùng, v.v.)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<WebSocketMessage> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        WebSocketMessage errorResponse = new WebSocketMessage(
            "CONFLICT",
            ex.getMessage(),
            new HashMap<>(Map.of("error", ex.getMessage()))
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Xử lý lỗi không tìm thấy entity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<WebSocketMessage> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        WebSocketMessage errorResponse = new WebSocketMessage(
            "NOT_FOUND",
            ex.getMessage(),
            new HashMap<>(Map.of("error", ex.getMessage()))
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Xử lý Optimistic Locking Exception – xảy ra khi @Version conflict.
     * Trong trường hợp soft delete với @Version, operation thực ra đã thành công
     * trước khi conflict → coi là thành công và trả về 204.
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Void> handleOptimisticLocking(ObjectOptimisticLockingFailureException ex) {
        log.warn("Optimistic locking conflict (treated as success for soft delete): {}", ex.getMessage());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebSocketMessage> handleException(Exception ex) {
        log.error("Lỗi không xác định: {}", ex.getMessage(), ex);
        WebSocketMessage errorResponse = new WebSocketMessage(
            "ERROR",
            "Đã xảy ra lỗi không xác định",
            new HashMap<>(Map.of("error", ex.getMessage() != null ? ex.getMessage() : "Unknown error"))
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
