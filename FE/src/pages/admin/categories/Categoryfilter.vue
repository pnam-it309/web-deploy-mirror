<template>
  <DivCustom label="Bộ lọc danh mục">
    <template #icon>
      <FilterOutlined />
    </template>

    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
      <!-- 1. Từ khóa (keyword) -->
      <div class="col-span-1 md:col-span-4 lg:col-span-1">
        <InputCustom
          v-model="filter.keyword"
          label="Tìm kiếm"
          placeholder="Tên, mô tả..."
          @input="onInput"
        />
      </div>

      <!-- 2. Danh mục cha (parentId) -->
      <div>
        <SelectCustom
          v-model="filter.parentId"
          label="Danh mục cha"
          @change="onChange"
        >
          <option :value="undefined">Tất cả danh mục cha</option>
          <option
            v-for="category in availableParentCategories"
            :key="category.id"
            :value="category.id"
          >
            {{ category.name }}
          </option>
        </SelectCustom>
      </div>

      <!-- 3. Loại danh mục (type) -->
      <div>
        <SelectCustom
          v-model="filter.type"
          label="Loại danh mục"
          @change="onChange"
        >
          <option :value="undefined">Tất cả loại</option>
          <option value="root">Danh mục gốc (Không cha)</option>
          <option value="child">Danh mục con (Có cha)</option>
          <option value="has-children">Danh mục cha (Có con)</option>
        </SelectCustom>
      </div>

      <!-- 4. Trạng thái (status) -->
      <div>
        <SelectCustom
          v-model="filter.status"
          label="Trạng thái"
          @change="onChange"
        >
          <option :value="undefined">Tất cả trạng thái</option>
          <option value="ACTIVE">Đang hoạt động</option>
          <option value="INACTIVE">Ngừng hoạt động</option>
        </SelectCustom>
      </div>

      <!-- Nút Reset -->
      <div class="col-span-1 flex justify-end md:justify-start md:col-span-4 lg:col-span-4 mt-2">
        <ButtonCustom
          color="sage"
          @click="resetFilters"
          class="h-[42px] flex items-center justify-center gap-2 px-6"
        >
          <ReloadOutlined /> Làm mới
        </ButtonCustom>
      </div>
    </div>
  </DivCustom>
</template>

<script setup lang="ts">
import { reactive, computed, defineProps, defineEmits } from 'vue'
import { FilterOutlined, ReloadOutlined } from '@ant-design/icons-vue'

// Import các component Custom (Thay thế Ant Design)
import DivCustom from '@/components/custom/Div/DivCustom.vue'
import InputCustom from '@/components/custom/Input/InputCustom.vue'
import SelectCustom from '@/components/custom/Select/SelectCustom.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'

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

<style scoped>
/* Không cần style riêng vì đã dùng Tailwind và Component Custom */
</style>