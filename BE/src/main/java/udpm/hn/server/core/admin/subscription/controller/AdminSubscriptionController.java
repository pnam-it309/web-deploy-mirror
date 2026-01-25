package udpm.hn.server.core.admin.subscription.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.repository.EmailSubscriptionRepository;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_SUBCRIPTION)
@RequiredArgsConstructor
public class AdminSubscriptionController {

    private final EmailSubscriptionRepository subscriptionRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                subscriptionRepository.findAll(),
                "Lấy danh sách subscribers thành công"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable String id) {
        subscriptionRepository.deleteById(id);
        return Helper.createResponseEntity(ResponseObject.successForward(null, "Xoá thành công"));
    }

    @GetMapping("/verified")
    public ResponseEntity<?> getVerified() {
        return Helper.createResponseEntity(ResponseObject.successForward(
                subscriptionRepository.findByIsVerifiedTrue(),
                "Lấy danh sách verified subscribers"));
    }
}
