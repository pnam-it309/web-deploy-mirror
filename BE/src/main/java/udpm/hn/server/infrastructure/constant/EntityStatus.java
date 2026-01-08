package udpm.hn.server.infrastructure.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enum định nghĩa các trạng thái của entity trong hệ thống
 * - Được sử dụng để đánh dấu trạng thái hoạt động của các bản ghi
 * - Hỗ trợ các phương thức tiện ích để làm việc với các trạng thái
 */

public enum EntityStatus {
    /**
     * Trạng thái hoạt động
     * - Bản ghi đang hoạt động bình thường
     * - Được hiển thị và xử lý trong các nghiệp vụ
     */
    ACTIVE,

    /**
     * Trạng thái không hoạt động
     * - Bản ghi bị vô hiệu hóa tạm thời
     * - Không hiển thị trong các chức năng thông thường
     */
    INACTIVE,

    /**
     * Trạng thái đã xóa mềm
     * - Bản ghi đánh dấu là đã xóa nhưng vẫn còn trong DB
     */
    DELETED;

    /**
     * Lấy danh sách tất cả các trạng thái dưới dạng List<String>
     * 
     * @return Danh sách tên các trạng thái
     */
    public static List<String> getAllStatus() {
        return Arrays.stream(EntityStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * Lấy chuỗi chứa tất cả các trạng thái, phân cách bởi dấu phẩy
     * 
     * @return Chuỗi định dạng "ACTIVE, INACTIVE"
     */
    public static String getAllStatusString() {
        return Arrays.stream(EntityStatus.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
