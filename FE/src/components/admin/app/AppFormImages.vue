<script setup lang="ts">
import { ref } from 'vue';
import apiClient from '@/services/api/api';
import type { AppImageRequest } from '@/types/admin.types';
import { toast } from 'vue3-toastify';

const props = defineProps<{ modelValue: AppImageRequest[] }>();
const emit = defineEmits(['update:modelValue']);
const fileInput = ref<HTMLInputElement | null>(null);
const activeIndex = ref<number | null>(null);
const isUploading = ref(false);

const addImageSlot = () => {
  const newList = [...props.modelValue, { url: '', isMain: false }];
  emit('update:modelValue', newList);
};

const removeImage = (index: number) => {
  const newList = [...props.modelValue];
  newList.splice(index, 1);
  emit('update:modelValue', newList);
};

const updateItem = (index: number, val: string) => {
  const newList = [...props.modelValue];
  newList[index] = { ...newList[index], url: val };
  emit('update:modelValue', newList);
};

const setMain = (index: number) => {
  const newList = props.modelValue.map((img, i) => ({
    ...img,
    isMain: i === index // Only one main image
  }));
  emit('update:modelValue', newList);
};

const triggerUpload = (index: number) => {
  activeIndex.value = index;
  fileInput.value?.click();
};

const handleFileChange = async (event: Event) => {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file || activeIndex.value === null) return;

  if (file.size > 5 * 1024 * 1024) {
    toast.error('File quá lớn (Max 5MB)');
    return;
  }

  isUploading.value = true;
  const formData = new FormData();
  formData.append('file', file);

  try {
    const res = await apiClient.post('/common/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    updateItem(activeIndex.value, res.data.url);
    toast.success('Upload thành công!');
  } catch (e) {
    console.error(e);
    // Fallback for demo if API fails
    // updateItem(activeIndex.value, URL.createObjectURL(file)); 
    toast.error('Upload thất bại (API chưa sẵn sàng)');
  } finally {
    isUploading.value = false;
    if (fileInput.value) fileInput.value.value = ''; // Reset input
    activeIndex.value = null;
  }
};
</script>

<template>
  <div class="bg-white rounded-lg">
    <div class="flex items-center justify-between mb-4">
      <h3 class="text-sm font-bold text-gray-800 uppercase tracking-wide flex items-center gap-2">
        <i class='bx bx-images text-lg text-primary'></i> Thư viện ảnh
      </h3>
      <button @click="addImageSlot"
        class="text-xs font-bold text-primary hover:bg-primary/5 px-3 py-1.5 rounded transition-colors flex items-center gap-1 border border-primary/20">
        <i class='bx bx-plus'></i> Thêm slot
      </button>
    </div>

    <!-- Hidden Global File Input -->
    <input type="file" ref="fileInput" class="hidden" accept="image/*" @change="handleFileChange" />

    <div class="grid grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-4">
      <!-- Image Slots -->
      <div v-for="(img, idx) in modelValue" :key="idx"
        class="group relative aspect-[4/3] rounded-lg border-2 border-dashed border-gray-300 bg-gray-50 overflow-hidden hover:border-primary transition-all duration-300">

        <!-- Empty State -->
        <div v-if="!img.url" class="absolute inset-0 flex flex-col">
          <!-- Upload Area (Clickable) -->
          <div @click="triggerUpload(idx)"
            class="flex-1 flex flex-col items-center justify-center cursor-pointer hover:bg-gray-100 transition-colors">
            <i class='bx bx-cloud-upload text-3xl text-gray-400 mb-1 group-hover:text-primary transition-colors'></i>
            <span class="text-xs font-bold text-gray-500 group-hover:text-primary">Tải ảnh lên</span>
          </div>

          <!-- Separator -->
          <div class="flex items-center gap-2 px-3">
            <div class="h-px bg-gray-200 flex-1"></div>
            <span class="text-[10px] text-gray-400 font-bold uppercase">URL</span>
            <div class="h-px bg-gray-200 flex-1"></div>
          </div>

          <!-- URL Input Area -->
          <div class="p-2 pb-3">
            <input type="text" :value="img.url" @input="(e: any) => updateItem(idx, e.target.value)"
              placeholder="Dán liên kết..."
              class="w-full text-xs border border-gray-200 rounded px-2 py-1.5 focus:border-primary focus:outline-none bg-white font-medium" />
          </div>

          <button @click="removeImage(idx)"
            class="absolute top-1 right-1 text-gray-400 hover:text-red-500 p-1 bg-white/50 rounded-full hover:bg-white transition-all">
            <i class='bx bx-x text-lg'></i>
          </button>
        </div>

        <!-- Filled State -->
        <div v-else class="w-full h-full relative border-none border-0">
          <img :src="img.url" class="w-full h-full object-cover" />

          <!-- Overlay Actions -->
          <div
            class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex flex-col justify-between p-3">
            <div class="flex justify-end">
              <button @click="removeImage(idx)"
                class="bg-white/20 backdrop-blur-sm text-white hover:bg-red-500 p-1.5 rounded-full transition-colors">
                <i class='bx bx-trash text-sm'></i>
              </button>
            </div>

            <div class="flex items-center justify-center">
              <button @click="setMain(idx)"
                class="flex items-center gap-2 px-3 py-1.5 rounded-full backdrop-blur-md transition-all border border-white/30"
                :class="img.isMain ? 'bg-yellow-400 text-white border-yellow-400 shadow-[0_0_10px_rgba(250,204,21,0.5)]' : 'bg-black/30 text-white hover:bg-black/50'">
                <i :class="img.isMain ? 'bx bxs-star' : 'bx bx-star'"></i>
                <span class="text-xs font-bold">{{ img.isMain ? 'Ảnh bìa' : 'Đặt làm bìa' }}</span>
              </button>
            </div>

            <!-- Bottom Input for Quick Edit -->
            <div class="bg-black/50 p-1 rounded backdrop-blur-sm">
              <input type="text" :value="img.url" @input="(e: any) => updateItem(idx, e.target.value)"
                class="w-full bg-transparent text-white text-[10px] border-none focus:ring-0 p-0 text-center placeholder-white/50"
                placeholder="URL..." />
            </div>
          </div>

          <!-- Default Badge if Main (Visible when not hovering) -->
          <div v-if="img.isMain"
            class="absolute top-2 left-2 bg-yellow-400 text-white text-[10px] font-bold px-2 py-0.5 rounded shadow-sm opacity-100 group-hover:opacity-0 transition-opacity">
            <i class='bx bxs-star mr-0.5'></i> MAIN
          </div>
        </div>
      </div>

      <!-- Quick Add Button -->
      <button @click="addImageSlot"
        class="aspect-[4/3] rounded-lg border-2 border-dashed border-gray-200 flex flex-col items-center justify-center text-gray-400 hover:text-primary hover:border-primary hover:bg-primary/5 transition-all duration-300 group">
        <div
          class="w-12 h-12 rounded-full bg-gray-100 group-hover:bg-white flex items-center justify-center mb-2 transition-colors shadow-sm">
          <i class='bx bx-plus text-2xl'></i>
        </div>
        <span class="text-sm font-bold">Thêm ảnh mới</span>
      </button>
    </div>
  </div>
</template>