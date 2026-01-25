package udpm.hn.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.MediaLibrary;

import java.util.List;

/**
 * MediaLibrary Repository
 * Now supports JPA Specifications for dynamic filtering
 * 
 * Usage with Specification:
 * 
 * <pre>
 * Specification<MediaLibrary> spec = MediaLibrarySpecification.filter(tag, folder, search);
 * Page<MediaLibrary> results = repository.findAll(spec, pageable);
 * </pre>
 */
@Repository
public interface MediaLibraryRepository
        extends JpaRepository<MediaLibrary, String>, JpaSpecificationExecutor<MediaLibrary> {

    /**
     * Find all media ordered by creation date (newest first)
     * Note: Can also be achieved with Specification.orderByCreatedAtDesc()
     */
    Page<MediaLibrary> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Filter by file type with ordering
     * Note: Can also use MediaLibrarySpecification.hasFileType()
     */
    Page<MediaLibrary> findByFileTypeOrderByCreatedAtDesc(String fileType, Pageable pageable);

    /**
     * Get all distinct folders (for filter dropdown)
     * Cannot be replaced by Specification - aggregate query
     */
    @Query("SELECT DISTINCT m.folder FROM MediaLibrary m WHERE m.folder IS NOT NULL")
    List<String> findDistinctFolders();

    /**
     * Get all distinct tags (for filter dropdown)
     * Cannot be replaced by Specification - aggregate query
     */
    @Query("SELECT DISTINCT m.tags FROM MediaLibrary m WHERE m.tags IS NOT NULL")
    List<String> findDistinctTags();
}
