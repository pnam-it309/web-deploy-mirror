package udpm.hn.server.core.admin.products.modal.request;

import lombok.Data;

@Data
public class ImageRequest {
    String url;
    String altText;
    Integer sortOrder;
}
