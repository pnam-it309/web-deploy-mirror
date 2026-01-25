package udpm.hn.server.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import udpm.hn.server.entity.MediaLibrary;

/**
 * JPA Specification for MediaLibrary filtering
 * Replaces multiple @Query methods with type-safe, reusable specifications
 */
public class MediaLibrarySpecification {

    /**
     * Filter media by tag (LIKE search)
     * 
     * @param tag - tag to search for
     * @return Specification for tag filtering
     */
    public static Specification<MediaLibrary> hasTag(String tag) {
        return (root, query, cb) -> {
            if (tag == null || tag.trim().isEmpty()) {
                return cb.conjunction(); // Always true if no tag
            }
            String pattern = "%" + tag.trim() + "%";
            return cb.like(root.get("tags"), pattern);
        };
    }

    /**
     * Filter media by folder
     * 
     * @param folder - folder name
     * @return Specification for folder filtering
     */
    public static Specification<MediaLibrary> inFolder(String folder) {
        return (root, query, cb) -> {
            if (folder == null || folder.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("folder"), folder.trim());
        };
    }

    /**
     * Search media by filename (case-insensitive LIKE)
     * 
     * @param search - search term
     * @return Specification for filename search
     */
    public static Specification<MediaLibrary> searchByFilename(String search) {
        return (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + search.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("originalName")), pattern);
        };
    }

    /**
     * Filter media by file type
     * 
     * @param fileType - file type (e.g., "image/png")
     * @return Specification for file type filtering
     */
    public static Specification<MediaLibrary> hasFileType(String fileType) {
        return (root, query, cb) -> {
            if (fileType == null || fileType.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("fileType"), fileType.trim());
        };
    }

    /**
     * Order by creation date descending
     * 
     * @return Specification with ordering
     */
    public static Specification<MediaLibrary> orderByCreatedAtDesc() {
        return (root, query, cb) -> {
            if (query != null) {
                query.orderBy(cb.desc(root.get("createdAt")));
            }
            return cb.conjunction();
        };
    }

    /**
     * Combined filter for media library
     * 
     * @param tag    - optional tag filter
     * @param folder - optional folder filter
     * @param search - optional search term
     * @return Combined specification
     */
    public static Specification<MediaLibrary> filter(String tag, String folder, String search) {
        return Specification
                .where(hasTag(tag))
                .and(inFolder(folder))
                .and(searchByFilename(search))
                .and(orderByCreatedAtDesc());
    }
}
