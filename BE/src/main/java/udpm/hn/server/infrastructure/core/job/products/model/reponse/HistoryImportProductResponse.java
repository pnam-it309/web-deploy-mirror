package udpm.hn.server.infrastructure.core.job.products.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.entity.Role;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryImportProductResponse {

    private String id;
    private String sku;
    private String name;
    private String message;
    private List<Role> roles;
    private Long createdDate;

}
