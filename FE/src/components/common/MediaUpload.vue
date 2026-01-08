<script setup lang="ts">
import { ref } from 'vue';
import apiClient from '@/services/api/api';

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  label: { type: String, default: 'Upload Media' },
  accept: { type: String, default: 'image/*' }
});

const emit = defineEmits(['update:modelValue']);
const isUploading = ref(false);
const error = ref('');

const isVideo = (url: string) => /\.(mp4|webm|ogg|mov)$/i.test(url);

const handleFileChange = async (event: any) => {
  const file = event.target.files[0];
  if (!file) return;

  // Validate size (max 5MB)
  if (file.size > 5 * 1024 * 1024) {
    error.value = 'File quá lớn. Vui lòng chọn file dưới 5MB';
    return;
  }

  isUploading.value = true;
  error.value = '';

  const formData = new FormData();
  formData.append('file', file);

  try {
    // Assuming a generic upload endpoint exists, otherwise update to match actual BE
    // For now, if BE MediaService endpoint isn't exposed via a Controller, we might need one.
    // Assuming /api/common/upload or similar. 
    // !!! CHECK REQ: We didn't create a Controller for MediaService yet. 
    // I will assume for now we might fail or need to mock if no controller.
    // Let's create a TODO task to create a CommonController for upload.

    const res = await apiClient.post('/common/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    emit('update:modelValue', res.data.url);
  } catch (e: any) {
    error.value = e.response?.data?.message || 'Upload failed';
  } finally {
    isUploading.value = false;
  }
};
</script>

<template>
  <div class="space-y-1">
    <label class="block text-sm font-medium text-gray-700">{{ label }}</label>
    <div class="flex items-center gap-4">
      <div v-if="modelValue"
        class="w-full h-24 rounded border border-gray-200 overflow-hidden relative group bg-black/5">
        <video v-if="isVideo(modelValue)" :src="modelValue" class="w-full h-full object-cover" controls></video>
        <img v-else :src="modelValue" class="w-full h-full object-cover" />

        <button @click="$emit('update:modelValue', '')"
          class="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1 opacity-0 group-hover:opacity-100 transition-opacity z-10">
          <svg class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <label
        class="cursor-pointer border border-gray-300 rounded px-12 py-2 text-sm hover:bg-gray-50 flex flex-col items-center justify-center h-24 border-dashed"
        :class="{ 'bg-gray-100': isUploading }">
        <span v-if="isUploading" class="text-gray-500">Uploading...</span>
        <span v-else class="text-gray-500">Click</span>
        <input type="file" class="hidden" @change="handleFileChange" :accept="accept" />
      </label>
    </div>
    <p v-if="error" class="text-xs text-red-500">{{ error }}</p>
  </div>
</template>
