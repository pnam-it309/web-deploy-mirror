<template>
  <div class="overflow-x-auto">
    <table class="min-w-full divide-y divide-gray-200">
      <thead class="bg-gray-50">
        <tr>
          <th
            v-for="col in columns"
            :key="col.key"
            scope="col"
            class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider"
            :class="col.headerClass"
          >
            {{ col.label }}
          </th>
          <th v-if="actions" scope="col" class="relative px-6 py-3">
            <span class="sr-only">Actions</span>
          </th>
        </tr>
      </thead>
      <tbody class="bg-white divide-y divide-gray-200">
        <tr v-for="(item, index) in data" :key="index" class="hover:bg-gray-50 transition-colors">
          <td
            v-for="col in columns"
            :key="col.key"
            class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"
            :class="col.cellClass"
          >
            <slot :name="`cell-${col.key}`" :item="item" :value="item[col.key]">
              {{ item[col.key] }}
            </slot>
          </td>
          <td v-if="actions" class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
            <slot name="actions" :item="item" />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { PropType } from 'vue';

interface Column {
  label: string;
  key: string;
  headerClass?: string;
  cellClass?: string;
}

defineProps({
  columns: {
    type: Array as PropType<Column[]>,
    required: true,
  },
  data: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  actions: {
    type: Boolean,
    default: false,
  },
});
</script>
