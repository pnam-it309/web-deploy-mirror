<template>
  <div class="bg-white p-4 rounded-lg shadow-sm border border-gray-200 mb-4">
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <!-- 1. Tìm kiếm (Tên/SKU) -->
      <div class="col-span-1 md:col-span-4 lg:col-span-1">
        <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm</label>
        <div class="relative">
          <input
            v-model="filter.keyword"
            type="text"
            placeholder="Tên sản phẩm, SKU..."
            class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500 text-sm"
            @input="onInput"
          />
          <span class="absolute left-3 top-2.5 text-gray-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
          </span>
        </div>
      </div>

      <!-- 2. Thương hiệu & Danh mục -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Thương hiệu</label>
        <select 
          v-model="filter.brandId" 
          class="w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring-blue-500 text-sm py-2"
          @change="onChange"
        >
          <option value="">Tất cả thương hiệu</option>
          <option v-for="b in brands" :key="b.id" :value="b.id">{{ b.name }}</option>
        </select>
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Danh mục</label>
        <select 
          v-model="filter.categoryId" 
          class="w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring-blue-500 text-sm py-2"
          @change="onChange"
        >
          <option value="">Tất cả danh mục</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
        </select>
      </div>

      <!-- 3. Trạng thái -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
        <select 
          v-model="filter.status" 
          class="w-full border-gray-300 rounded-md shadow-sm focus:border-blue-500 focus:ring-blue-500 text-sm py-2"
          @change="onChange"
        >
          <option value="">Tất cả trạng thái</option>
          <option value="ACTIVE">Đang bán</option>
          <option value="INACTIVE">Ngừng bán</option>
        </select>
      </div>
      
      <!-- 4. Khoảng giá (Nâng cao) -->
      <div class="col-span-1 md:col-span-4 lg:col-span-2 flex items-center space-x-2">
         <div class="flex-1">
            <label class="block text-sm font-medium text-gray-700 mb-1">Giá từ</label>
            <input 
              v-model.number="filter.minPrice" 
              type="number" 
              placeholder="0" 
              class="w-full border-gray-300 rounded-md text-sm py-2"
              @input="onInput"
            />
         </div>
         <span class="pt-6 text-gray-400">-</span>
         <div class="flex-1">
            <label class="block text-sm font-medium text-gray-700 mb-1">Đến</label>
            <input 
              v-model.number="filter.maxPrice" 
              type="number" 
              placeholder="Tối đa" 
              class="w-full border-gray-300 rounded-md text-sm py-2"
              @input="onInput"
            />
         </div>
      </div>

      <!-- Nút Reset -->
      <div class="col-span-1 flex items-end">
         <button 
           @click="resetFilter"
           class="px-4 py-2 bg-gray-100 text-gray-600 rounded-md hover:bg-gray-200 text-sm font-medium w-full transition-colors"
         >
           Làm mới bộ lọc
         </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, defineProps, defineEmits } from 'vue';

const props = defineProps({
  brands: { type: Array as () => any[], default: () => [] },
  categories: { type: Array as () => any[], default: () => [] },
});

const emit = defineEmits(['filter']);

const filter = reactive({
  keyword: '',
  brandId: '',
  categoryId: '',
  status: '',
  minPrice: null as number | null,
  maxPrice: null as number | null,
});

let debounceTimer: any = null;

// Hàm debounce để tránh spam API khi gõ phím
const onInput = () => {
  if (debounceTimer) clearTimeout(debounceTimer);
  debounceTimer = setTimeout(() => {
    emitFilter();
  }, 500); // Chờ 500ms sau khi ngừng gõ mới gọi API
};

// Với Select box thì gọi ngay
const onChange = () => {
  emitFilter();
};

const emitFilter = () => {
  const payload = { ...filter };
  if (!payload.keyword) delete (payload as any).keyword;
  if (!payload.brandId) delete (payload as any).brandId;
  if (!payload.categoryId) delete (payload as any).categoryId;
  if (!payload.status) delete (payload as any).status;
  if (payload.minPrice === null || payload.minPrice === 0) delete (payload as any).minPrice;
  if (payload.maxPrice === null || payload.maxPrice === 0) delete (payload as any).maxPrice;
  
  emit('filter', payload);
};

const resetFilter = () => {
  filter.keyword = '';
  filter.brandId = '';
  filter.categoryId = '';
  filter.status = '';
  filter.minPrice = null;
  filter.maxPrice = null;
  emitFilter();
};
</script>