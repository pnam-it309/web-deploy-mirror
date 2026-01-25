package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.*;
import udpm.hn.server.entity.base.PrimaryEntity;

@Entity
@Table(name = "media_library")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaLibrary extends PrimaryEntity {

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "file_type", nullable = false)
    private String fileType; // image, video, document

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_size")
    private Long fileSize; // in bytes

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "tags", length = 500)
    private String tags; // comma-separated tags

    @Column(name = "folder")
    private String folder; // virtual folder for organization

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @lombok.Builder.Default
    @Column(name = "usage_count")
    private Integer usageCount = 0;
}
