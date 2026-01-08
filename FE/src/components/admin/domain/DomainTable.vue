<script setup lang="ts">
import BaseCard from '@/components/base/BaseCard.vue';
import BaseIconButton from '@/components/base/BaseIconButton.vue';
import type { Domain } from '@/types/admin.types';
import VueDraggable from 'vuedraggable';

import { computed } from 'vue';

const props = defineProps<{ items: Domain[] }>();
const emit = defineEmits(['edit', 'delete', 'update:items', 'reorder']);

const list = computed({
  get: () => props.items,
  set: (val) => emit('update:items', val)
});

const onDragChange = () => {
  emit('reorder');
};
</script>

<template>
  <BaseCard class="p-0 overflow-hidden">
    <table class="w-full text-left border-collapse">
      <thead class="bg-gray-50 border-b border-gray-100">
        <tr>
          <th class="py-4 px-6 w-10"></th> <!-- Handle -->
          <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider">Tên lĩnh vực</th>
          <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider">Mô tả</th>
          <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider">Slug</th>
          <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider">Trạng thái</th>
          <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider text-right">Thao tác</th>
        </tr>
      </thead>
      <VueDraggable tag="tbody" v-model="list" item-key="id" handle=".handle" @change="onDragChange"
        class="divide-y divide-gray-50">
        <template #item="{ element: item }">
          <tr class="hover:bg-yellow-50/10 transition-colors group">
            <td class="py-4 px-6 cursor-move handle text-gray-400 hover:text-dark">
              <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8h16M4 16h16" />
              </svg>
            </td>
            <td class="py-4 px-6 font-bold text-dark group-hover:text-primary transition-colors">{{ item.name }}</td>
            <td class="py-4 px-6 text-sm text-gray-500 truncate max-w-xs">{{ item.description || '-' }}</td>
            <td class="py-4 px-6 text-xs font-mono text-gray-400">{{ item.slug }}</td>
            <td class="py-4 px-6">
              <span :class="item.status === 'ACTIVE' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                class="px-2 py-1 rounded-full text-xs font-bold uppercase">
                {{ item.status === 'ACTIVE' ? 'Hiển thị' : 'Ẩn' }}
              </span>
            </td>
            <td class="py-4 px-6 text-right flex justify-end gap-1">
              <BaseIconButton variant="primary" @click="emit('edit', item)">
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
              </BaseIconButton>
              <BaseIconButton variant="danger" @click="emit('delete', item.id)">
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </BaseIconButton>
            </td>
          </tr>
        </template>
      </VueDraggable>

      <tbody v-if="items.length === 0">
        <tr>
          <td colspan="5" class="py-8 text-center text-gray-400 text-sm">Không tìm thấy dữ liệu</td>
        </tr>
      </tbody>
    </table>
  </BaseCard>
</template>