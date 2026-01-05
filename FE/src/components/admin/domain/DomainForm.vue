<script setup lang="ts">
import { reactive, watch } from 'vue';
import BaseInput from '@/components/base/BaseInput.vue';
import BaseTextarea from '@/components/base/BaseTextarea.vue';
import type { Domain } from '@/types/admin.types';

const props = defineProps<{ initialData?: Domain | null }>();
const emit = defineEmits(['submit', 'cancel']);

const form = reactive({
  id: '',
  name: '',
  description: '',
  icon: ''
});

// Load data khi mở form
watch(() => props.initialData, (newVal) => {
  if (newVal) Object.assign(form, newVal);
  else Object.assign(form, { id: '', name: '', description: '', icon: '' });
}, { immediate: true });

const handleSubmit = () => emit('submit', { ...form });
</script>

<template>
  <form @submit.prevent="handleSubmit" class="space-y-5">
    <BaseInput v-model="form.name" label="Tên lĩnh vực (*)" placeholder="VD: E-commerce" required />
    <BaseInput v-model="form.icon" label="Icon Class" placeholder="VD: bx bx-cart" />
    <BaseTextarea v-model="form.description" label="Mô tả ngắn" placeholder="Mô tả về lĩnh vực..." />
    
    <div class="flex justify-end gap-3 pt-4 border-t border-gray-50 mt-6">
      <button type="button" @click="$emit('cancel')" class="px-4 py-2 text-sm text-gray-500 hover:text-dark">Huỷ bỏ</button>
      <button type="submit" class="px-6 py-2 bg-primary text-dark font-medium rounded-sm hover:bg-yellow-500 transition-colors">Lưu lại</button>
    </div>
  </form>
</template>