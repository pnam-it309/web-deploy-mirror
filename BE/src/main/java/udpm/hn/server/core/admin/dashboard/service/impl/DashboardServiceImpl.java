package udpm.hn.server.core.admin.dashboard.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import udpm.hn.server.core.admin.dashboard.dto.response.DashboardStatsResponse;
import udpm.hn.server.core.admin.dashboard.dto.response.RevenueChartResponse;
import udpm.hn.server.core.admin.dashboard.service.DashboardService;
import udpm.hn.server.core.admin.manage_customer.repository.CustomerManageRepository;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.admin.order.repository.OrderManageRepository;
import udpm.hn.server.core.admin.product.dto.response.ProductResponse;
import udpm.hn.server.core.admin.product.repository.ProductManageRepository;
import udpm.hn.server.entity.Order;
import udpm.hn.server.entity.Product;
// import udpm.hn.server.infrastructure.core.constant.EntityStatus; // Không còn cần thiết ở đây

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final OrderManageRepository orderRepository;
    private final ProductManageRepository productRepository;
    private final CustomerManageRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats(String startDate, String endDate) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0);

        // Tính toán các chỉ số (Logic đơn giản cho demo)
        BigDecimal revenue = orderRepository.sumTotalAmountByDateRange(startOfMonth, now);
        long orders = orderRepository.count();
        long products = productRepository.count();
        long customers = customerRepository.count();

        // Trả về DTO
        return DashboardStatsResponse.builder()
                .totalRevenue(revenue != null ? revenue : BigDecimal.ZERO)
                .totalOrders(orders)
                .totalProducts(products)
                .totalCustomers(customers)
                .revenueChange(10.5) // Mock % thay đổi (cần logic so sánh tháng trước để tính thật)
                .ordersChange(5.2)
                .productsChange(2.1)
                .customersChange(1.8)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public RevenueChartResponse getChartData(String year) {
        // Mock data cho biểu đồ (Vì query group by month trong JPA khá phức tạp để viết nhanh)
        List<String> labels = List.of("T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12");
        List<BigDecimal> revenue = new ArrayList<>();
        List<Long> orders = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            revenue.add(BigDecimal.valueOf(Math.random() * 10000000));
            orders.add((long) (Math.random() * 50));
        }

        return RevenueChartResponse.builder()
                .labels(labels)
                .revenue(revenue)
                .orders(orders)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getRecentOrders() {
        return orderRepository.findRecentOrders(PageRequest.of(0, 5))
                .stream()
                .map(this::convertOrderToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts() {
        // --- SỬA LỖI Ở ĐÂY ---
        // Gọi hàm findLowStockProducts MỚI (không có tham số status)
        // Để lấy tất cả sản phẩm tồn kho thấp bất kể trạng thái
        List<Product> products = productRepository.findLowStockProducts(PageRequest.of(0, 5));

        return products.stream()
                .map(this::convertProductToResponse)
                .collect(Collectors.toList());
    }

    // --- Helper Methods ---

    private ProductResponse convertProductToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSku(product.getSku());
        response.setSlug(product.getSlug());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setStatus(product.getStatus());
        response.setUnit(product.getUnit());

        if (product.getBrand() != null) {
            response.setBrandName(product.getBrand().getName());
        }
        if (product.getCategory() != null) {
            response.setCategoryName(product.getCategory().getName());
        }
        return response;
    }

    private OrderResponse convertOrderToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderCode(order.getOrderCode());
        response.setCustomerName(order.getCustomerName());
        response.setTotalAmount(order.getTotalAmount());
        response.setOrderStatus(order.getOrderStatus());
        // response.setCreatedDate(order.getCreatedDate());
        return response;
    }
}