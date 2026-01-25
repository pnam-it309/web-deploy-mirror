package udpm.hn.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.entity.base.PrimaryEntity;
import udpm.hn.server.infrastructure.constant.EntityProperties;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "search_queries", indexes = {
        @Index(name = "idx_query_text", columnList = "query_text"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuery extends PrimaryEntity implements Serializable {

    @Column(name = "query_text", nullable = false, length = EntityProperties.LENGTH_NAME)
    private String queryText;

    @Column(name = "user_id", length = EntityProperties.LENGTH_ID)
    private String userId; // nullable - anonymous searches allowed

    @Column(name = "result_count")
    private Integer resultCount;

    @Column(name = "category", length = 50)
    private String category; // e.g., "DOMAIN", "TECHNOLOGY", "GENERAL"

    @Column(name = "has_results")
    private Boolean hasResults; // quick filter for no-result queries
}
