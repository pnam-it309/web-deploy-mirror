import apiClient from '../../services/api/api';
import type { SpringPage } from '@/stores/brand.store';

export interface Customer {
  id: string;
  code: string;
  name: string;
  email: string;
  picture: string;
  status: 'ACTIVE' | 'INACTIVE';
  createdDate: number;
}

export const CustomerService = {
  getAll: (params: any) => {
    return apiClient.get<SpringPage<Customer>>('/admin/customers/get-all-customers', { params });
  },
  getById: (id: string) => {
    return apiClient.get<Customer>(`/admin/customers/${id}`);
  },
  updateStatus: (id: string) => {
    return apiClient.put(`/admin/customers/${id}/status`);
  }
};