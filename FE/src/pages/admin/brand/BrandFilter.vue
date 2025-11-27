<template>
  <DivCustom label="Bộ lọc thương hiệu">
    <template #icon><FilterOutlined /></template>
    
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
      <!-- 1. Tìm kiếm (Tên, Mã) -->
      <div>
        <InputCustom
          v-model="filter.keyword"
          label="Tìm kiếm"
          placeholder="Tìm theo tên hoặc mã thương hiệu"
          @input="onInput"
        />
      </div>

      <!-- 2. Trạng thái -->
      <div>
        <SelectCustom
          v-model="filter.status"
          label="Trạng thái"
          @change="onChange"
        >
          <option value="">Tất cả trạng thái</option>
          <option value="ACTIVE">Đang hoạt động</option>
          <option value="INACTIVE">Ngừng hoạt động</option>
        </SelectCustom>
      </div>

      <!-- Nút Reset -->
      <div class="col-span-1 flex justify-end md:justify-start md:col-span-4 lg:col-span-4 mt-2">
        <ButtonCustom
          color="sage"
          @click="resetFilter"
          class="h-[42px] flex items-center justify-center gap-2 px-6"
        >
          <ReloadOutlined /> Làm mới
        </ButtonCustom>
      </div>
    </div>
  </DivCustom>
</template>

<script setup lang="ts">
import { reactive, defineEmits } from 'vue';
import DivCustom from '@/components/custom/Div/DivCustom.vue';
import { FilterOutlined, ReloadOutlined } from '@ant-design/icons-vue';
// Import các component tùy chỉnh
import InputCustom from '@/components/custom/Input/InputCustom.vue';
import SelectCustom from '@/components/custom/Select/SelectCustom.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';

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

<style scoped>
/* Không cần style riêng vì đã dùng Tailwind */
</style>