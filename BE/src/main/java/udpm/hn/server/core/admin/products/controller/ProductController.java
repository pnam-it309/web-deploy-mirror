package udpm.hn.server.core.admin.products.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.admin.products.service.ManageProductService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PRODUCT)
@RequiredArgsConstructor
public class ProductController {
    private final ManageProductService manageProductService;
}
