import apiClient from '../../services/api/api';
import type { SpringPage } from '@/stores/brand.store';

// Enum trạng thái (Khớp với Backend)
export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  PROCESSING = 'PROCESSING',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED',
  RETURNED = 'RETURNED'
}

export interface OrderItem {
  productName: string;
  productSku: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export interface Order {
  id: string;
  orderCode: string;
  customerName: string;
  customerPhone: string;
  customerAddress: string;
  totalAmount: number;
  orderStatus: OrderStatus;
  paymentMethod: string;
  createdDate: string; // Backend trả về LocalDate/DateTime
  items: OrderItem[];
}

export const OrderService = {
  getAll: (params: any) => {
    return apiClient.get<SpringPage<Order>>('/admin/orders/get-all-orders', { params });
  },
  
  getById: (id: string) => {
    return apiClient.get<Order>(`/admin/orders/${id}`);
  },

  // Update Status
  updateStatus: (id: string, status: string) => {
    return apiClient.put(`/admin/orders/${id}/status`, null, {
      params: { status } // Gửi qua query param
    });
  }
};