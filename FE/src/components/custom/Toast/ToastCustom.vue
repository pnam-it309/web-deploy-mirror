<template>
  <transition
    enter-active-class="transform ease-out duration-300 transition"
    enter-from-class="translate-y-2 opacity-0 sm:translate-y-0 sm:translate-x-2"
    enter-to-class="translate-y-0 opacity-100 sm:translate-x-0"
    leave-active-class="transition ease-in duration-100"
    leave-from-class="opacity-100"
    leave-to-class="opacity-0"
  >
    <div
      v-if="isVisible"
      :class="[
        'fixed top-6 right-6 z-[9999]', // z-index cao nhất để đè lên mọi thứ
        'flex items-center gap-3',
        'px-4 py-3 rounded-xl shadow-lg border backdrop-blur-md',
        'min-w-[300px] max-w-md',
        colorClasses
      ]"
    >
      <!-- Icon -->
      <div class="flex-shrink-0">
        <svg v-if="type === 'success'" xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      </div>

      <!-- Message -->
      <div class="flex-1 text-sm font-medium">
        {{ message }}
      </div>

      <!-- Close Button (Optional) -->
      <button @click="isVisible = false" class="text-current opacity-60 hover:opacity-100 transition-opacity">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'

const props = defineProps({
  show: { type: Boolean, default: false },
  message: { type: String, default: '' },
  type: { type: String as () => 'success' | 'error', default: 'success' },
  duration: { type: Number, default: 3000 }
})

const isVisible = ref(props.show)
let timer: any = null

const colorClasses = computed(() => {
  switch (props.type) {
    case 'success':
      // Nền Sage pha Olive, chữ xanh rêu đậm
      return 'bg-[#dde5b6]/90 border-[#adc178] text-[#386641]'
    case 'error':
      // Nền đỏ pha Mocha nhẹ, chữ đỏ đậm
      return 'bg-red-50/90 border-red-200 text-red-700'
    default:
      return 'bg-white/90 border-gray-200 text-gray-800'
  }
})

watch(
  () => props.show,
  (newVal) => {
    if (newVal) {
      isVisible.value = true
      if (timer) clearTimeout(timer)
      timer = setTimeout(() => {
        isVisible.value = false
      }, props.duration)
    } else {
      if (timer) clearTimeout(timer)
      isVisible.value = false
    }
  }
)
</script>

<style scoped>
/* Không cần thêm CSS vì đã dùng Tailwind */
</style>