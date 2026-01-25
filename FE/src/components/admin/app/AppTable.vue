<script setup lang="ts">

import BaseTag from '@/components/base/BaseTag.vue';
import BaseIconButton from '@/components/base/BaseIconButton.vue';
import type { AppResponse } from '@/types/admin.types';

const props = defineProps<{ apps: AppResponse[] }>();
const emit = defineEmits(['edit', 'delete', 'duplicate', 'selection-change']);

const getStatusVariant = (status: string) => {
  switch (status) {
    case 'APPROVED': return 'success';
    case 'PENDING': return 'gold'; // Màu vàng cho trạng thái chờ
    default: return 'gray';
  }
};

const selectedIds = ref<string[]>([]);
const isAllSelected = computed(() => props.apps.length > 0 && selectedIds.value.length === props.apps.length);

const toggleSelectAll = () => {
  if (isAllSelected.value) {
    selectedIds.value = [];
  } else {
    selectedIds.value = props.apps.map(a => a.id);
  }
  emit('selection-change', selectedIds.value);
};

const toggleSelect = (id: string) => {
  if (selectedIds.value.includes(id)) {
    selectedIds.value = selectedIds.value.filter(i => i !== id);
  } else {
    selectedIds.value.push(id);
  }
  emit('selection-change', selectedIds.value);
};

import { ref, computed } from 'vue';

</script>

<template>
  <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr class="bg-gray-50/50 dark:bg-gray-900/50 border-b border-gray-100 dark:border-gray-700">
            <th class="py-4 px-6 w-12 text-center">
              <input type="checkbox" :checked="isAllSelected" @change="toggleSelectAll" class="rounded border-gray-300 dark:border-gray-600 text-primary focus:ring-primary bg-white dark:bg-gray-800" />
            </th>
            <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider w-20">Ảnh</th>
            <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Thông tin dự án</th>
            <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Lĩnh vực</th>
            <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider text-center">Lượt xem</th>
            <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Trạng thái</th>
            <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider text-right">Thao tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-50 dark:divide-gray-700/50">
          <tr v-for="app in apps" :key="app.id" class="hover:bg-primary/5 dark:hover:bg-primary/5 transition-colors group">
            <td class="py-4 px-6 text-center">
              <input type="checkbox" :checked="selectedIds.includes(app.id)" @change="toggleSelect(app.id)" class="rounded border-gray-300 dark:border-gray-600 text-primary focus:ring-primary bg-white dark:bg-gray-800" />
            </td>
            <td class="py-4 px-6 align-middle">
              <div class="w-12 h-12 rounded-lg overflow-hidden border border-gray-100 dark:border-gray-700 bg-gray-50 dark:bg-gray-800 shadow-sm">
                <img v-if="app.thumbnail" :src="app.thumbnail" class="w-full h-full object-cover" />
                <div v-else class="w-full h-full flex items-center justify-center text-xs text-gray-400">N/A</div>
              </div>
            </td>
            
            <td class="py-4 px-6 align-middle">
              <div class="flex flex-col">
                <span class="font-bold text-gray-900 dark:text-white text-sm group-hover:text-primary transition-colors cursor-pointer" @click="emit('edit', app.id)">
                  {{ app.name }}
                </span>
                <span class="text-xs text-gray-400 mt-1 font-mono bg-gray-100 dark:bg-gray-700 px-1 py-0.5 rounded w-fit">{{ app.sku || 'No SKU' }}</span>
              </div>
            </td>

            <td class="py-4 px-6 align-middle">
              <span class="text-sm font-medium text-gray-600 dark:text-gray-300 bg-gray-50 dark:bg-gray-700/50 px-2 py-1 rounded inline-block">{{ app.domainName }}</span>
            </td>

            <td class="py-4 px-6 align-middle text-center">
              <span class="text-sm font-bold text-gray-500 dark:text-gray-400">{{ app.viewCount }}</span>
            </td>

            <td class="py-4 px-6 align-middle">
              <BaseTag :variant="getStatusVariant(app.approvalStatus)" class="shadow-sm">
                {{ app.approvalStatus }}
              </BaseTag>
            </td>

            <td class="py-4 px-6 align-middle text-right">
              <div class="flex justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                <BaseIconButton variant="primary" @click="emit('edit', app.id)" title="Chỉnh sửa">
                  <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>
                </BaseIconButton>
                <BaseIconButton variant="secondary" @click="emit('duplicate', app.id)" title="Nhân bản">
                   <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7v8a2 2 0 002 2h6M8 7V5a2 2 0 012-2h4.586a1 1 0 01.707.293l4.414 4.414a1 1 0 01.293.707V15a2 2 0 01-2 2h-2M8 7H6a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2v-2" /></svg>
                </BaseIconButton>
                <BaseIconButton variant="danger" @click="emit('delete', app.id)" title="Xoá">
                  <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                </BaseIconButton>
              </div>
            </td>
          </tr>
          
          <tr v-if="apps.length === 0">
            <td colspan="7" class="py-16 text-center text-gray-400 dark:text-gray-500 text-sm italic">
              <div class="flex flex-col items-center gap-2">
                 <svg class="w-10 h-10 opacity-20" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" /></svg>
                 Không có dữ liệu hiển thị
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div> 
</template>