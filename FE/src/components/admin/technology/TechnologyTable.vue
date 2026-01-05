<script setup lang="ts">
import BaseCard from '@/components/base/BaseCard.vue';
import BaseIconButton from '@/components/base/BaseIconButton.vue';
import type { Technology } from '@/types/admin.types';

defineProps<{ items: Technology[] }>();
const emit = defineEmits(['edit', 'delete']);
</script>

<template>
  <BaseCard class="p-0 overflow-hidden">
    <table class="w-full text-left border-collapse">
      <thead class="bg-gray-50 border-b border-gray-100">
        <tr>
          <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider w-24">Icon</th>
          <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider">Tên Công nghệ</th>
          <th class="py-4 px-6 text-[11px] font-bold text-dark uppercase tracking-wider text-right">Thao tác</th>
        </tr>
      </thead>
      <tbody class="divide-y divide-gray-50">
        <tr v-for="item in items" :key="item.id" class="hover:bg-yellow-50/10 transition-colors">
          <td class="py-4 px-6">
            <div class="w-10 h-10 rounded bg-white border border-gray-200 flex items-center justify-center p-1">
              <img v-if="item.icon" :src="item.icon" class="w-full h-full object-contain" onerror="this.style.display='none'"/>
              <span v-else class="text-xs font-bold text-gray-400">{{ item.name.charAt(0) }}</span>
            </div>
          </td>
          <td class="py-4 px-6 font-bold text-dark">{{ item.name }}</td>
          <td class="py-4 px-6 text-right flex justify-end gap-1">
            <BaseIconButton variant="primary" @click="emit('edit', item)">
               <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" /></svg>
            </BaseIconButton>
            <BaseIconButton variant="danger" @click="emit('delete', item.id)">
               <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
            </BaseIconButton>
          </td>
        </tr>
      </tbody>
    </table>
  </BaseCard>
</template>