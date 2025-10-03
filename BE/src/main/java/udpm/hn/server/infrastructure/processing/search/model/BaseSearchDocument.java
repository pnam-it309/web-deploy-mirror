package udpm.hn.server.infrastructure.processing.search.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * Base class for all Elasticsearch document models.
 * Provides common fields like id, createdDate, and lastModifiedDate.
 */
@Data
public abstract class BaseSearchDocument {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    @CreatedDate
    private LocalDateTime createdDate;
    
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    
    /**
     * Returns the index name for this document type.
     * Each concrete class should override this to provide its specific index name.
     */
    public abstract String getIndexName();
}
