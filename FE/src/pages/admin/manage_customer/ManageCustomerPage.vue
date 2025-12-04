<template>
  <div class="p-6 min-h-screen bg-[#f8f9fa] dark:bg-brand-dark-200 transition-colors duration-300 rounded-lg">
    <!-- FILTER -->
    <div class="mb-5">
      <ManageCustomerFilter @filter="handleFilter" />
    </div>

    <!-- TABLE CUSTOM -->
    <TableCustom
      :data="customers"
      :columns="columns"
      :loading="isLoading"
      :total="customers.length"
      :pageSize="10"
    >
      <!-- 1. Khách hàng -->
      <template #customer="{ record }">
        <div class="flex items-center">
          <img 
            :src="record.picture || 'https://ui-avatars.com/api/?name=' + record.name" 
            class="h-10 w-10 rounded-full object-cover border border-gray-200 dark:border-brand-dark-50 mr-3 shadow-sm"
            alt=""
          />
          <div>
            <div class="font-semibold text-[#6c584c] dark:text-brand-cream">{{ record.name }}</div>
            <div class="text-xs text-gray-500 dark:text-gray-400">Đăng ký: {{ new Date(record.createdDate).toLocaleDateString('vi-VN') }}</div>
          </div>
        </div>
      </template>

      <!-- 2. Liên hệ -->
      <template #contact="{ record }">
        <div class="flex items-center gap-1.5 text-sm text-gray-600 dark:text-gray-300">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-[#a98467] dark:text-brand-coffee" viewBox="0 0 20 20" fill="currentColor"><path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z" /><path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z" /></svg>
          {{ record.email }}
        </div>
      </template>

      <!-- 3. Mã Code -->
      <template #code="{ record }">
        <span class="text-xs font-mono bg-gray-50 dark:bg-white/10 px-2 py-1 rounded text-gray-600 dark:text-gray-300 border border-gray-200 dark:border-white/10">
          {{ record.code }}
        </span>
      </template>

      <!-- 4. Trạng thái -->
      <template #status="{ record }">
        <span :class="['px-2.5 py-1 rounded-full text-xs font-medium border', record.status === 'ACTIVE' ? 'bg-[#dde5b6]/40 text-[#386641] border-[#adc178]/30 dark:bg-brand-sage/20 dark:text-brand-sage dark:border-brand-sage/30' : 'bg-red-50 text-red-700 border-red-100 dark:bg-red-900/30 dark:text-red-300 dark:border-red-800']">
          {{ record.status === 'ACTIVE' ? 'Hoạt động' : 'Đã khoá' }}
        </span>
      </template>

      <!-- 5. Hành động -->
      <template #actions="{ record }">
        <div class="text-center">
          <ButtonCustom 
            :color="record.status === 'ACTIVE' ? 'mocha-soft' : 'success'" 
            size="small" 
            @click="toggleStatus(record)"
            class="min-w-[90px]"
          >
            {{ record.status === 'ACTIVE' ? 'Khoá' : 'Mở khoá' }}
          </ButtonCustom>
        </div>
      </template>
    </TableCustom>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useCustomerStore } from '@/stores/manage_customer.store';
import ManageCustomerFilter from './ManageCustomerFilter.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import TableCustom from '@/components/custom/Table/TableCustom.vue';

const customerStore = useCustomerStore();
const { customers, isLoading } = storeToRefs(customerStore);

// ĐỊNH NGHĨA CỘT
const columns = [
  { title: 'Khách hàng', key: 'customer', width: '300px' },
  { title: 'Liên hệ', key: 'contact' },
  { title: 'Mã Code', key: 'code' },
  { title: 'Trạng thái', key: 'status', align: 'center' },
  { title: 'Hành động', key: 'actions', align: 'center' },
];

onMounted(() => {
  customerStore.fetchCustomers();
});

const handleFilter = (params: any) => {
  customerStore.fetchCustomers(params);
};

const toggleStatus = async (customer: any) => {
  const action = customer.status === 'ACTIVE' ? 'KHOÁ' : 'MỞ KHOÁ';
  if (confirm(`Bạn có chắc muốn ${action} tài khoản này?`)) {
    await customerStore.toggleStatus(customer.id);
  }
};
</script>