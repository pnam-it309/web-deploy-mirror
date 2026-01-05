<script setup lang="ts">
import { reactive, watch } from 'vue';
import BaseInput from '@/components/base/BaseInput.vue';
import type { Technology } from '@/types/admin.types';

const props = defineProps<{ initialData?: Technology | null }>();
const emit = defineEmits(['submit', 'cancel']);

const form = reactive({ id: '', name: '', icon: '' });

watch(() => props.initialData, (newVal) => {
  if (newVal) Object.assign(form, newVal);
  else Object.assign(form, { id: '', name: '', icon: '' });
}, { immediate: true });
</script>

<template>
  <form @submit.prevent="emit('submit', { ...form })" class="space-y-5">
    <BaseInput v-model="form.name" label="Tên công nghệ (*)" required />
    <BaseInput v-model="form.icon" label="URL Icon" placeholder="https://..." />
    
    <div class="flex justify-end gap-3 pt-4 border-t border-gray-50 mt-6">
      <button type="button" @click="emit('cancel')" class="px-4 py-2 text-sm text-gray-500 hover:text-dark">Huỷ bỏ</button>
      <button type="submit" class="px-6 py-2 bg-primary text-dark font-medium rounded-sm hover:bg-yellow-500 transition-colors">Lưu lại</button>
    </div>
  </form>
</template>