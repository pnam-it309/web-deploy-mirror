<template>
  <transition
    enter-active-class="ease-out duration-300"
    enter-from-class="opacity-0"
    enter-to-class="opacity-100"
    leave-active-class="ease-in duration-200"
    leave-from-class="opacity-100"
    leave-to-class="opacity-0"
  >
    <div
      v-if="show"
      class="fixed inset-0 z-50 flex items-center justify-center overflow-y-auto overflow-x-hidden p-4 sm:p-6"
    >
      <!-- Backdrop (Nền tối mờ) -->
      <div 
        class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm transition-opacity" 
        @click="$emit('close')"
      ></div>

      <!-- Modal Panel -->
      <transition
        enter-active-class="ease-out duration-300"
        enter-from-class="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
        enter-to-class="opacity-100 translate-y-0 sm:scale-100"
        leave-active-class="ease-in duration-200"
        leave-from-class="opacity-100 translate-y-0 sm:scale-100"
        leave-to-class="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
      >
        <div 
          :class="[
            'relative w-full bg-white rounded-xl shadow-2xl transform transition-all flex flex-col max-h-[90vh]',
            sizeClass
          ]"
        >
          <!-- Header -->
          <div class="flex justify-between items-center px-6 py-4 border-b border-gray-100">
            <h3 class="text-lg font-bold text-[#6c584c] tracking-wide">
              <slot name="title">Tiêu đề Modal</slot>
            </h3>
            <button 
              @click="$emit('close')" 
              class="text-gray-400 hover:text-red-500 transition-colors p-1 rounded-md hover:bg-red-50 focus:outline-none focus:ring-2 focus:ring-red-500/50"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <!-- Body (Scrollable) -->
          <div class="px-6 py-4 overflow-y-auto custom-scrollbar">
            <slot />
          </div>

          <!-- Footer -->
          <div class="flex justify-end space-x-3 px-6 py-4 border-t border-gray-100 bg-gray-50/50 rounded-b-xl">
            <slot name="footer" />
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  size: {
    type: String,
    default: 'md', // sm, md, lg, xl, 2xl, full
  }
})

defineEmits(['close'])

const sizeClass = computed(() => {
  switch (props.size) {
    case 'sm': return 'max-w-sm'
    case 'md': return 'max-w-lg' // Default (~512px)
    case 'lg': return 'max-w-2xl' // (~672px)
    case 'xl': return 'max-w-4xl' // (~896px)
    case '2xl': return 'max-w-6xl'
    case 'full': return 'max-w-full mx-4'
    default: return 'max-w-lg'
  }
})
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent; 
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #d1d5db; 
  border-radius: 3px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #9ca3af; 
}
</style>