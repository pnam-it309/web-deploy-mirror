<template>
  <button
    type="button"
    :class="[
      'inline-flex items-center justify-center rounded-md font-medium transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed shadow-sm active:scale-95',
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
    // Các giá trị hỗ trợ:
    // - Cơ bản: primary, secondary, danger, info, success, warning, gray, dark
    // - Gradient: gradient-blue, gradient-red, gradient-green, gradient-purple, gradient-orange
    // - Soft: soft-blue, soft-red, soft-green, soft-yellow, soft-gray
    default: 'primary', 
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

const handleClick = (event: MouseEvent) => {
  if (props.loading || props.disabled) {
    event.preventDefault()
    event.stopPropagation()
    return
  }
  emit('click', event) 
}

const colorClasses = computed(() => {
  switch (props.color) {
    // --- SOLID COLORS (Màu bệt) ---
    case 'primary':
      return 'bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500 border border-transparent'
    case 'secondary': // Màu trắng viền xám
      return 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50 focus:ring-gray-500'
    case 'danger':
      return 'bg-red-600 text-white hover:bg-red-700 focus:ring-red-500 border border-transparent'
    case 'info':
      return 'bg-cyan-500 text-white hover:bg-cyan-600 focus:ring-cyan-500 border border-transparent'
    case 'success':
      return 'bg-green-600 text-white hover:bg-green-700 focus:ring-green-500 border border-transparent'
    case 'warning':
      return 'bg-yellow-500 text-white hover:bg-yellow-600 focus:ring-yellow-500 border border-transparent'
    case 'gray':
      return 'bg-gray-600 text-white hover:bg-gray-700 focus:ring-gray-500 border border-transparent'
    case 'dark':
      return 'bg-gray-800 text-white hover:bg-gray-900 focus:ring-gray-500 border border-transparent'

    // --- GRADIENT COLORS (Màu chuyển sắc - Rất đẹp cho nút chính) ---
    case 'gradient-blue':
      return 'bg-gradient-to-r from-blue-500 to-indigo-600 text-white hover:from-blue-600 hover:to-indigo-700 focus:ring-blue-500 border-none'
    case 'gradient-red':
      return 'bg-gradient-to-r from-red-500 to-pink-600 text-white hover:from-red-600 hover:to-pink-700 focus:ring-red-500 border-none'
    case 'gradient-green':
      return 'bg-gradient-to-r from-green-400 to-emerald-600 text-white hover:from-green-500 hover:to-emerald-700 focus:ring-green-500 border-none'
    case 'gradient-purple':
      return 'bg-gradient-to-r from-purple-500 to-pink-500 text-white hover:from-purple-600 hover:to-pink-600 focus:ring-purple-500 border-none'
    case 'gradient-orange':
      return 'bg-gradient-to-r from-orange-400 to-red-500 text-white hover:from-orange-500 hover:to-red-600 focus:ring-orange-500 border-none'

    // --- SOFT/TRANSLUCENT COLORS (Màu nhạt - Dùng cho nút phụ/ít quan trọng) ---
    case 'soft-blue':
      return 'bg-blue-100 text-blue-700 hover:bg-blue-200 focus:ring-blue-500 border-transparent'
    case 'soft-red':
      return 'bg-red-100 text-red-700 hover:bg-red-200 focus:ring-red-500 border-transparent'
    case 'soft-green':
      return 'bg-green-100 text-green-700 hover:bg-green-200 focus:ring-green-500 border-transparent'
    case 'soft-yellow':
      return 'bg-yellow-100 text-yellow-800 hover:bg-yellow-200 focus:ring-yellow-500 border-transparent'
    case 'soft-gray':
      return 'bg-gray-100 text-gray-700 hover:bg-gray-200 focus:ring-gray-500 border-transparent'

    default: // Mặc định là Primary
      return 'bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500 border border-transparent'
  }
})

const sizeClasses = computed(() => {
  switch (props.size) {
    case 'small':
      return 'px-3 py-1.5 text-xs'
    case 'large':
      return 'px-6 py-3 text-base'
    default:
      return 'px-4 py-2 text-sm'
  }
})
</script>