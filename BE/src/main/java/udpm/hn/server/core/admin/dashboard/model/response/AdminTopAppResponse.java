package udpm.hn.server.core.admin.dashboard.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminTopAppResponse {
    private String id;
    private String name;
    private Long viewCount;
    private String thumbnail;
}
