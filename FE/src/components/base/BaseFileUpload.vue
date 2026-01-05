<script setup lang="ts">
defineEmits(['change']);
</script>

<template>
  <div class="relative w-full">
    <label class="flex flex-col items-center justify-center w-full h-32 border-2 border-gray-300 border-dashed rounded-sm cursor-pointer bg-gray-50 hover:bg-white hover:border-primary transition-all duration-300 group">
      <div class="flex flex-col items-center justify-center pt-5 pb-6 text-gray-400 group-hover:text-primary transition-colors">
        <svg class="w-8 h-8 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path></svg>
        <p class="text-xs uppercase tracking-wider font-semibold">Tải ảnh lên</p>
      </div>
      <input type="file" class="hidden" @change="$emit('change', $event)" accept="image/*" />
    </label>
  </div>
</template>