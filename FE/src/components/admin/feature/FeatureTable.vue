<script setup lang="ts">

import BaseIconButton from '@/components/base/BaseIconButton.vue';
import VueDraggable from 'vuedraggable';
import { computed } from 'vue';

const props = defineProps<{ items: any[], apps: any[] }>();
const emit = defineEmits(['edit', 'delete', 'update:items', 'reorder']);

const list = computed({
  get: () => props.items,
  set: (val) => emit('update:items', val)
});

const onDragChange = () => emit('reorder');

const getAppName = (appId: string, apps: any[]) => {
  const app = apps.find(a => a.id === appId);
  return app ? app.name : 'Unknown';
};
</script>

<template>
  <div class="overflow-x-auto">
    <table class="w-full text-left border-collapse">
          <thead class="bg-gray-50 border-b border-gray-100 dark:bg-gray-900/50 dark:border-gray-700">
        <tr>
          <th class="py-4 px-6 w-10"></th>
          <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider whitespace-nowrap">Tên chức năng</th>
          <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider whitespace-nowrap">Thuộc dự án</th>
          <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider whitespace-nowrap">Trạng thái</th>
          <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider text-center whitespace-nowrap">Thứ tự</th>
          <th class="py-4 px-6 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider text-right whitespace-nowrap">Thao tác</th>
        </tr>
      </thead>
      <VueDraggable tag="tbody" v-model="list" item-key="id" handle=".handle" @change="onDragChange"
        class="divide-y divide-gray-50 dark:divide-gray-700/50">
        <template #item="{ element: item }">
          <tr class="hover:bg-primary/5 dark:hover:bg-primary/5 transition-colors group">
            <td class="py-4 px-6 cursor-move handle text-gray-400 hover:text-gray-600 dark:text-gray-600 dark:hover:text-gray-400">
              <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8h16M4 16h16" />
              </svg>
            </td>
            <td class="py-4 px-6 min-w-[200px]">
              <div class="font-bold text-gray-900 dark:text-white group-hover:text-primary transition-colors">{{ item.name }}</div>
              <div class="text-xs text-gray-500 dark:text-gray-400 truncate max-w-xs">{{ item.description }}</div>
            </td>
            <td class="py-4 px-6 text-sm text-primary font-medium whitespace-nowrap">{{ getAppName(item.appId, apps) }}</td>
            <td class="py-4 px-6 whitespace-nowrap">
              <span v-if="item.status === 'INACTIVE'"
                class="inline-flex px-3 py-1 bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-300 rounded-full text-[10px] font-bold uppercase tracking-wider">
                Ẩn
              </span>
              <span v-else
                class="inline-flex px-3 py-1 bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-300 rounded-full text-[10px] font-bold uppercase tracking-wider">
                Hiển thị
              </span>
            </td>
            <td class="py-4 px-6 text-center text-sm text-gray-600 dark:text-gray-400 whitespace-nowrap">{{ item.sortOrder }}</td>
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
    </table>
  </div>
</template>