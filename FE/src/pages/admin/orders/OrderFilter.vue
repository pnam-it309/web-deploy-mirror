<template>
  <DivCustom label="Bộ lọc đơn hàng">
    <template #icon><FilterOutlined /></template>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
      <div class="md:col-span-2">
        <InputCustom
          v-model="filter.keyword"
          label="Tìm kiếm"
          placeholder="Mã đơn, Tên khách, SĐT..."
          @input="onInput"
        />
      </div>
      <div>
        <SelectCustom v-model="filter.status" label="Trạng thái" @change="emitFilter">
          <option :value="undefined">Tất cả trạng thái</option>
          <option value="PENDING">Chờ xử lý</option>
          <option value="CONFIRMED">Đã xác nhận</option>
          <option value="SHIPPED">Đang giao</option>
          <option value="DELIVERED">Đã giao</option>
          <option value="CANCELLED">Đã huỷ</option>
        </SelectCustom>
      </div>
    </div>
  </DivCustom>
</template>
<script setup lang="ts">
import { reactive, defineEmits } from 'vue';
import { FilterOutlined } from '@ant-design/icons-vue';
import DivCustom from '@/components/custom/Div/DivCustom.vue';
import InputCustom from '@/components/custom/Input/InputCustom.vue';
import SelectCustom from '@/components/custom/Select/SelectCustom.vue';

const emit = defineEmits(['filter']);
const filter = reactive({ keyword: '', status: undefined });
let debounceTimer: any = null;

const onInput = () => {
  if (debounceTimer) clearTimeout(debounceTimer);
  debounceTimer = setTimeout(() => emitFilter(), 500);
};

const emitFilter = () => {
  const payload: any = {};
  if (filter.keyword?.trim()) payload.keyword = filter.keyword.trim();
  if (filter.status) payload.status = filter.status;
  emit('filter', payload);
};
</script>