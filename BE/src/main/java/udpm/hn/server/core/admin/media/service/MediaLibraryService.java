package udpm.hn.server.core.admin.media.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.entity.MediaLibrary;
import udpm.hn.server.repository.MediaLibraryRepository;

import java.util.Map;

/**
 * Media Library Service
 * Handles business logic for media operations
 * Transactions managed at this layer (not in controller)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MediaLibraryService {

    private final MediaLibraryRepository mediaLibraryRepository;

    /**
     * Create new media
     */
    @Transactional
    public MediaLibrary create(MediaLibrary media) {
        log.info("Creating media: type={}, file={}", media.getFileType(), media.getOriginalName());
        MediaLibrary saved = mediaLibraryRepository.save(media);
        log.info("Media created successfully: id={}", saved.getId());
        return saved;
    }

    /**
     * Update media metadata
     */
    @Transactional
    public MediaLibrary update(String id, Map<String, Object> updates) {
        log.info("Updating media: id={}", id);

        MediaLibrary media = mediaLibraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        // Apply updates
        if (updates.containsKey("tags")) {
            media.setTags((String) updates.get("tags"));
        }
        if (updates.containsKey("folder")) {
            media.setFolder((String) updates.get("folder"));
        }
        if (updates.containsKey("originalName")) {
            media.setOriginalName((String) updates.get("originalName"));
        }

        MediaLibrary saved = mediaLibraryRepository.save(media);
        log.info("Media updated successfully: id={}", saved.getId());
        return saved;
    }

    /**
     * Delete media
     */
    @Transactional
    public void delete(String id) {
        log.info("Deleting media: id={}", id);

        if (!mediaLibraryRepository.existsById(id)) {
            throw new RuntimeException("Media not found");
        }

        mediaLibraryRepository.deleteById(id);
        log.info("Media deleted successfully: id={}", id);
    }

    /**
     * Increment usage count
     */
    @Transactional
    public void incrementUsage(String id) {
        log.debug("Incrementing usage count: id={}", id);

        MediaLibrary media = mediaLibraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        media.setUsageCount(media.getUsageCount() + 1);
        mediaLibraryRepository.save(media);

        log.debug("Usage count incremented: id={}, newCount={}", id, media.getUsageCount());
    }
}
