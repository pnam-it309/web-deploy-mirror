<script setup lang="ts">
import { reactive } from 'vue';
import BaseCard from '@/components/base/BaseCard.vue';
import BaseInput from '@/components/base/BaseInput.vue';
import BaseSelect from '@/components/base/BaseSelect.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import type { Domain } from '@/types/admin.types';

// Props nhận danh sách Domain để nạp vào dropdown
defineProps<{ domains: Domain[] }>();

const emit = defineEmits(['filter', 'create']);

const filter = reactive({
  keyword: '',
  domainId: '',
  status: ''
});

const statusOptions = [
  { value: 'PENDING', label: 'Chờ duyệt' },
  { value: 'APPROVED', label: 'Đã duyệt' },
  { value: 'REJECTED', label: 'Từ chối' }
];

const handleSearch = () => emit('filter', { ...filter });
const handleReset = () => {
  filter.keyword = '';
  filter.domainId = '';
  filter.status = '';
  emit('filter', { ...filter });
};
</script>

<template>
  <BaseCard class="mb-6">
    <div class="flex flex-col md:flex-row gap-4 items-end">
      <div class="flex-1 w-full">
        <BaseInput 
          v-model="filter.keyword" 
          label="TỪ KHÓA" 
          placeholder="Tìm theo tên dự án hoặc mã SKU..." 
          @keyup.enter="handleSearch"
        />
      </div>

      <div class="w-full md:w-48">
        <BaseSelect 
          v-model="filter.domainId" 
          :options="domains.map(d => ({ value: d.id, label: d.name }))"
          label="LĨNH VỰC"
          placeholder="Tất cả"
        />
      </div>

      <div class="w-full md:w-40">
        <BaseSelect 
          v-model="filter.status" 
          :options="statusOptions"
          label="TRẠNG THÁI"
          placeholder="Tất cả"
        />
      </div>

      <div class="flex gap-2">
        <BaseButton variant="secondary" @click="handleSearch">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path></svg>
          Tìm kiếm
        </BaseButton>
        <BaseButton variant="outline" @click="handleReset">
           Xóa
        </BaseButton>
      </div>
      
      <div class="ml-auto pl-4 border-l border-gray-100">
         <BaseButton variant="primary" @click="$emit('create')">
          + Tạo dự án
        </BaseButton>
      </div>
    </div>
  </BaseCard>
</template>