<script setup lang="ts">
import { ref, watch } from 'vue'
import { Pagination } from 'ant-design-vue'

const props = defineProps({
  data: {
    type: Array,
    required: true
  },
  columns: {
    type: Array as () => any[], // Type rõ ràng hơn
    required: true
  },
  pageSize: {
    type: Number,
    default: 10 // Tăng mặc định lên 10 cho hợp lý
  },
  total: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['change'])

// Provide typing for slot props so consumers don't see 'unknown'
defineSlots<{
  default: (props: { item: any }) => any
}>()

const currentPage = ref(1)
const currentPageSize = ref(props.pageSize)

const handlePageChange = (page: number, pageSize?: number) => {
  currentPage.value = page
  if (pageSize) {
    currentPageSize.value = pageSize
  }
  emit('change', page, pageSize)
}

watch(
  () => props.pageSize,
  (newPageSize) => {
    currentPageSize.value = newPageSize
  }
)
</script>

<template>
  <div
    class="
      rounded-xl 
      border border-[#e6dfc0] /* Viền màu Cream đậm */
      bg-white/90 backdrop-blur-sm 
      shadow-sm 
      overflow-hidden /* Bo góc cho cả header */
      transition-all duration-300
      hover:shadow-md hover:border-[#adc178]/40
    "
  >
    <!-- Table Header -->
    <div
      class="
        grid grid-cols-12 
        border-b border-[#e6dfc0] 
        py-3.5 px-4 md:px-6 
        bg-[#f7f9ef] /* Nền Header màu Sage nhạt */
        text-[#5a483e] /* Chữ màu nâu đậm */
        font-bold text-sm tracking-wide
      "
    >
      <div v-for="column in columns" :key="column.key" :class="column.class">
        <!-- Căn giữa hoặc trái tuỳ ý, ở đây giữ nguyên logic cũ -->
        <p class="flex items-center" :class="{'justify-center': column.align === 'center', 'justify-end': column.align === 'right'}">
          {{ column.title }}
        </p>
      </div>
    </div>

    <!-- Table Rows -->
    <!-- Kiểm tra nếu data rỗng -->
    <div v-if="data.length === 0" class="py-8 text-center text-gray-500 italic bg-white">
      Chưa có dữ liệu hiển thị.
    </div>

    <div
      v-else
      v-for="(item, index) in data"
      :key="index"
      class="
        grid grid-cols-12 
        border-t border-[#f0ead2] /* Viền giữa các dòng màu Cream */
        py-3 px-4 sm:grid-cols-12 md:px-6 
        items-center 
        transition-colors duration-150
        
        /* Hiệu ứng Zebra (Xen kẽ) theo Brand Palette */
        odd:bg-white 
        even:bg-[#fffdf5] /* Cream siêu nhạt */
        
        /* Hover Effect */
        hover:bg-[#f0ead2]/40 /* Coffee sữa nhạt khi hover */
      "
    >
      <slot :item="item"></slot>
    </div>
  </div>

  <!-- Pagination and Page Size Selector -->
  <div class="py-4 px-4 flex justify-end">
    <Pagination
      :current="currentPage"
      :total="props.total"
      :pageSize="currentPageSize"
      @change="handlePageChange"
      show-size-changer
      show-quick-jumper
      :show-total="(total: number) => `Tổng ${total} bản ghi`"
      class="custom-pagination"
    />
  </div>
</template>

<style scoped>
.break-words {
  word-wrap: break-word;
  word-break: break-word;
  white-space: normal;
  padding-right: 10px;
}

/* Tuỳ chỉnh màu cho Ant Pagination để hợp tông (Optional) */
:deep(.ant-pagination-item-active) {
  border-color: #adc178; /* Olive */
  background-color: #f7f9ef; /* Sage nhạt */
}
:deep(.ant-pagination-item-active a) {
  color: #386641; /* Xanh rêu đậm */
}
:deep(.ant-pagination-item:hover) {
  border-color: #6c584c; /* Mocha */
}
:deep(.ant-pagination-item:hover a) {
  color: #6c584c;
}
</style>