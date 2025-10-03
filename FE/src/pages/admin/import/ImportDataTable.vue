<template>
  <div class="overflow-x-auto">
    <div v-if="loading" class="flex justify-center items-center p-8">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
    </div>
    
    <div v-else>
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              ID
            </th>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Tên file
            </th>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Người tạo
            </th>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Ngày tạo
            </th>
            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Trạng thái
            </th>
            <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
              Thao tác
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="item in items" :key="item.id" class="hover:bg-gray-50">
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
              {{ item.id }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              <div class="flex items-center">
                <svg v-if="item.fileType === 'xlsx' || item.fileType === 'xls'" class="h-5 w-5 text-green-500 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M4 4a2 2 0 012-2h4.586A2 2 0 0112 2.586L15.414 6A2 2 0 0116 7.414V16a2 2 0 01-2 2H6a2 2 0 01-2-2V4zm2 6a1 1 0 011-1h6a1 1 0 110 2H7a1 1 0 01-1-1zm1 3a1 1 0 100 2h6a1 1 0 100-2H7z" clip-rule="evenodd" />
                </svg>
                <svg v-else-if="item.fileType === 'csv'" class="h-5 w-5 text-blue-500 mr-2" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M9 2a1 1 0 000 2h2a1 1 0 100-2H9z" />
                  <path fill-rule="evenodd" d="M4 5a2 2 0 012-2 3 3 0 003 3h2a3 3 0 003-3 2 2 0 014 0v1h-2V5a1 1 0 00-1-1 1 1 0 01-1-1 1 1 0 00-1-1h-2a1 1 0 00-1 1 1 1 0 01-1 1 1 1 0 00-1 1v1H6V5a2 2 0 012-2z" clip-rule="evenodd" />
                  <path d="M4 13v6h12v-6H4zm3 2a1 1 0 100-2 1 1 0 000 2zm4-1a1 1 0 11-2 0 1 1 0 012 0zm2 1a1 1 0 100-2 1 1 0 000 2z" />
                </svg>
                <span>{{ item.fileName }}</span>
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              <div class="flex items-center">
                <div class="flex-shrink-0 h-8 w-8 rounded-full bg-gray-200 flex items-center justify-center text-gray-600 font-medium">
                  {{ item.createdBy ? item.createdBy.charAt(0).toUpperCase() : 'U' }}
                </div>
                <div class="ml-2">
                  <div class="text-sm font-medium text-gray-900">{{ item.createdByName || 'Người dùng' }}</div>
                  <div class="text-xs text-gray-500">{{ item.createdBy || 'user@example.com' }}</div>
                </div>
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
              {{ formatDate(item.createdAt) }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <span 
                :class="{
                  'px-2 inline-flex text-xs leading-5 font-semibold rounded-full': true,
                  'bg-yellow-100 text-yellow-800': item.status === 'pending',
                  'bg-blue-100 text-blue-800': item.status === 'processing',
                  'bg-green-100 text-green-800': item.status === 'completed',
                  'bg-red-100 text-red-800': item.status === 'failed'
                }"
              >
                {{ getStatusText(item.status) }}
              </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
              <div class="flex justify-end space-x-2">
                <button 
                  @click="$emit('edit', item)"
                  class="text-blue-600 hover:text-blue-900 mr-3"
                  title="Xem chi tiết"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                    <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
                  </svg>
                </button>
                <button 
                  @click="$emit('delete', item.id)"
                  class="text-red-600 hover:text-red-900"
                  title="Xóa"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clip-rule="evenodd" />
                  </svg>
                </button>
              </div>
            </td>
          </tr>
          
          <tr v-if="!items || items.length === 0">
            <td colspan="6" class="px-6 py-4 text-center text-sm text-gray-500">
              Không có dữ liệu nào được tìm thấy.
            </td>
          </tr>
        </tbody>
      </table>
      
      <!-- Pagination -->
      <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6 mt-4">
        <div class="flex-1 flex justify-between sm:hidden">
          <button 
            @click="$emit('page-change', currentPage - 1)"
            :disabled="currentPage === 1"
            class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
          >
            Trước
          </button>
          <button 
            @click="$emit('page-change', currentPage + 1)"
            :disabled="!hasNextPage"
            class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
          >
            Tiếp
          </button>
        </div>
        <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
          <div>
            <p class="text-sm text-gray-700">
              Hiển thị
              <span class="font-medium">{{ (currentPage - 1) * itemsPerPage + 1 }}</span>
              đến
              <span class="font-medium">{{ Math.min(currentPage * itemsPerPage, totalItems) }}</span>
              trong tổng số
              <span class="font-medium">{{ totalItems }}</span>
              kết quả
            </p>
          </div>
          <div>
            <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
              <button
                @click="$emit('page-change', currentPage - 1)"
                :disabled="currentPage === 1"
                :class="{
                  'relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50': true,
                  'opacity-50 cursor-not-allowed': currentPage === 1
                }"
              >
                <span class="sr-only">Trước</span>
                <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
              </button>
              
              <button
                v-for="page in visiblePages"
                :key="page"
                @click="$emit('page-change', page)"
                :class="{
                  'bg-blue-50 border-blue-500 text-blue-600': page === currentPage,
                  'bg-white border-gray-300 text-gray-500 hover:bg-gray-50': page !== currentPage,
                  'relative inline-flex items-center px-4 py-2 border text-sm font-medium': true
                }"
              >
                {{ page }}
              </button>
              
              <button
                @click="$emit('page-change', currentPage + 1)"
                :disabled="!hasNextPage"
                :class="{
                  'relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50': true,
                  'opacity-50 cursor-not-allowed': !hasNextPage
                }"
              >
                <span class="sr-only">Tiếp</span>
                <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
                </svg>
              </button>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  items: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  },
  currentPage: {
    type: Number,
    default: 1
  },
  itemsPerPage: {
    type: Number,
    default: 10
  },
  totalItems: {
    type: Number,
    default: 0
  },
  maxVisiblePages: {
    type: Number,
    default: 5
  }
});

const emit = defineEmits(['edit', 'delete', 'page-change']);

const hasNextPage = computed(() => {
  return props.currentPage * props.itemsPerPage < props.totalItems;
});

const totalPages = computed(() => {
  return Math.ceil(props.totalItems / props.itemsPerPage);
});

const visiblePages = computed(() => {
  const range = [];
  const half = Math.floor(props.maxVisiblePages / 2);
  let start = Math.max(1, props.currentPage - half);
  const end = Math.min(start + props.maxVisiblePages - 1, totalPages.value);
  
  if (end - start + 1 < props.maxVisiblePages) {
    start = Math.max(1, end - props.maxVisiblePages + 1);
  }
  
  for (let i = start; i <= end; i++) {
    range.push(i);
  }
  
  return range;
});

const formatDate = (dateString: string) => {
  if (!dateString) return '';
  const options: Intl.DateTimeFormatOptions = { 
    year: 'numeric', 
    month: 'short', 
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  };
  return new Date(dateString).toLocaleDateString('vi-VN', options);
};

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    'pending': 'Chờ xử lý',
    'processing': 'Đang xử lý',
    'completed': 'Hoàn thành',
    'failed': 'Lỗi'
  };
  return statusMap[status] || status;
};
</script>