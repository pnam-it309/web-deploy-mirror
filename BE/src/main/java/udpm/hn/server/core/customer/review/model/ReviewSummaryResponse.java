package udpm.hn.server.core.customer.review.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSummaryResponse {
    private Double averageRating;
    private Long totalReviews;
    private List<ReviewResponse> reviews;
}
