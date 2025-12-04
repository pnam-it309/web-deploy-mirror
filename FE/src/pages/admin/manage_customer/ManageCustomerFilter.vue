<template>
  <DivCustom label="Bộ lọc khách hàng">
    <template #icon><FilterOutlined /></template>
    
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
      <div class="md:col-span-2">
        <InputCustom
          v-model="filter.keyword"
          label="Tìm kiếm"
          placeholder="Tên, Email, Mã khách hàng..."
          @input="onInput"
        />
      </div>

      <div>
        <SelectCustom v-model="filter.status" label="Trạng thái" @change="onChange">
          <option :value="undefined">Tất cả trạng thái</option>
          <option value="ACTIVE">Đang hoạt động</option>
          <option value="INACTIVE">Ngừng hoạt động</option>
        </SelectCustom>
      </div>
    </div>
    <div class="col-span-1 flex justify-end md:justify-start md:col-span-4 lg:col-span-4 mt-2">
        <ButtonCustom
          color="sage"
          @click="resetFilters"
          class="h-[42px] flex items-center justify-center gap-2 px-6"
        >
          <ReloadOutlined /> Làm mới
        </ButtonCustom>
      </div>
  </DivCustom>
</template>

<script setup lang="ts">
import { reactive, defineEmits } from 'vue';
import { FilterOutlined, ReloadOutlined } from '@ant-design/icons-vue';
import DivCustom from '@/components/custom/Div/DivCustom.vue';
import InputCustom from '@/components/custom/Input/InputCustom.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import SelectCustom from '@/components/custom/Select/SelectCustom.vue';

const emit = defineEmits(['filter']);
const filter = reactive({ keyword: '', status: undefined });
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
const resetFilters = () => {
  filter.keyword = '';
  filter.status = undefined;
  emitFilter();
}
</script>