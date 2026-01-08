package udpm.hn.server.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.infrastructure.listen.AuditEntityListener;

/**
 * Lớp cơ sở chứa các trường audit cho tất cả các entity
 * - Tự động cập nhật thời gian tạo và chỉnh sửa
 * - Sử dụng EntityListener để tự động cập nhật các trường này
 */

@Getter
@Setter
@MappedSuperclass // Đánh dấu đây là class cơ sở cho các entity
@EntityListeners(AuditEntityListener.class) // Lắng nghe sự kiện để cập nhật thời gian
public abstract class AuditEntity {

    /**
     * Thời điểm tạo bản ghi (timestamp)
     * - Không thể cập nhật sau khi tạo
     * - Được tự động thiết lập bởi AuditEntityListener
     */
    @Column(updatable = false)
    private Long createdDate;

    /**
     * Thời điểm cập nhật bản ghi gần nhất (timestamp)
     * - Được tự động cập nhật mỗi khi bản ghi thay đổi
     * - Được quản lý bởi AuditEntityListener
     */
    @Column
    private Long lastModifiedDate;

}
