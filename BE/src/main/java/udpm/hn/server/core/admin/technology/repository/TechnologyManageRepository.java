package udpm.hn.server.core.admin.technology.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.TechnologyRepository;

@Repository
public interface TechnologyManageRepository extends TechnologyRepository {

    /**
     * Kiểm tra tên công nghệ đã tồn tại (chỉ xét bản ghi chưa bị xóa mềm)
     */
    @Query("SELECT COUNT(t) > 0 FROM Technology t WHERE t.name = :name AND t.status <> :status")
    boolean existsByNameAndStatusNot(String name, EntityStatus status);

    /**
     * Kiểm tra tên công nghệ khi cập nhật (loại trừ chính nó và bản ghi đã xóa mềm)
     */
    @Query("SELECT COUNT(t) > 0 FROM Technology t WHERE t.name = :name AND t.id <> :id AND t.status <> :status")
    boolean existsByNameAndIdNotAndStatusNot(String name, String id, EntityStatus status);
}
