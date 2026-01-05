<script setup lang="ts">
import { ref } from 'vue';
import BaseCard from '@/components/base/BaseCard.vue';
import BaseSelect from '@/components/base/BaseSelect.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import type { AppResponse } from '@/types/admin.types';

defineProps<{ apps: AppResponse[] }>();
const emit = defineEmits(['filter', 'create']);
const appId = ref('');

const handleChange = () => emit('filter', appId.value);
</script>

<template>
  <BaseCard class="mb-6">
    <div class="flex flex-col md:flex-row gap-4 items-end">
      <div class="flex-1 w-full md:w-1/3">
        <BaseSelect 
          v-model="appId" 
          :options="apps.map(a => ({ value: a.id, label: a.name }))"
          label="LỌC THEO DỰ ÁN"
          placeholder="-- Tất cả dự án --"
          @update:model-value="handleChange"
        />
      </div>
      <div class="flex gap-2">
        <BaseButton variant="primary" @click="emit('create')">+ Thêm chức năng</BaseButton>
      </div>
    </div>
  </BaseCard>
</template>