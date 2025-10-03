package udpm.hn.server.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import udpm.hn.server.infrastructure.core.constant.EntityProperties;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;
import udpm.hn.server.infrastructure.processing.listener.CreatePrimaryEntityListener;

/**
 * Lớp cơ sở cho tất cả các entity trong hệ thống
 * - Cung cấp các trường cơ bản: id, status
 * - Tự động sinh ID khi tạo mới thông qua CreatePrimaryEntityListener
 * - Kế thừa từ AuditEntity để có các trường audit (createdDate, lastModifiedDate, ...)
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass  // Đánh dấu đây là class cơ sở cho các entity
@EntityListeners(CreatePrimaryEntityListener.class)  // Lắng nghe sự kiện tạo mới entity
public abstract class PrimaryEntity extends AuditEntity implements IsIdentified {

    /**
     * Định danh duy nhất của entity
     * - Độ dài cố định theo EntityProperties.LENGTH_ID
     * - Không thể cập nhật sau khi tạo
     */
    @Id
    @Column(length = EntityProperties.LENGTH_ID, updatable = false)
    protected String id;

    /**
     * Trạng thái của bản ghi
     * - Lưu dưới dạng số nguyên (ORDINAL) trong database
     * - Các giá trị được định nghĩa trong enum EntityStatus
     */
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private EntityStatus status;
}
