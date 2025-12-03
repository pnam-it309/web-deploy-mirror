import { defineStore } from 'pinia';
import { OrderService, type Order } from '@/services/axios/order.services';
import type { SpringPage } from './brand.store';

export const useOrderStore = defineStore('order', {
  state: () => ({
    orders: [] as Order[],
    currentOrder: null as Order | null,
    pageData: null as SpringPage<Order> | null,
    isLoading: false,
    error: null as string | null,
  }),
  actions: {
    async fetchOrders(filterParams: any = {}) {
      this.isLoading = true;
      try {
        const params = { page: 0, size: 20, ...filterParams };
        const response = await OrderService.getAll(params);
        this.pageData = response.data;
        this.orders = this.pageData.content;
      } catch (err) {
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    },

    async fetchOrderDetail(id: string) {
      this.isLoading = true;
      this.currentDetail = null;
      try {
        const res = await OrderService.getById(id);
        this.currentOrder = res.data;
      } catch(err) {
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    },

    async updateStatus(id: string, status: string) {
      this.isLoading = true;
      try {
        await OrderService.updateStatus(id, status);
        // Reload lại list hoặc detail
        await this.fetchOrders(); 
        if (this.currentOrder && this.currentOrder.id === id) {
             this.currentOrder.orderStatus = status as any;
        }
      } catch (err) {
        console.error(err);
        throw err;
      } finally {
        this.isLoading = false;
      }
    }
  }
});