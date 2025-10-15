package udpm.hn.server.core.admin.quotes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.QuoteRequest;
import udpm.hn.server.infrastructure.core.constant.EntityStatus;

@Repository
public interface OrderManageRepository extends JpaRepository<QuoteRequest, String> {
    
    @Query("""
        SELECT q FROM QuoteRequest q
        WHERE (:search IS NULL OR :search = '' OR 
               LOWER(q.customerName) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(q.customerEmail) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(q.customerPhone) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(q.customerCompany) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:status IS NULL OR q.status = :status)
        ORDER BY q.createdDate DESC
    """)
    Page<QuoteRequest> findAllWithFilters(
        @Param("search") String search,
        @Param("status") EntityStatus status,
        Pageable pageable
    );
    
    @Query("SELECT COUNT(q) FROM QuoteRequest q WHERE q.status = :status")
    long countByStatus(@Param("status") EntityStatus status);
}
