package udpm.hn.server.core.admin.analytics.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchKeywordResponse {
    private String keyword;
    private Long searchCount;
}
