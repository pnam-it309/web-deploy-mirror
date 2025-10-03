package udpm.hn.server.core.permitall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_PUBLIC_PREFIX)
@RequiredArgsConstructor
public class PublicController {

    @GetMapping("/view_products")
    public ResponseEntity<?> getPublicProducts() {
        // TODO: Implement public product viewing
        return ResponseEntity.ok("Public products endpoint - to be implemented");
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getPublicCategories() {
        // TODO: Implement public category viewing
        return ResponseEntity.ok("Public categories endpoint - to be implemented");
    }
}
