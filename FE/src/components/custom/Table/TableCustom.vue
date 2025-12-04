<script setup lang="ts">
import { ref, watch } from 'vue'
import { Pagination } from 'ant-design-vue'

const props = defineProps({
  data: { type: Array, required: true },
  columns: { type: Array as () => any[], required: true },
  loading: { type: Boolean, default: false },
  pageSize: { type: Number, default: 10 },
  total: { type: Number, required: true }
})

const emit = defineEmits(['change'])

// Slot động cho từng cột
defineSlots<{
  default: (props: { item: any }) => any
  [key: string]: (props: { record: any, index: number }) => any
}>()

const currentPage = ref(1)
const currentPageSize = ref(props.pageSize)

const handlePageChange = (page: number, pageSize?: number) => {
  currentPage.value = page
  if (pageSize) currentPageSize.value = pageSize
  emit('change', page, pageSize)
}

watch(() => props.pageSize, (val) => currentPageSize.value = val)
</script>

<template>
  <div class="flex flex-col mt-4">
    
    <!-- LOADING STATE -->
    <div v-if="loading" class="p-12 text-center border rounded-xl bg-white/50 dark:bg-brand-dark-100/50 border-[#e6dfc0] dark:border-brand-dark-50 backdrop-blur-sm">
      <div class="animate-spin inline-block w-8 h-8 border-4 border-current border-t-transparent text-[#adc178] rounded-full mb-3"></div>
      <p class="text-gray-500 dark:text-gray-400 text-sm font-medium animate-pulse">Đang tải dữ liệu...</p>
    </div>

    <!-- TABLE CONTAINER -->
    <div 
      v-else 
      class="
        overflow-hidden rounded-xl border shadow-sm transition-all duration-300 hover:shadow-md
        border-[#e6dfc0] bg-white/90 backdrop-blur-sm
        dark:border-brand-dark-50 dark:bg-brand-dark-100/90
      "
    >
      <div class="overflow-x-auto">
        <table class="w-full text-left border-collapse">
          
          <!-- HEADER -->
          <thead class="bg-[#f7f9ef] dark:bg-brand-dark-300">
            <tr>
              <th 
                v-for="col in columns" 
                :key="col.key"
                class="px-6 py-4 text-sm font-bold text-[#5a483e] dark:text-brand-sage border-b border-[#e6dfc0] dark:border-brand-dark-50 whitespace-nowrap"
                :class="{'text-center': col.align === 'center', 'text-right': col.align === 'right'}"
                :style="{ width: col.width }"
              >
                {{ col.title }}
              </th>
            </tr>
          </thead>

          <!-- BODY -->
          <tbody class="bg-white dark:bg-brand-dark-200 divide-y divide-[#f0ead2] dark:divide-brand-dark-50">
            
            <!-- EMPTY STATE -->
            <tr v-if="data.length === 0">
              <td :colspan="columns.length" class="p-12 text-center bg-[#fffdf5]/50 dark:bg-white/5">
                <div class="flex flex-col items-center justify-center text-gray-400 dark:text-gray-500">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mb-2 opacity-50" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" /></svg>
                  <span class="text-sm font-medium">Chưa có dữ liệu</span>
                </div>
              </td>
            </tr>

            <!-- DATA ROWS -->
            <tr
              v-else
              v-for="(record, index) in data"
              :key="index"
              class="
                hover:bg-[#f0ead2]/30 dark:hover:bg-white/5 
                transition-colors duration-150
                odd:bg-white dark:odd:bg-transparent 
                even:bg-[#fffdf5] dark:even:bg-white/[0.02]
              "
            >
              <td 
                v-for="col in columns" 
                :key="col.key"
                class="px-6 py-4 text-sm text-gray-700 dark:text-gray-300"
                :class="{'text-center': col.align === 'center', 'text-right': col.align === 'right'}"
              >
                <!-- DYNAMIC SLOT -->
                <slot :name="col.key" :record="record" :index="index">
                  {{ record[col.dataIndex || col.key] }}
                </slot>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- PAGINATION -->
    <div v-if="total > 0" class="py-4 flex justify-end">
      <Pagination
        :current="currentPage"
        :total="total"
        :pageSize="currentPageSize"
        @change="handlePageChange"
        show-size-changer
        show-quick-jumper
        :show-total="(total: number) => `Tổng ${total} bản ghi`"
        class="custom-pagination"
      />
    </div>
  </div>
</template>

<style scoped>
/* Dark Mode Pagination Override */
:global(.dark) :deep(.ant-pagination) { color: #f0ead2; }
:global(.dark) :deep(.ant-pagination-item a) { color: #f0ead2; }
:global(.dark) :deep(.ant-pagination-item-active) { 
  background-color: #adc178; border-color: #adc178; 
}
:global(.dark) :deep(.ant-pagination-item-active a) { color: #241c18; font-weight: bold; }
:global(.dark) :deep(.ant-pagination-prev .ant-pagination-item-link),
:global(.dark) :deep(.ant-pagination-next .ant-pagination-item-link) {
  background-color: transparent; color: #f0ead2; border-color: #5e504a;
}
:global(.dark) :deep(.ant-select-selector) {
  background-color: transparent !important; color: #f0ead2 !important; border-color: #6c584c !important;
}
:global(.dark) :deep(.ant-select-arrow) { color: #f0ead2 !important; }
</style>