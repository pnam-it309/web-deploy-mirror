package udpm.hn.server.core.customer.technology.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.customer.technology.service.CustomerTechnologyService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_CUSTOMER_TECHNOLOGY)
@RequiredArgsConstructor
public class CustomerTechnologyController {

    private final CustomerTechnologyService customerTechnologyService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                customerTechnologyService.getAllTechnologies(),
                "Lấy danh sách công nghệ thành công"));
    }
}
