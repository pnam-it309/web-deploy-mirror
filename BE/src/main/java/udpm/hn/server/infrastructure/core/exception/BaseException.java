package udpm.hn.server.infrastructure.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Lớp cơ sở cho các ngoại lệ tùy chỉnh trong ứng dụng
 * - Cung cấp cấu trúc thống nhất cho việc xử lý lỗi
 * - Bao gồm mã lỗi và mã trạng thái HTTP tương ứng
 * - Kế thừa từ RuntimeException để hỗ trợ unchecked exception
 */
/**
 * Đánh dấu là một ngoại lệ cơ sở
 * - Sử dụng @Getter của Lombok để tự động tạo các phương thức getter
 */
@Getter
public abstract class BaseException extends RuntimeException {
    /**
     * Mã trạng thái HTTP (ví dụ: 400, 404, 500, ...)
     */
    private final HttpStatus status;
    
    /**
     * Mã lỗi tùy chỉnh để xác định loại lỗi
     */
    private final String errorCode;
    
    /**
     * Thông báo lỗi chi tiết
     */
    private final String message;

    /**
     * Khởi tạo một ngoại lệ cơ sở
     *
     * @param status    Mã trạng thái HTTP
     * @param errorCode Mã lỗi tùy chỉnh
     * @param message   Thông báo lỗi chi tiết
     */
    protected BaseException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }
}
