package udpm.hn.server.core.admin.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.admin.dashboard.service.DashboardService;
import udpm.hn.server.infrastructure.core.constant.MappingConstants;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_DASHBOARD)
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(dashboardService.getStats(startDate, endDate));
    }

    @GetMapping("/chart")
    public ResponseEntity<?> getChartData(@RequestParam(required = false) String year) {
        return ResponseEntity.ok(dashboardService.getChartData(year));
    }

    @GetMapping("/recent-orders")
    public ResponseEntity<?> getRecentOrders() {
        return ResponseEntity.ok(dashboardService.getRecentOrders());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockProducts() {
        return ResponseEntity.ok(dashboardService.getLowStockProducts());
    }
}