import apiClient from '../../services/api/api';

// DTO cho thống kê tổng quát
export interface DashboardStats {
  totalRevenue: number;
  totalOrders: number;
  totalProducts: number;
  totalCustomers: number;
  revenueChange: number; // % thay đổi so với tháng trước
  ordersChange: number;
  productsChange: number;
  customersChange: number;
}

// DTO cho biểu đồ
export interface RevenueChartData {
  labels: string[]; // ['T1', 'T2', ...]
  revenue: number[]; // [100, 200, ...]
  orders: number[]; // [10, 20, ...]
}

// Service
export const DashboardService = {
  // GET /admin/dashboard/stats?startDate=...&endDate=...
  getStats: (params: any) => {
    return apiClient.get<DashboardStats>('/admin/dashboard/stats', { params });
  },

  // GET /admin/dashboard/chart?year=2024
  getChartData: (params: any) => {
    return apiClient.get<RevenueChartData>('/admin/dashboard/chart', { params });
  },

  // GET /admin/dashboard/recent-orders
  getRecentOrders: () => {
    return apiClient.get('/admin/dashboard/recent-orders');
  },
  
  // GET /admin/dashboard/low-stock
  getLowStockProducts: () => {
    return apiClient.get('/admin/dashboard/low-stock');
  }
};