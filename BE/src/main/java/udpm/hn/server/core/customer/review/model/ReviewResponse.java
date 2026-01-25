package udpm.hn.server.core.customer.review.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private Integer rating;
    private String comment;
    private String userEmail;
    private String userName;
    private Long createdAt;
}
