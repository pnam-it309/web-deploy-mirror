<template>
  <DivCustom label="Bộ lọc thương hiệu">
    <template #icon><FilterOutlined /></template>
    
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <!-- 1. Tìm kiếm (Tên, Mã) -->
      <div>
        <a-input
          v-model:value="filter.keyword"
          placeholder="Tìm theo tên hoặc mã thương hiệu"
          allow-clear
          @change="onInput"
        />
      </div>

      <!-- 2. Trạng thái -->
      <div>
        <a-select
          v-model:value="filter.status"
          placeholder="Trạng thái"
          allow-clear
          class="w-full"
          @change="onChange"
        >
          <a-select-option value="ACTIVE">Đang hoạt động</a-select-option>
          <a-select-option value="INACTIVE">Ngừng hoạt động</a-select-option>
        </a-select>
      </div>

      <!-- Nút Reset -->
      <div class="flex justify-end">
        <a-tooltip title="Làm mới bộ lọc">
          <a-button type="default" @click="resetFilter" class="bg-blue-50 text-blue-600 border-none">
            <ReloadOutlined /> Làm mới
          </a-button>
        </a-tooltip>
      </div>
    </div>
  </DivCustom>
</template>

<script setup lang="ts">
import { reactive, defineEmits } from 'vue';
import DivCustom from '@/components/custom/Div/DivCustom.vue';
import { FilterOutlined, ReloadOutlined } from '@ant-design/icons-vue';
import { Input as AInput, Select as ASelect, Button as AButton, Tooltip as ATooltip } from 'ant-design-vue';

const ASelectOption = ASelect.Option;
const emit = defineEmits(['filter']);

const filter = reactive({
  keyword: '',
  status: undefined as string | undefined,
});

let debounceTimer: any = null;

const onInput = () => {
  if (debounceTimer) clearTimeout(debounceTimer);
  debounceTimer = setTimeout(() => emitFilter(), 500);
};

const onChange = () => emitFilter();

const emitFilter = () => {
  const payload: any = {};
  if (filter.keyword?.trim()) payload.keyword = filter.keyword.trim();
  if (filter.status) payload.status = filter.status;
  emit('filter', payload);
};

const resetFilter = () => {
  filter.keyword = '';
  filter.status = undefined;
  emitFilter();
};
</script>