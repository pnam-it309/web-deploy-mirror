package udpm.hn.server.infrastructure.core.constant;

/**
 * Lớp chứa các hằng số liên quan đến thuộc tính của các entity
 * - Định nghĩa độ dài tối đa cho các trường dữ liệu
 * - Đảm bảo tính nhất quán trong toàn bộ hệ thống
 */
public final class EntityProperties {

    /**
     * Private constructor để ngăn chặn việc khởi tạo đối tượng
     * Vì đây là lớp tiện ích chỉ chứa các hằng số
     */
    private EntityProperties() {
        throw new UnsupportedOperationException("Đây là lớp tiện ích, không thể khởi tạo");
    }

    /**
     * Độ dài tối đa của trường ID (36 ký tự - UUID)
     */
    public static final byte LENGTH_ID = 36;

    /**
     * Độ dài tối đa của mã định danh (50 ký tự)
     */
    public static final byte LENGTH_CODE = 50;

    /**
     * Độ dài tối đa của tên (255 ký tự)
     */
    public static final short LENGTH_NAME = 255;

    /**
     * Độ dài tối đa của đường dẫn ảnh (2000 ký tự)
     */
    public static final short LENGTH_PICTURE = 2000;

    /**
     * Độ dài tối đa của URL (2000 ký tự)
     */
    public static final short LENGTH_URL = 2000;

    /**
     * Độ dài tối đa của mô tả (5000 ký tự)
     */
    public static final short LENGTH_DESCRIPTION = 5000;

    /**
     * Độ dài tối đa của nội dung (2000 ký tự)
     */
    public static final short LENGTH_CONTENT = 2000;

}
