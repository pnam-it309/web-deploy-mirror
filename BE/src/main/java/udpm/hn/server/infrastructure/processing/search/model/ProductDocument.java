package udpm.hn.server.infrastructure.processing.search.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "products")
public class ProductDocument extends BaseSearchDocument {
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String name;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;
    
    @Field(type = FieldType.Double)
    private BigDecimal price;
    
    @Field(type = FieldType.Keyword)
    private String category;
    
    @Field(type = FieldType.Integer)
    private Integer stockQuantity;
    
    @Field(type = FieldType.Boolean)
    private boolean active;
    
    @Override
    public String getIndexName() {
        return "products";
    }
}
