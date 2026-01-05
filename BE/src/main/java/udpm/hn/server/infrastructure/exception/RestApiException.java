package udpm.hn.server.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class RestApiException extends BaseException {

    public RestApiException(String message) {
        super(HttpStatus.BAD_REQUEST, "API_ERROR", message);
    }

    public RestApiException(String message, HttpStatus status) {
        super(status, "API_ERROR", message);
    }
}
