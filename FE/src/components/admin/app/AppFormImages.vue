<script setup lang="ts">
import BaseButton from '@/components/base/BaseButton.vue';
import BaseInput from '@/components/base/BaseInput.vue';
import type { AppImageRequest } from '@/types/admin.types';

const props = defineProps<{ modelValue: AppImageRequest[] }>();
const emit = defineEmits(['update:modelValue']);

const addImage = () => {
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
    isMain: i === index // Chỉ có 1 ảnh là Main
  }));
  emit('update:modelValue', newList);
};
</script>

<template>
  <div class="space-y-3">
    <div class="flex justify-between items-center mb-2">
      <h4 class="text-sm font-bold text-dark uppercase tracking-wide">Thư viện ảnh</h4>
      <BaseButton size="sm" variant="outline" @click="addImage" class="text-xs">
        + Thêm ảnh URL
      </BaseButton>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <div v-for="(img, idx) in modelValue" :key="idx" class="group relative border border-gray-200 rounded-sm bg-white p-2 hover:shadow-md transition-all">
        
        <div class="aspect-video bg-gray-100 rounded-sm overflow-hidden mb-2 relative">
          <img v-if="img.url" :src="img.url" class="w-full h-full object-cover" onerror="this.style.display='none'" />
          <div v-else class="w-full h-full flex items-center justify-center text-gray-400 text-xs">No Image</div>
          
          <div v-if="img.isMain" class="absolute top-2 left-2 bg-primary text-white text-[10px] font-bold px-2 py-0.5 rounded shadow-sm">
            MAIN COVER
          </div>
        </div>

        <BaseInput 
          :model-value="img.url" 
          @update:model-value="val => updateItem(idx, String(val))"
          placeholder="https://..." 
          class="mb-2"
        />

        <div class="flex justify-between items-center pt-1 border-t border-gray-50">
          <label class="flex items-center gap-2 cursor-pointer text-xs text-gray-600 hover:text-primary">
            <input type="radio" :checked="img.isMain" @change="setMain(idx)" class="accent-primary" />
            Đặt làm ảnh bìa
          </label>
          
          <button @click="removeImage(idx)" class="text-gray-400 hover:text-red-500 text-xs font-medium">
            Xoá
          </button>
        </div>
      </div>

      <div 
        @click="addImage"
        class="border-2 border-dashed border-gray-200 rounded-sm flex flex-col items-center justify-center min-h-[200px] cursor-pointer hover:border-primary hover:bg-yellow-50/5 transition-all text-gray-400 hover:text-primary"
      >
        <svg class="w-8 h-8 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
        <span class="text-sm font-semibold">Thêm ảnh</span>
      </div>
    </div>
  </div>
</template>