<template>
  <div class="flex items-center justify-end gap-3 mb-6">
    <!-- Extra Actions Slot -->
    <slot name="actions"></slot>



    <!-- Search Wrapper -->
    <div class="relative">
      <!-- Search Toggle Button -->
      <button 
        ref="searchTrigger"
        @click="toggleSearch"
        class="flex items-center gap-2 px-4 py-2 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-lg text-gray-600 dark:text-gray-300 hover:text-blue-600 hover:border-blue-600 dark:hover:text-blue-500 dark:hover:border-blue-500 transition-all shadow-sm"
        :class="{ 'border-blue-600 ring-2 ring-blue-100 dark:ring-blue-900/30': isOpen }"
      >
        <i class='bx bx-search text-xl'></i>
        <span class="font-medium hidden sm:block">Tìm kiếm</span>
      </button>

      <!-- Search/Filter Popover -->
      <Transition
        enter-active-class="transition duration-200 ease-out"
        enter-from-class="transform scale-95 opacity-0"
        enter-to-class="transform scale-100 opacity-100"
        leave-active-class="transition duration-150 ease-in"
        leave-from-class="transform scale-100 opacity-100"
        leave-to-class="transform scale-95 opacity-0"
      >
        <div 
          v-if="isOpen"
          ref="popoverRef"
          class="absolute top-12 right-0 w-80 sm:w-96 bg-white dark:bg-gray-800 rounded-xl shadow-xl border border-gray-100 dark:border-gray-700 p-4 z-50 origin-top-right"
        >
          <!-- Triangle Arrow -->
          <div class="absolute -top-2 right-8 w-4 h-4 bg-white dark:bg-gray-800 border-t border-l border-gray-100 dark:border-gray-700 transform rotate-45"></div>

          <div class="space-y-4">
            <!-- Main Search Input -->
            <div>
              <label class="block text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">
                Từ khoá
              </label>
              <div class="relative">
                  <input 
                    type="text" 
                    :value="modelValue"
                    @input="handleInput"
                    :placeholder="placeholder"
                    class="w-full pl-10 pr-4 py-2 bg-gray-50 dark:bg-gray-900 border border-gray-200 dark:border-gray-700 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500/50 focus:border-blue-500 transition-colors text-sm"
                    ref="searchInput"
                  />
                  <i class='bx bx-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400'></i>
              </div>
            </div>

            <!-- Additional Filters Slot -->
            <div v-if="$slots.default" class="space-y-4">
               <slot></slot> 
            </div>

            <!-- Actions -->
            <div class="pt-3 border-t border-gray-100 dark:border-gray-700 flex justify-end gap-2">
              <button 
                @click="handleReset"
                class="px-3 py-1.5 text-xs font-medium text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-md transition-colors"
              >
                Xóa bộ lọc
              </button>
              <button 
                @click="toggleSearch"
                class="px-3 py-1.5 text-xs font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-md shadow-sm transition-colors"
              >
                Đóng
              </button>
            </div>
          </div>
        </div>
      </Transition>
    </div>

    <!-- Create Button (Moved Out of Filter) -->
    <button 
      v-if="showCreate"
      @click="$emit('create')"
      class="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg shadow-md hover:shadow-lg transition-all transform active:scale-95 font-medium"
    >
      <i class='bx bx-plus text-xl'></i>
      <span class="hidden sm:block">Thêm mới</span>
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  modelValue: { type: String, default: '' },
  placeholder: { type: String, default: 'Tìm kiếm...' },
  showCreate: { type: Boolean, default: true }
});

const emit = defineEmits(['update:modelValue', 'create', 'reset']);

const isOpen = ref(false);
const searchInput = ref<HTMLInputElement | null>(null);
const popoverRef = ref<HTMLElement | null>(null);
const searchTrigger = ref<HTMLElement | null>(null);


const toggleSearch = () => {
  isOpen.value = !isOpen.value;
  if (isOpen.value) {
    nextTick(() => {
      searchInput.value?.focus();
    });
  }
};

const handleInput = (event: Event) => {
  const value = (event.target as HTMLInputElement).value;
  emit('update:modelValue', value);
};

const handleReset = () => {
  emit('update:modelValue', '');
  emit('reset');
};

// Close when clicking outside
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as Node;

  // Close Search
  if (
    isOpen.value &&
    popoverRef.value &&
    !popoverRef.value.contains(target) &&
    searchTrigger.value &&
    !searchTrigger.value.contains(target)
  ) {
    isOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>
