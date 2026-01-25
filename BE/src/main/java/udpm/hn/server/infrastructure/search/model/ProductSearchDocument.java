package udpm.hn.server.infrastructure.search.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Product document for Meilisearch indexing
 * This is a denormalized version of Product entity optimized for search
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDocument {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("slug")
    private String slug;
    
    @JsonProperty("summary")
    private String summary;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("domain_name")
    private String domainName;
    
    @JsonProperty("domain_id")
    private String domainId;
    
    @JsonProperty("technology_names")
    private List<String> technologyNames;
    
    @JsonProperty("technology_ids")
    private List<String> technologyIds;
    
    @JsonProperty("feature_names")
    private List<String> featureNames;
    
    @JsonProperty("team_members")
    private List<String> teamMembers;
    
    @JsonProperty("view_count")
    private Long viewCount;
    
    @JsonProperty("is_featured")
    private Boolean isFeatured;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("published_at")
    private Long publishedAt; // Unix timestamp for sorting
    
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    
    @JsonProperty("demo_url")
    private String demoUrl;
    
    @JsonProperty("source_url")
    private String sourceUrl;
}
