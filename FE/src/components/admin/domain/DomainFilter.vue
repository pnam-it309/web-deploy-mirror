<script setup lang="ts">
import { ref } from 'vue';
import BaseCard from '@/components/base/BaseCard.vue';
import BaseInput from '@/components/base/BaseInput.vue';
import BaseButton from '@/components/base/BaseButton.vue';

const emit = defineEmits(['search', 'create']);
const keyword = ref('');

const handleSearch = () => emit('search', keyword.value);
</script>

<template>
  <BaseCard class="mb-6">
    <div class="flex flex-col md:flex-row gap-4 items-end">
      <div class="flex-1 w-full">
        <BaseInput 
          v-model="keyword" 
          label="TỪ KHOÁ" 
          placeholder="Tìm kiếm lĩnh vực..." 
          @keyup.enter="handleSearch"
        />
      </div>
      <div class="flex gap-2">
        <BaseButton variant="secondary" @click="handleSearch">Tìm kiếm</BaseButton>
        <BaseButton variant="primary" @click="$emit('create')">+ Thêm mới</BaseButton>
      </div>
    </div>
  </BaseCard>
</template>