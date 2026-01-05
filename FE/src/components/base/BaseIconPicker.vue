<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';

interface Props {
  modelValue: string;
  label?: string;
  error?: string;
}

const props = defineProps<Props>();
const emit = defineEmits(['update:modelValue']);

const isOpen = ref(false);
const searchQuery = ref('');
const containerRef = ref<HTMLElement | null>(null);

// Danh sách các Icon phổ biến (Boxicons)
const iconList = [
  'bx bx-home', 'bx bx-user', 'bx bx-cart', 'bx bx-store', 'bx bx-calendar',
  'bx bx-search', 'bx bx-bell', 'bx bx-chat', 'bx bx-envelope', 'bx bx-cog',
  'bx bx-folder', 'bx bx-file', 'bx bx-image', 'bx bx-music', 'bx bx-video',
  'bx bx-map', 'bx bx-navigation', 'bx bx-compass', 'bx bx-flag', 'bx bx-star',
  'bx bx-heart', 'bx bx-shield', 'bx bx-lock', 'bx bx-key', 'bx bx-credit-card',
  'bx bx-dollar', 'bx bx-bitcoin', 'bx bx-chart', 'bx bx-bar-chart', 'bx bx-pie-chart',
  'bx bx-bulb', 'bx bx-edit', 'bx bx-pencil', 'bx bx-trash', 'bx bx-save',
  'bx bx-share', 'bx bx-send', 'bx bx-wifi', 'bx bx-bluetooth', 'bx bx-usb',
  'bx bx-mobile', 'bx bx-laptop', 'bx bx-desktop', 'bx bx-tv', 'bx bx-joystick',
  'bx bx-football', 'bx bx-basketball', 'bx bx-briefcase', 'bx bx-book', 'bx bx-graduation',
  'bx bx-building', 'bx bx-rocket', 'bx bx-planet', 'bx bx-code', 'bx bx-terminal',
  'bx bx-bug', 'bx bx-window', 'bx bx-layer', 'bx bx-layout', 'bx bx-grid'
];

const filteredIcons = computed(() => {
  if (!searchQuery.value) return iconList;
  return iconList.filter(icon => icon.includes(searchQuery.value.toLowerCase()));
});

const selectIcon = (icon: string) => {
  emit('update:modelValue', icon);
  isOpen.value = false;
};

// Đóng khi click ra ngoài
const handleClickOutside = (event: MouseEvent) => {
  if (containerRef.value && !containerRef.value.contains(event.target as Node)) {
    isOpen.value = false;
  }
};

onMounted(() => document.addEventListener('click', handleClickOutside));
onUnmounted(() => document.removeEventListener('click', handleClickOutside));
</script>

<template>
  <div class="flex flex-col gap-1.5 relative" ref="containerRef">
    <label v-if="label" class="text-[11px] font-bold text-dark uppercase tracking-wider">
      {{ label }}
    </label>

    <div 
      @click="isOpen = !isOpen"
      class="w-full px-4 py-2.5 bg-white border border-gray-200 rounded-sm cursor-pointer flex items-center gap-3 hover:border-primary transition-colors"
      :class="{'ring-1 ring-primary/50 border-primary': isOpen}"
    >
      <div class="w-8 h-8 rounded bg-gray-100 flex items-center justify-center text-xl text-dark shrink-0">
        <i v-if="modelValue" :class="modelValue"></i>
        <span v-else class="text-xs text-gray-400">?</span>
      </div>
      
      <span v-if="modelValue" class="text-sm font-medium text-dark">{{ modelValue }}</span>
      <span v-else class="text-sm text-gray-400">Chọn biểu tượng...</span>

      <div class="ml-auto text-gray-400">
        <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
      </div>
    </div>

    <div v-if="isOpen" class="absolute top-full left-0 w-full mt-2 bg-white border border-gray-200 shadow-xl rounded-sm z-50 p-3">
      <input 
        v-model="searchQuery"
        type="text" 
        placeholder="Tìm kiếm icon (vd: home, user...)" 
        class="w-full px-3 py-2 text-sm border border-gray-200 rounded-sm mb-3 focus:outline-none focus:border-primary"
      />

      <div class="grid grid-cols-6 gap-2 max-h-48 overflow-y-auto custom-scrollbar">
        <div 
          v-for="icon in filteredIcons" 
          :key="icon"
          @click="selectIcon(icon)"
          class="aspect-square flex items-center justify-center rounded cursor-pointer hover:bg-primary/10 hover:text-primary transition-colors text-xl text-gray-600"
          :class="{'bg-primary text-white hover:bg-primary hover:text-white': modelValue === icon}"
          :title="icon"
        >
          <i :class="icon"></i>
        </div>
      </div>
      
      <div v-if="filteredIcons.length === 0" class="text-center text-xs text-gray-400 py-2">
        Không tìm thấy icon nào.
      </div>
    </div>
  </div>
</template>