package udpm.hn.server.core.admin.manage_customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.admin.manage_customer.service.CustomerAccountService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RequestMapping(MappingConstants.API_ADMIN_CUSTOMER)
@RequiredArgsConstructor
@RestController
public class AccountController {
    private final CustomerAccountService customerAccountService;
}
