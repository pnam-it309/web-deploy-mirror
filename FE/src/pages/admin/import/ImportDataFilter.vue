<template>
  <div class="space-y-4">
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <div>
        <label for="search" class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
        <input
          id="search"
          v-model="filters.search"
          type="text"
          class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
          placeholder="Nhập từ khóa..."
          @keyup.enter="handleSearch"
        />
      </div>
      
      <div>
        <label for="status" class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
        <select
          id="status"
          v-model="filters.status"
          class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
        >
          <option value="">Tất cả</option>
          <option value="pending">Chờ xử lý</option>
          <option value="processing">Đang xử lý</option>
          <option value="completed">Hoàn thành</option>
          <option value="failed">Lỗi</option>
        </select>
      </div>
      
      <div>
        <label for="dateRange" class="block text-sm font-medium text-gray-700 mb-1">Ngày tạo</label>
        <input
          id="dateRange"
          v-model="filters.dateRange"
          type="date"
          class="w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
        />
      </div>
    </div>
    
    <div class="flex justify-end space-x-3 pt-2">
      <button
        type="button"
        @click="handleReset"
        class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
      >
        Đặt lại
      </button>
      <button
        type="button"
        @click="handleSearch"
        class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
      >
        <div class="flex items-center">
          <svg v-if="isLoading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          <span>Tìm kiếm</span>
        </div>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const emit = defineEmits(['search', 'reset']);

const isLoading = ref(false);
const filters = ref({
  search: '',
  status: '',
  dateRange: ''
});

const handleSearch = async () => {
  try {
    isLoading.value = true;
    // Emit search event with current filters
    emit('search', { ...filters.value });
  } finally {
    isLoading.value = false;
  }
};

const handleReset = () => {
  // Reset all filters
  filters.value = {
    search: '',
    status: '',
    dateRange: ''
  };
  
  // Emit reset event
  emit('reset');
};
</script>