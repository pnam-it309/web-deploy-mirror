<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
    <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
      <!-- Background overlay -->
      <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="$emit('close')"></div>

      <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

      <!-- Modal panel -->
      <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-2xl sm:w-full">
        <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
          
          <!-- Header -->
          <div class="flex justify-between items-start mb-6">
            <h3 class="text-lg leading-6 font-medium text-gray-900" id="modal-title">
              Chi tiết đơn hàng
            </h3>
            <button @click="$emit('close')" class="text-gray-400 hover:text-gray-500">
              <span class="sr-only">Close</span>
              <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <!-- Order Info -->
          <div class="bg-gray-50 rounded-lg p-4 mb-6 flex justify-between items-center">
            <div>
              <p class="text-xs text-gray-500 uppercase">Mã đơn hàng</p>
              <p class="font-medium text-gray-900">{{ order.number }}</p>
            </div>
            <div class="text-right">
              <p class="text-xs text-gray-500 uppercase">Ngày đặt</p>
              <p class="font-medium text-gray-900">{{ formatDate(order.date) }}</p>
            </div>
          </div>

          <!-- Product Info -->
          <div class="mb-6">
            <h4 class="text-sm font-medium text-gray-900 mb-3">Thông tin sản phẩm</h4>
            <div class="space-y-3">
              <div v-for="item in order.items" :key="item.id" class="flex justify-between text-sm">
                <div class="flex-1">
                  <p class="font-medium text-gray-900">{{ item.name }}</p>
                  <p class="text-gray-500">Số lượng: {{ item.quantity || 1 }}</p>
                </div>
                <div class="text-blue-600 font-medium">
                  {{ formatPrice(item.price || order.total) }}
                </div>
              </div>
              <div class="border-t border-gray-100 pt-3 flex justify-between items-center font-medium">
                <span>Tổng tiền:</span>
                <span class="text-blue-600 text-lg">{{ formatPrice(order.total) }}</span>
              </div>
            </div>
          </div>

          <!-- Customer Info -->
          <div class="mb-6">
            <h4 class="text-sm font-medium text-gray-900 mb-3">Thông tin khách hàng</h4>
            <div class="text-sm text-gray-600 space-y-1">
              <div class="flex justify-between">
                <span>Họ tên:</span>
                <span class="text-gray-900 font-medium">{{ order.customer?.name || 'Nguyễn Văn A' }}</span>
              </div>
              <div class="flex justify-between">
                <span>Email:</span>
                <span class="text-gray-900">{{ order.customer?.email || 'nguyenvana@example.com' }}</span>
              </div>
              <div class="flex justify-between">
                <span>Số điện thoại:</span>
                <span class="text-gray-900">{{ order.customer?.phone || '0123456789' }}</span>
              </div>
              <div class="mt-2">
                <span class="block mb-1">Địa chỉ:</span>
                <span class="text-gray-900">{{ order.shippingAddress?.address || '123 Đường ABC, Quận XYZ' }}, {{ order.shippingAddress?.city }}</span>
              </div>
            </div>
          </div>

          <!-- Order Status Timeline -->
          <div>
            <h4 class="text-sm font-medium text-gray-900 mb-4">Trạng thái đơn hàng</h4>
            <div class="flow-root">
              <ul role="list" class="-mb-8">
                <li v-for="(status, statusIdx) in timeline" :key="status.label">
                  <div class="relative pb-8">
                    <!-- Vertical line -->
                    <span 
                      v-if="statusIdx !== timeline.length - 1" 
                      class="absolute top-5 left-5 -ml-px h-full w-0.5" 
                      :class="status.completed ? 'bg-blue-600' : 'bg-gray-200'"
                      aria-hidden="true"
                    ></span>
                    
                    <div class="relative flex space-x-3">
                      <!-- Dot -->
                      <div>
                        <span 
                          class="h-10 w-10 rounded-full flex items-center justify-center ring-8 ring-white" 
                          :class="status.completed ? 'bg-blue-600' : 'bg-gray-200'"
                        >
                          <svg v-if="status.completed" class="w-6 h-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                          </svg>
                          <svg v-else class="w-6 h-6 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                          </svg>
                        </span>
                      </div>
                      <!-- Content -->
                      <div class="min-w-0 flex-1 pt-1.5 flex justify-between space-x-4">
                        <div>
                          <p class="text-sm font-medium text-gray-900">
                            {{ status.label }}
                          </p>
                          <p v-if="status.time" class="text-sm text-gray-500">
                            {{ status.time }}
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
              </ul>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  isOpen: Boolean,
  order: {
    type: Object,
    required: true,
    default: () => ({})
  }
});

defineEmits(['close']);

const formatDate = (dateString: string) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('vi-VN');
};

const formatPrice = (price: string | number) => {
  if (!price) return '0 đ';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(Number(price));
};

// Mock timeline logic based on order status
const timeline = computed(() => {
  const currentStatus = props.order.status?.toLowerCase() || 'processing';
  const date = props.order.date ? new Date(props.order.date).toLocaleString('vi-VN') : '';

  const steps = [
    { label: 'Chờ xác nhận', completed: true, time: `Cập nhật: ${date}` },
    { label: 'Đã xác nhận', completed: ['shipped', 'delivered'].includes(currentStatus) || currentStatus === 'processing', time: '' },
    { label: 'Đang giao hàng', completed: ['shipped', 'delivered'].includes(currentStatus), time: '' },
    { label: 'Đã giao hàng', completed: currentStatus === 'delivered', time: '' }
  ];

  if (currentStatus === 'cancelled') {
    return [
      { label: 'Chờ xác nhận', completed: true, time: `Cập nhật: ${date}` },
      { label: 'Đã hủy', completed: true, time: 'Đơn hàng đã bị hủy' }
    ];
  }

  return steps;
});
</script>
