package udpm.hn.server.core.admin.dashboard.service;

import udpm.hn.server.core.admin.dashboard.dto.response.DashboardStatsResponse;
import udpm.hn.server.core.admin.dashboard.dto.response.RevenueChartResponse;
import udpm.hn.server.core.admin.order.dto.response.OrderResponse;
import udpm.hn.server.core.admin.product.dto.response.ProductResponse;

import java.util.List;

public interface DashboardService {

    // Thống kê tổng quan (Doanh thu, Đơn hàng...)
    DashboardStatsResponse getStats(String startDate, String endDate);

    // Dữ liệu biểu đồ
    RevenueChartResponse getChartData(String year);

    // Đơn hàng gần đây
    List<OrderResponse> getRecentOrders();

    // Sản phẩm sắp hết hàng (Đây là chỗ gọi hàm findLowStockProducts mà bạn đang lỗi)
    List<ProductResponse> getLowStockProducts();
}