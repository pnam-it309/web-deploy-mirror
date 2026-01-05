<script setup lang="ts">
import BaseCard from '@/components/base/BaseCard.vue';
import BaseTag from '@/components/base/BaseTag.vue';
import BaseIconButton from '@/components/base/BaseIconButton.vue';
import type { AppResponse } from '@/types/admin.types';

defineProps<{ apps: AppResponse[] }>();
const emit = defineEmits(['edit', 'delete']);

const getStatusVariant = (status: string) => {
  switch (status) {
    case 'APPROVED': return 'success';
    case 'PENDING': return 'gold'; // Màu vàng cho trạng thái chờ
    default: return 'gray';
  }
};
</script>

<template>
  <BaseCard class="p-0 overflow-hidden"> <div class="overflow-x-auto">
      <table class="w-full text-left border-collapse">
        <thead>
          <tr class="border-b border-gray-100 bg-gray-50/50">
            <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider w-20">Ảnh</th>
            <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider">Thông tin dự án</th>
            <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider">Lĩnh vực</th>
            <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider text-center">Lượt xem</th>
            <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider">Trạng thái</th>
            <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider text-right">Thao tác</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-50">
          <tr v-for="app in apps" :key="app.id" class="hover:bg-yellow-50/10 transition-colors group">
            <td class="py-4 px-6 align-middle">
              <div class="w-12 h-12 rounded-sm overflow-hidden border border-gray-100 bg-gray-50">
                <img v-if="app.thumbnail" :src="app.thumbnail" class="w-full h-full object-cover" />
                <div v-else class="w-full h-full flex items-center justify-center text-xs text-gray-400">N/A</div>
              </div>
            </td>
            
            <td class="py-4 px-6 align-middle">
              <div class="flex flex-col">
                <span class="font-bold text-dark text-sm group-hover:text-primary transition-colors cursor-pointer" @click="emit('edit', app.id)">
                  {{ app.name }}
                </span>
                <span class="text-xs text-gray-400 mt-0.5 font-mono">{{ app.sku || 'No SKU' }}</span>
              </div>
            </td>

            <td class="py-4 px-6 align-middle">
              <span class="text-sm text-gray-600">{{ app.domainName }}</span>
            </td>

            <td class="py-4 px-6 align-middle text-center">
              <span class="text-sm font-semibold text-gray-500">{{ app.viewCount }}</span>
            </td>

            <td class="py-4 px-6 align-middle">
              <BaseTag :variant="getStatusVariant(app.approvalStatus)">
                {{ app.approvalStatus }}
              </BaseTag>
            </td>

            <td class="py-4 px-6 align-middle text-right">
              <div class="flex justify-end gap-1">
                <BaseIconButton variant="primary" @click="emit('edit', app.id)" title="Chỉnh sửa">
                  <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>
                </BaseIconButton>
                <BaseIconButton variant="danger" @click="emit('delete', app.id)" title="Xoá">
                  <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                </BaseIconButton>
              </div>
            </td>
          </tr>
          
          <tr v-if="apps.length === 0">
            <td colspan="6" class="py-12 text-center text-gray-400 text-sm">
              Không có dữ liệu hiển thị
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </BaseCard>
</template>