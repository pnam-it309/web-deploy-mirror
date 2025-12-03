<template>
  <ModalCustom :show="true" @close="$emit('close')" size="xl">
    <template #title>
      <div class="flex items-center gap-2">
        <span class="font-bold text-lg text-[#5a483e]">Chi tiết đơn hàng</span>
        <span class="font-mono text-sm bg-gray-100 px-2 py-1 rounded text-blue-600">
           {{ order?.orderCode }}
        </span>
        <!-- Badge Status -->
        <span :class="getStatusClass(order?.orderStatus)" class="text-xs px-2 py-1 rounded border">
           {{ order?.orderStatus }}
        </span>
      </div>
    </template>

    <div v-if="!order" class="p-8 text-center">Loading...</div>
    
    <div v-else class="space-y-6">
      <!-- 1. Thông tin khách hàng -->
      <div class="grid grid-cols-2 gap-4 p-4 bg-gray-50 rounded-lg border border-gray-200">
         <div>
            <p class="text-xs text-gray-500 uppercase font-bold">Khách hàng</p>
            <p class="text-gray-900 font-medium">{{ order.customerName }}</p>
            <p class="text-sm text-gray-600">{{ order.customerPhone }}</p>
         </div>
         <div>
            <p class="text-xs text-gray-500 uppercase font-bold">Địa chỉ giao hàng</p>
            <p class="text-sm text-gray-700">{{ order.customerAddress }}</p>
         </div>
      </div>

      <!-- 2. Danh sách sản phẩm -->
      <div>
        <h4 class="font-bold text-[#5a483e] mb-2">Sản phẩm đặt mua</h4>
        <div class="border rounded-lg overflow-hidden">
          <table class="w-full text-left text-sm">
            <thead class="bg-gray-100 text-gray-700">
               <tr>
                 <th class="p-3">Sản phẩm</th>
                 <th class="p-3 text-center">SL</th>
                 <th class="p-3 text-right">Đơn giá</th>
                 <th class="p-3 text-right">Thành tiền</th>
               </tr>
            </thead>
            <tbody class="divide-y">
               <tr v-for="(item, idx) in order.items" :key="idx">
                 <td class="p-3">
                    <div class="font-medium">{{ item.productName }}</div>
                    <div class="text-xs text-gray-500 font-mono">{{ item.productSku }}</div>
                 </td>
                 <td class="p-3 text-center">{{ item.quantity }}</td>
                 <td class="p-3 text-right">{{ item.unitPrice.toLocaleString() }} ₫</td>
                 <td class="p-3 text-right font-bold">{{ item.totalPrice.toLocaleString() }} ₫</td>
               </tr>
            </tbody>
            <tfoot class="bg-gray-50">
               <tr>
                 <td colspan="3" class="p-3 text-right font-bold">Tổng tiền:</td>
                 <td class="p-3 text-right font-bold text-red-600 text-lg">{{ order.totalAmount.toLocaleString() }} ₫</td>
               </tr>
            </tfoot>
          </table>
        </div>
      </div>
    </div>

    <template #footer>
      <!-- Các nút hành động dựa trên trạng thái hiện tại -->
      <div class="flex gap-2">
        <ButtonCustom color="cream" @click="$emit('close')">Đóng</ButtonCustom>
        
        <!-- Chỉ hiện nút Duyệt nếu đang PENDING -->
        <ButtonCustom 
          v-if="order?.orderStatus === 'PENDING'" 
          color="olive" 
          @click="changeStatus('CONFIRMED')"
        >
          Xác nhận đơn
        </ButtonCustom>
        
        <ButtonCustom 
          v-if="order?.orderStatus === 'CONFIRMED'" 
          color="info" 
          @click="changeStatus('SHIPPED')"
        >
          Giao hàng
        </ButtonCustom>

        <ButtonCustom 
          v-if="['PENDING', 'CONFIRMED'].includes(order?.orderStatus)" 
          color="danger" 
          @click="changeStatus('CANCELLED')"
        >
          Huỷ đơn
        </ButtonCustom>
      </div>
    </template>
  </ModalCustom>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';

const props = defineProps({
  order: Object
});

const emit = defineEmits(['close', 'updateStatus']);

const changeStatus = (status: string) => {
  if (confirm(`Bạn có chắc muốn đổi trạng thái thành ${status}?`)) {
    emit('updateStatus', status);
  }
};

const getStatusClass = (status: string) => {
    switch(status) {
        case 'PENDING': return 'bg-yellow-100 text-yellow-800 border-yellow-200';
        case 'CONFIRMED': return 'bg-blue-100 text-blue-800 border-blue-200';
        case 'SHIPPED': return 'bg-purple-100 text-purple-800 border-purple-200';
        case 'DELIVERED': return 'bg-green-100 text-green-800 border-green-200';
        case 'CANCELLED': return 'bg-red-100 text-red-800 border-red-200';
        default: return 'bg-gray-100 text-gray-800';
    }
}
</script>