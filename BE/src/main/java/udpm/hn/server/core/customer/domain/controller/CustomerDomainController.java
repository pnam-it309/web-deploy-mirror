package udpm.hn.server.core.customer.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.domain.service.CustomerDomainService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_DOMAIN)
@RequiredArgsConstructor
public class CustomerDomainController {

    private final CustomerDomainService customerDomainService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                customerDomainService.getAllActiveInfo(),
                "Lấy danh sách lĩnh vực thành công"));
    }
}
