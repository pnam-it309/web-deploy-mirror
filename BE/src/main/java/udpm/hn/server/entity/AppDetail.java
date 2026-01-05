package udpm.hn.server.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import udpm.hn.server.entity.base.PrimaryEntity;

import udpm.hn.server.infrastructure.constant.EntityProperties;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "app_details")
public class AppDetail extends PrimaryEntity implements Serializable {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false, unique = true)
    private App app;

    @Column(name = "long_description", columnDefinition = "LONGTEXT")
    private String longDescription;

    @Column(name = "demo_url", length = EntityProperties.LENGTH_URL)
    private String demoUrl;

    @Column(name = "source_url", length = EntityProperties.LENGTH_URL)
    private String sourceUrl;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode specifications;
}
