<script setup lang="ts">
import { ref, watch } from 'vue'
import { Pagination } from 'ant-design-vue'

const props = defineProps({
  data: {
    type: Array,
    required: true
  },
  columns: {
    type: Array,
    required: true
  },
  pageSize: {
    type: Number,
    default: 5
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
    class="rounded-md border border-stroke bg-white shadow-sm dark:border-strokedark dark:bg-boxdark"
  >
    <!-- Table Header -->
    <div
      class="grid grid-cols-12 border-b border-stroke py-3 px-4 md:px-6 2xl:px-7.5 bg-gray-50 text-gray-700 font-semibold text-sm rounded-t-md"
    >
      <div v-for="column in columns" :key="column.key" :class="column.class">
        <p class="flex justify-center items-center">{{ column.title }}</p>
      </div>
    </div>

    <!-- Table Rows -->
    <div
      v-for="item in data"
      :key="item.id"
      class="grid grid-cols-12 border-t border-stroke py-3 px-4 sm:grid-cols-12 md:px-6 2xl:px-7.5 items-center odd:bg-white even:bg-gray-50 hover:bg-gray-100 transition-colors"
    >
      <slot :item="item"></slot>
    </div>
  </div>

  <!-- Pagination and Page Size Selector -->
  <div class="py-3 px-4 md:px-6 2xl:px-7.5 flex justify-end">
    <Pagination
      :current="currentPage"
      :total="props.total"
      :pageSize="currentPageSize"
      @change="handlePageChange"
      showLessItems
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
</style>
