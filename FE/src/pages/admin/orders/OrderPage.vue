<template>
  <div class="p-6 min-h-screen bg-[#f8f9fa] dark:bg-brand-dark-200 transition-colors duration-300">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
       <div class="flex items-center gap-2">
        <h2 class="text-2xl font-bold text-[#5a483e] dark:text-brand-sage">Quản lý Đơn hàng</h2>
        <span class="px-2.5 py-0.5 rounded-full bg-[#f0ead2] dark:bg-brand-coffee/20 text-[#6c584c] dark:text-brand-cream text-xs font-bold border border-[#e6dfc0] dark:border-brand-coffee/30">{{ orders.length }} đơn</span>
      </div>
    </div>

    <!-- Filter -->
    <div class="mb-5">
        <OrderFilter @filter="handleFilter" />
    </div>

    <!-- Table -->
    <TableCustom :data="orders" :columns="columns" :loading="isLoading" :total="orders.length">
        <template #orderCode="{ record }">
            <span class="font-mono text-blue-600 font-medium cursor-pointer" @click="openDetail(record)">
                {{ record.orderCode }}
            </span>
        </template>
        <template #totalAmount="{ record }">
            <span class="font-bold text-[#5a483e] dark:text-brand-sage">{{ record.totalAmount.toLocaleString() }} ₫</span>
        </template>
        <template #orderStatus="{ record }">
             <span :class="['px-2 py-1 rounded text-xs font-bold border', getStatusClass(record.orderStatus)]">
                 {{ record.orderStatus }}
             </span>
        </template>
        <template #actions="{ record }">
             <ButtonCustom color="sage-soft" size="small" @click="openDetail(record)">Xem & Xử lý</ButtonCustom>
        </template>
    </TableCustom>

    <!-- Modal Detail -->
    <OrderDetailModal 
        v-if="showModal" 
        :order="selectedOrder" 
        @close="showModal = false"
        @updateStatus="handleUpdateStatus"
    />
    
    <ToastCustom :show="toast.show" :message="toast.message" :type="toast.type" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { storeToRefs } from 'pinia';
import { useOrderStore } from '@/stores/order.store';
import OrderFilter from './OrderFilter.vue';
import OrderDetailModal from './OrderDetailModal.vue';
import TableCustom from '@/components/custom/Table/TableCustom.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import ToastCustom from '@/components/custom/Toast/ToastCustom.vue';

const orderStore = useOrderStore();
const { orders, isLoading } = storeToRefs(orderStore);

const columns = [
    { title: 'Mã đơn', key: 'orderCode' },
    { title: 'Khách hàng', key: 'customerName' },
    { title: 'SĐT', key: 'customerPhone' },
    { title: 'Tổng tiền', key: 'totalAmount', align: 'right' },
    { title: 'Trạng thái', key: 'orderStatus', align: 'center' },
    { title: 'Hành động', key: 'actions', align: 'center' }
];

const showModal = ref(false);
const selectedOrder = ref(null);
const toast = ref({ show: false, message: '', type: 'success' });

onMounted(() => orderStore.fetchOrders());

const handleFilter = (params: any) => orderStore.fetchOrders(params);

const openDetail = (order: any) => {
    selectedOrder.value = order;
    showModal.value = true;
}

const handleUpdateStatus = async (status: string) => {
    if (!selectedOrder.value) return;
    try {
        await orderStore.updateStatus(selectedOrder.value.id, status);
        toast.value = { show: true, message: 'Cập nhật trạng thái thành công!', type: 'success' };
        showModal.value = false;
    } catch (e) {
        toast.value = { show: true, message: 'Lỗi cập nhật trạng thái', type: 'error' };
    }
}

const getStatusClass = (status: string) => {
    // (Logic class màu sắc giống Modal)
    switch(status) {
        case 'PENDING': return 'bg-yellow-50 text-yellow-700 border-yellow-200';
        case 'CONFIRMED': return 'bg-blue-50 text-blue-700 border-blue-200';
        case 'SHIPPED': return 'bg-purple-50 text-purple-700 border-purple-200';
        case 'DELIVERED': return 'bg-green-50 text-green-700 border-green-200';
        case 'CANCELLED': return 'bg-red-50 text-red-700 border-red-200';
        default: return 'bg-gray-50 text-gray-700';
    }
}
</script>