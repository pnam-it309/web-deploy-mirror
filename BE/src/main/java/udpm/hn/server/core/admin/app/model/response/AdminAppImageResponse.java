package udpm.hn.server.core.admin.app.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminAppImageResponse {
    private String id;
    private String url;
    private Boolean isMain;
}
