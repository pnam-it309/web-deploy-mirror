<template>
  <div class="p-6 min-h-screen bg-[#f8f9fa]">
    <!-- FILTER -->
    <div class="mb-5">
      <ManageCustomerFilter @filter="handleFilter" />
    </div>

    <!-- TABLE -->
    <CardCustom>
      <div v-if="isLoading && !customers.length" class="p-12 text-center">
        <div class="animate-spin inline-block w-8 h-8 border-4 border-current border-t-transparent text-[#adc178] rounded-full mb-3"></div>
        <p class="text-gray-500 text-sm">Đang tải dữ liệu...</p>
      </div>

      <table v-else class="w-full text-left border-collapse">
        <thead class="bg-[#f7f9ef]">
          <tr>
            <th class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0]">Khách hàng</th>
            <th class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0]">Liên hệ</th>
            <th class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0]">Mã Code</th>
            <th class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0] text-center">Trạng thái</th>
            <th class="px-6 py-4 text-sm font-bold text-[#5a483e] border-b border-[#e6dfc0] text-center">Hành động</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-[#f0ead2]">
          <tr v-for="cus in customers" :key="cus.id" class="hover:bg-[#f0ead2]/30 transition-colors">
            
            <!-- Cột Khách hàng (Avatar + Tên) -->
            <td class="px-6 py-4">
              <div class="flex items-center">
                <img 
                  :src="cus.picture || 'https://ui-avatars.com/api/?name=' + cus.name" 
                  class="h-10 w-10 rounded-full object-cover border border-gray-200 mr-3"
                  alt=""
                />
                <div>
                  <div class="font-bold text-[#6c584c]">{{ cus.name }}</div>
                  <div class="text-xs text-gray-500">Đăng ký: {{ new Date(cus.createdDate).toLocaleDateString('vi-VN') }}</div>
                </div>
              </div>
            </td>

            <!-- Cột Liên hệ -->
            <td class="px-6 py-4 text-sm text-gray-600">
              <div class="flex items-center gap-1">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-gray-400" viewBox="0 0 20 20" fill="currentColor"><path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z" /><path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z" /></svg>
                {{ cus.email }}
              </div>
            </td>

            <!-- Cột Mã Code -->
            <td class="px-6 py-4">
              <span class="font-mono text-xs bg-gray-100 px-2 py-1 rounded text-gray-600">{{ cus.code }}</span>
            </td>

            <!-- Cột Trạng thái -->
            <td class="px-6 py-4 text-center">
              <span :class="['px-2.5 py-1 rounded-full text-xs font-medium border', cus.status === 'ACTIVE' ? 'bg-[#dde5b6]/40 text-[#386641] border-[#adc178]/30' : 'bg-red-50 text-red-700 border-red-100']">
                {{ cus.status === 'ACTIVE' ? 'Hoạt động' : 'Đã khoá' }}
              </span>
            </td>

            <!-- Cột Hành động -->
            <td class="px-6 py-4 text-center">
              <ButtonCustom 
                :color="cus.status === 'ACTIVE' ? 'mocha-soft' : 'success'" 
                size="small" 
                @click="toggleStatus(cus)"
              >
                {{ cus.status === 'ACTIVE' ? 'Khoá' : 'Mở khoá' }}
              </ButtonCustom>
            </td>
          </tr>

          <tr v-if="!customers.length && !isLoading">
            <td colspan="5" class="p-12 text-center text-gray-500 italic">Chưa có khách hàng nào.</td>
          </tr>
        </tbody>
      </table>
    </CardCustom>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useCustomerStore } from '@/stores/manage_customer.store';
import ManageCustomerFilter from './ManageCustomerFilter.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import CardCustom from '@/components/custom/Card/CardCustom.vue';

const customerStore = useCustomerStore();
const { customers, isLoading } = storeToRefs(customerStore);

onMounted(() => {
  customerStore.fetchCustomers();
});

const handleFilter = (params: any) => {
  customerStore.fetchCustomers(params);
};

const toggleStatus = async (customer: any) => {
  if (confirm(`Bạn có chắc muốn ${customer.status === 'ACTIVE' ? 'KHOÁ' : 'MỞ KHOÁ'} tài khoản này?`)) {
    await customerStore.toggleStatus(customer.id);
  }
};
</script>