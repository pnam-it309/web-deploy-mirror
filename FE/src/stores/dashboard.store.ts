import { defineStore } from 'pinia';
import { DashboardService, type DashboardStats, type RevenueChartData } from '@/services/axios/dashboard.services';

export const useDashboardStore = defineStore('dashboard', {
  state: () => ({
    stats: {
      totalRevenue: 0,
      totalOrders: 0,
      totalProducts: 0,
      totalCustomers: 0,
      revenueChange: 0,
      ordersChange: 0,
      productsChange: 0,
      customersChange: 0
    } as DashboardStats,
    
    chartData: {
      labels: [],
      revenue: [],
      orders: []
    } as RevenueChartData,

    recentOrders: [],
    lowStockProducts: [],
    
    isLoading: false,
    error: null as string | null,
  }),

  actions: {
    async fetchDashboardData(filterParams: any = {}) {
      this.isLoading = true;
      this.error = null;
      try {
        // Gọi song song các API để tối ưu tốc độ
        const [statsRes, chartRes, ordersRes, stockRes] = await Promise.all([
          DashboardService.getStats(filterParams),
          DashboardService.getChartData(filterParams), // Gửi params để lọc biểu đồ nếu cần
          DashboardService.getRecentOrders(),
          DashboardService.getLowStockProducts()
        ]);

        this.stats = statsRes.data;
        this.chartData = chartRes.data;
        this.recentOrders = ordersRes.data;
        this.lowStockProducts = stockRes.data;

      } catch (err: any) {
        console.error('Dashboard Error:', err);
        this.error = err.message || 'Lỗi tải dữ liệu Dashboard';
        // (Optional) Nếu API chưa có, ta có thể mock tạm ở đây để UI không vỡ
      } finally {
        this.isLoading = false;
      }
    }
  }
});