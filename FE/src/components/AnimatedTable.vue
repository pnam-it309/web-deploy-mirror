<template>
  <div class="table-container">
    <!-- Loading state -->
    <div v-if="animations.state.isLoading" class="loading-overlay">
      <div class="flex items-center justify-center h-32">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
        <span class="ml-2 text-gray-600">Loading...</span>
      </div>
    </div>

    <!-- Table -->
    <div class="overflow-x-auto bg-white rounded-lg shadow">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th
              v-for="(header, index) in headers"
              :key="header.key"
              :class="[
                'px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider',
                header.sortable ? 'cursor-pointer hover:bg-gray-100 table-transition' : ''
              ]"
              @click="header.sortable ? handleSort(header.key) : null"
            >
              <div class="flex items-center">
                {{ header.label }}
                <ArrowsUpDownIcon
                  v-if="header.sortable"
                  :class="[
                    'ml-1 h-4 w-4',
                    sortKey === header.key ? 'text-indigo-600' : 'text-gray-400'
                  ]"
                />
              </div>
            </th>
            <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
              Actions
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <!-- Skeleton loading rows -->
          <tr v-for="n in skeletonRows" :key="`skeleton-${n}`" class="skeleton-row">
            <td v-for="i in headers.length" :key="i" class="px-6 py-4 whitespace-nowrap">
              <div class="skeleton h-4 rounded"></div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right">
              <div class="skeleton h-8 w-16 rounded"></div>
            </td>
          </tr>

          <!-- Data rows -->
          <tr
            v-for="(row, rowIndex) in displayedRows"
            :key="row.id || rowIndex"
            :style="animations.getRowStyle(rowIndex)"
            :class="[
              'table-row-hover',
              animations.state.selectedRowIndex === rowIndex ? 'bg-indigo-50' : ''
            ]"
            @mouseenter="animations.handleRowHover(rowIndex)"
            @mouseleave="animations.handleRowHover(null)"
            @click="animations.handleRowClick(rowIndex)"
          >
            <td
              v-for="(header, colIndex) in headers"
              :key="header.key"
              :style="animations.getCellStyle(rowIndex, colIndex)"
              class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"
            >
              {{ row[header.key] }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
              <button
                @click.stop="handleAction(row)"
                class="text-indigo-600 hover:text-indigo-900 table-transition"
                :style="animations.getCellStyle(rowIndex, headers.length)"
              >
                <PencilIcon class="h-4 w-4" />
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div v-if="showPagination" class="mt-4 flex items-center justify-between">
      <div class="text-sm text-gray-700">
        Showing {{ (currentPage - 1) * itemsPerPage + 1 }} to
        {{ Math.min(currentPage * itemsPerPage, totalItems) }} of {{ totalItems }} results
      </div>
      <div class="flex space-x-2">
        <button
          @click="currentPage = Math.max(1, currentPage - 1)"
          :disabled="currentPage === 1"
          class="px-3 py-1 border border-gray-300 rounded-md disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50 table-transition"
        >
          Previous
        </button>
        <button
          @click="currentPage = Math.min(totalPages, currentPage + 1)"
          :disabled="currentPage === totalPages"
          class="px-3 py-1 border border-gray-300 rounded-md disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50 table-transition"
        >
          Next
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useTableAnimations } from '@/composable/useTableAnimations'
import { PencilIcon, ArrowsUpDownIcon } from '@heroicons/vue/24/outline'

interface TableColumn {
  key: string
  label: string
  sortable?: boolean
}

interface TableProps {
  data: any[]
  columns: TableColumn[]
  loading?: boolean
  itemsPerPage?: number
  showPagination?: boolean
  animationConfig?: any
}

// Props
const props = withDefaults(defineProps<TableProps>(), {
  loading: false,
  itemsPerPage: 10,
  showPagination: true,
})

// Composables
const animations = useTableAnimations(props.animationConfig)

// Reactive data
const currentPage = ref(1)
const sortKey = ref('')
const sortOrder = ref<'asc' | 'desc'>('asc')
const skeletonRows = ref(5)

// Computed
const totalItems = computed(() => props.data.length)
const totalPages = computed(() => Math.ceil(totalItems.value / props.itemsPerPage))

const displayedRows = computed(() => {
  let sorted = [...props.data]

  if (sortKey.value) {
    sorted.sort((a, b) => {
      const aVal = a[sortKey.value]
      const bVal = b[sortKey.value]

      if (sortOrder.value === 'asc') {
        return aVal < bVal ? -1 : aVal > bVal ? 1 : 0
      } else {
        return aVal > bVal ? -1 : aVal < bVal ? 1 : 0
      }
    })
  }

  const start = (currentPage.value - 1) * props.itemsPerPage
  const end = start + props.itemsPerPage

  return sorted.slice(start, end)
})

const headers = computed(() =>
  props.columns.map(col => ({
    ...col,
    sortable: col.sortable !== false,
  }))
)

// Methods
const handleSort = (key: string) => {
  if (sortKey.value === key) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortKey.value = key
    sortOrder.value = 'asc'
  }
}

const handleAction = (row: any) => {
  // Handle row action (edit, delete, etc.)
  console.log('Action on row:', row)
}

// Watch for data changes to restart animations
watch(() => props.data, (newData) => {
  if (newData.length > 0) {
    animations.startLoadingAnimation(newData.length)
  }
}, { immediate: true })

// Initialize animations when component mounts
onMounted(() => {
  if (props.data.length > 0) {
    animations.startLoadingAnimation(props.data.length)
  }
})
</script>

<style scoped>
.table-container {
  position: relative;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
}

.skeleton-row {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

.table-transition {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Global table animations */
@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

@keyframes loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

.skeleton {
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: loading 1.5s infinite;
  border-radius: 4px;
}
</style>
