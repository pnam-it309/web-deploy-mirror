<template>
    <DivCustom label="Bộ lọc khách hàng">
        <template #icon>
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
            </svg>
        </template>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end">
            <div class="md:col-span-2">
                <InputCustom v-model="filter.keyword" label="Tìm kiếm" placeholder="Tên, Email, Mã khách hàng..."
                    @input="onInput" />
            </div>

            <div>
                <SelectCustom v-model="filter.status" label="Trạng thái" @change="onChange">
                    <option :value="undefined">Tất cả trạng thái</option>
                    <option value="ACTIVE">Đang hoạt động</option>
                    <option value="INACTIVE">Ngừng hoạt động</option>
                </SelectCustom>
            </div>

            <!-- Nút Reset (Nếu cần) -->
            <!-- 
      <div class="flex justify-end md:justify-start">
        <ButtonCustom color="secondary" @click="resetFilter">Làm mới</ButtonCustom>
      </div> 
      -->
        </div>
    </DivCustom>
</template>

<script setup lang="ts">
import { reactive, defineEmits } from 'vue';
// Import component chuẩn
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

const onChange = () => emitFilter();

const emitFilter = () => {
    const payload: any = {};
    if (filter.keyword?.trim()) payload.keyword = filter.keyword.trim();
    if (filter.status) payload.status = filter.status;
    emit('filter', payload);
};
</script>