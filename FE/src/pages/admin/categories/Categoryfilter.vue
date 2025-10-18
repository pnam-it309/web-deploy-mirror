<template>
  <DivCustom label="Bộ lọc danh mục">
    <template #icon>
      <FilterOutlined />
    </template>

    <div class="flex flex-col gap-4">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4 w-full">
        <!-- Từ khóa -->
        <div class="flex flex-col">
          <a-input
            v-model:value="localKeyword"
            placeholder="Tìm theo tên, slug, mô tả"
            allow-clear
          />
        </div>

        <!-- Danh mục cha -->
        <div class="flex flex-col">
          <a-select
            v-model:value="localParentCategory"
            placeholder="Chọn danh mục cha"
            allow-clear
          >
            <a-select-option
              v-for="category in availableParentCategories"
              :key="category.id"
              :value="category.id"
            >
              {{ category.name }}
            </a-select-option>
          </a-select>
        </div>

        <!-- Loại danh mục -->
        <div class="flex flex-col">
          <a-select
            v-model:value="localCategoryType"
            placeholder="Loại danh mục"
            allow-clear
          >
            <a-select-option value="root">Danh mục gốc</a-select-option>
            <a-select-option value="child">Danh mục con</a-select-option>
            <a-select-option value="has-children">Có danh mục con</a-select-option>
          </a-select>
        </div>

        <!-- Nút làm mới -->
        <div class="flex flex-col justify-end">
          <a-tooltip title="Làm mới bộ lọc">
            <a-button
              type="default"
              @click="resetFilters"
              class="flex items-center gap-2 h-[32px]"
              style="
                background-color: #e6f4ff !important;
                color: #183153 !important;
                border: none !important;
                font-weight: 500 !important;
              "
            >
              <ReloadOutlined />
            </a-button>
          </a-tooltip>
        </div>
      </div>
    </div>
  </DivCustom>
</template>

<script setup lang="ts">
import { ref, computed, defineProps, defineEmits, watch } from 'vue'
import DivCustom from '@/components/custom/Div/DivCustom.vue'
import { FilterOutlined, ReloadOutlined } from '@ant-design/icons-vue'
import { Input as AInput, Select as ASelect, Button as AButton, Tooltip as ATooltip } from 'ant-design-vue'

const ASelectOption = ASelect.Option

// Props
interface Category {
  id: number
  name: string
  slug: string
  description?: string
  parentId?: number | null
}

const props = defineProps<{
  categories: Category[]
}>()

// Emits
const emit = defineEmits<{
  filter: [filters: any]
}>()

// Local filter state
const localKeyword = ref('')
const localParentCategory = ref<number | undefined>(undefined)
const localCategoryType = ref('')

// Available parent categories (categories that have children or are referenced as parents)
const availableParentCategories = computed(() => {
  const parentIds = new Set<number>()
  props.categories.forEach(cat => {
    if (cat.parentId) {
      parentIds.add(cat.parentId)
    }
  })

  return props.categories.filter(cat => parentIds.has(cat.id))
})

// Computed filtered categories
const filteredCategories = computed(() => {
  let filtered = props.categories

  // Filter by keyword
  if (localKeyword.value) {
    const keyword = localKeyword.value.toLowerCase()
    filtered = filtered.filter(cat =>
      cat.name.toLowerCase().includes(keyword) ||
      cat.slug.toLowerCase().includes(keyword) ||
      (cat.description && cat.description.toLowerCase().includes(keyword))
    )
  }

  // Filter by parent category
  if (localParentCategory.value !== undefined) {
    filtered = filtered.filter(cat => cat.parentId === localParentCategory.value)
  }

  // Filter by category type
  if (localCategoryType.value) {
    switch (localCategoryType.value) {
      case 'root':
        filtered = filtered.filter(cat => !cat.parentId)
        break
      case 'child':
        filtered = filtered.filter(cat => cat.parentId !== null && cat.parentId !== undefined)
        break
      case 'has-children':
        const parentIds = new Set(props.categories.map(cat => cat.parentId).filter(Boolean))
        filtered = filtered.filter(cat => parentIds.has(cat.id))
        break
    }
  }

  return filtered
})

// Watch for filter changes and emit to parent
watch([localKeyword, localParentCategory, localCategoryType], () => {
  emit('filter', {
    keyword: localKeyword.value,
    parentCategory: localParentCategory.value,
    categoryType: localCategoryType.value
  })
}, { immediate: true })

// Reset filters
const resetFilters = () => {
  localKeyword.value = ''
  localParentCategory.value = undefined
  localCategoryType.value = ''
}

// Expose filtered categories for parent component
defineExpose({
  filteredCategories
})
</script>

<style scoped>
/* Additional styles if needed */
</style>
