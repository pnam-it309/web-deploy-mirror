package udpm.hn.server.core.customer.review.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequest {
    private String appId;
    private Integer rating; // 1-5
    private String comment;
    private String userEmail;
    private String userName;
}
