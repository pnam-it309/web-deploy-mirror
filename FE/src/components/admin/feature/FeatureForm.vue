<script setup lang="ts">
import { reactive, watch } from 'vue';
import BaseInput from '@/components/base/BaseInput.vue';
import BaseTextarea from '@/components/base/BaseTextarea.vue';
import BaseSelect from '@/components/base/BaseSelect.vue';

const props = defineProps<{ initialData?: any, apps: any[] }>();
const emit = defineEmits(['submit', 'cancel']);

const form = reactive({
  id: '',
  name: '',
  description: '',
  appId: '',
  imagePreview: '',
  sortOrder: 1
});

watch(() => props.initialData, (newVal) => {
  if (newVal) Object.assign(form, newVal);
  else Object.assign(form, { id: '', name: '', description: '', appId: props.apps.length ? props.apps[0].id : '', imagePreview: '', sortOrder: 1 });
}, { immediate: true });
</script>

<template>
  <form @submit.prevent="emit('submit', { ...form })" class="space-y-4">
    <BaseInput v-model="form.name" label="Tên chức năng (*)" required />
    <BaseSelect 
      v-model="form.appId" 
      :options="apps.map(a => ({ value: a.id, label: a.name }))" 
      label="Thuộc dự án (*)" 
    />
    <BaseTextarea v-model="form.description" label="Mô tả" />
    <BaseInput v-model="form.imagePreview" label="Ảnh minh hoạ" />
    <BaseInput v-model="form.sortOrder" type="number" label="Thứ tự" />

    <div class="flex justify-end gap-3 pt-4 border-t border-gray-50 mt-6">
      <button type="button" @click="emit('cancel')" class="px-4 py-2 text-sm text-gray-500 hover:text-dark">Huỷ bỏ</button>
      <button type="submit" class="px-6 py-2 bg-primary text-dark font-medium rounded-sm hover:bg-yellow-500 transition-colors">Lưu lại</button>
    </div>
  </form>
</template>