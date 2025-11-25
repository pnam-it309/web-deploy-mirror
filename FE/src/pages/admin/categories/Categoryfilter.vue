<template>
  <DivCustom label="Bộ lọc danh mục">
    <template #icon>
      <FilterOutlined />
    </template>

    <div class="flex flex-col gap-4">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4 w-full">
        <!-- 1. Từ khóa (keyword) -->
        <div class="flex flex-col">
          <a-input
            v-model:value="filter.keyword"
            placeholder="Tìm theo tên, mô tả"
            allow-clear
            @change="onInput"
          />
        </div>

        <!-- 2. Danh mục cha (parentId) -->
        <div class="flex flex-col">
          <a-select
            v-model:value="filter.parentId"
            placeholder="Chọn danh mục cha"
            allow-clear
            @change="onChange"
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

        <!-- 3. Loại danh mục (type) -->
        <div class="flex flex-col">
          <a-select
            v-model:value="filter.type"
            placeholder="Loại danh mục"
            allow-clear
            @change="onChange"
          >
            <a-select-option value="root">Danh mục gốc (Không cha)</a-select-option>
            <a-select-option value="child">Danh mục con (Có cha)</a-select-option>
            <a-select-option value="has-children">Danh mục cha (Có con)</a-select-option>
          </a-select>
        </div>

        <!-- 4. Trạng thái (status) -->
        <div class="flex flex-col">
          <a-select
            v-model:value="filter.status"
            placeholder="Trạng thái"
            allow-clear
            @change="onChange"
          >
            <a-select-option value="ACTIVE">Đang hoạt động</a-select-option>
            <a-select-option value="INACTIVE">Ngừng hoạt động</a-select-option>
          </a-select>
        </div>
      </div>

      <!-- Nút Reset -->
      <div class="flex justify-end">
        <a-tooltip title="Làm mới bộ lọc">
          <a-button
            type="default"
            @click="resetFilters"
            class="flex items-center gap-2"
          >
            <ReloadOutlined /> Làm mới
          </a-button>
        </a-tooltip>
      </div>
    </div>
  </DivCustom>
</template>

<script setup lang="ts">
import { reactive, computed, defineProps, defineEmits } from 'vue'
import DivCustom from '@/components/custom/Div/DivCustom.vue'
import { FilterOutlined, ReloadOutlined } from '@ant-design/icons-vue'
import { Input as AInput, Select as ASelect, Button as AButton, Tooltip as ATooltip } from 'ant-design-vue'

const ASelectOption = ASelect.Option

interface Category {
  id: string;
  name: string;
  slug: string;
  description?: string;
  parentId?: string | null;
}

const props = defineProps<{
  categories: Category[]
}>()

const emit = defineEmits(['filter'])

// --- STATE KHỚP 100% VỚI BACKEND DTO ---
const filter = reactive({
  keyword: '',
  parentId: undefined as string | undefined, // Backend: parentId
  type: undefined as string | undefined,     // Backend: type
  status: undefined as string | undefined    // Backend: status
})

// Lọc ra danh sách các danh mục có thể làm cha để hiển thị trong dropdown
const availableParentCategories = computed(() => {
  // Có thể lọc bớt hoặc hiển thị tất cả
  return props.categories;
})

let debounceTimer: any = null;

// Debounce cho input text
const onInput = () => {
  if (debounceTimer) clearTimeout(debounceTimer);
  debounceTimer = setTimeout(() => {
    emitFilter();
  }, 500);
};

// Gọi ngay cho Select
const onChange = () => {
  emitFilter();
};

const emitFilter = () => {
  // Clone và xoá các giá trị rỗng/undefined trước khi gửi
  const payload: any = {};
  
  if (filter.keyword?.trim()) payload.keyword = filter.keyword.trim();
  if (filter.parentId) payload.parentId = filter.parentId;
  if (filter.type) payload.type = filter.type;
  if (filter.status) payload.status = filter.status;

  // Emit payload này sẽ khớp với CategoryFilterRequest ở Backend
  emit('filter', payload);
}

const resetFilters = () => {
  filter.keyword = '';
  filter.parentId = undefined;
  filter.type = undefined;
  filter.status = undefined;
  emitFilter();
}
</script>