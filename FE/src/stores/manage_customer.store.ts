import { defineStore } from 'pinia';
import { CustomerService, type Customer } from '@/services/axios/manage_customer.service';
import type { SpringPage } from './brand.store';

export const useCustomerStore = defineStore('customer', {
  state: () => ({
    customers: [] as Customer[],
    pageData: null as SpringPage<Customer> | null,
    isLoading: false,
    error: null as string | null,
  }),
  actions: {
    async fetchCustomers(filterParams: any = {}) {
      this.isLoading = true;
      try {
        const response = await CustomerService.getAll(filterParams);
        this.pageData = response.data;
        this.customers = this.pageData.content;
      } catch (err) {
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    },
    async toggleStatus(id: string) {
      try {
        await CustomerService.updateStatus(id);
        await this.fetchCustomers(); // Reload list
      } catch (err) {
        console.error(err);
      }
    }
  }
});