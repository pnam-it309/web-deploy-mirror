package udpm.hn.server.core.admin.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.DomainRepository;

@Repository
public interface DomainManageRepository extends DomainRepository {

    /**
     * Kiểm tra tên đã tồn tại (chỉ xét các bản ghi chưa bị xóa mềm)
     */
    @Query("SELECT COUNT(d) > 0 FROM Domain d WHERE d.name = :name AND d.status <> :status")
    boolean existsByNameAndStatusNot(String name, EntityStatus status);

    /**
     * Kiểm tra tên đã tồn tại khi cập nhật (loại trừ chính nó và bản ghi đã xóa mềm)
     */
    @Query("SELECT COUNT(d) > 0 FROM Domain d WHERE d.name = :name AND d.id <> :id AND d.status <> :status")
    boolean existsByNameAndIdNotAndStatusNot(String name, String id, EntityStatus status);
}