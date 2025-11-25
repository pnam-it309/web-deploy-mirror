<template>
  <button
    type="button"
    :class="[
      'inline-flex items-center justify-center rounded-md font-medium transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed',
      sizeClasses,
      colorClasses
    ]"
    :disabled="disabled || loading"
    @click="handleClick"
  >
    <!-- Loading Spinner -->
    <svg
      v-if="loading"
      class="animate-spin -ml-1 mr-2 h-4 w-4 text-current"
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
    >
      <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
      <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
    </svg>
    
    <!-- Content Slot -->
    <slot />
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  color: {
    type: String,
    default: 'primary', // primary, secondary, danger, info, success
  },
  size: {
    type: String,
    default: 'default', // small, default, large
  },
  loading: {
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['click'])

// --- HÀM QUAN TRỌNG ---
const handleClick = (event: MouseEvent) => {
  if (props.loading || props.disabled) {
    event.preventDefault()
    event.stopPropagation()
    return
  }
  // Truyền 'event' gốc ra ngoài để cha có thể dùng @click.stop
  emit('click', event) 
}
// ----------------------

const colorClasses = computed(() => {
  switch (props.color) {
    case 'primary':
      return 'bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500'
    case 'secondary':
      return 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50 focus:ring-blue-500'
    case 'danger':
      return 'bg-red-600 text-white hover:bg-red-700 focus:ring-red-500'
    case 'info':
      return 'bg-cyan-500 text-white hover:bg-cyan-600 focus:ring-cyan-500'
    case 'success':
      return 'bg-green-600 text-white hover:bg-green-700 focus:ring-green-500'
    default:
      return 'bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500'
  }
})

const sizeClasses = computed(() => {
  switch (props.size) {
    case 'small':
      return 'px-3 py-1.5 text-sm'
    case 'large':
      return 'px-6 py-3 text-lg'
    default:
      return 'px-4 py-2 text-sm'
  }
})
</script>