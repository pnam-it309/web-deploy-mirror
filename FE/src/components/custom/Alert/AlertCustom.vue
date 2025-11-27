<template>
  <transition
    enter-active-class="transform ease-out duration-300 transition"
    enter-from-class="translate-y-2 opacity-0 sm:translate-y-0 sm:translate-x-2"
    enter-to-class="translate-y-0 opacity-100 sm:translate-x-0"
    leave-active-class="transition ease-in duration-100"
    leave-from-class="opacity-100"
    leave-to-class="opacity-0"
  >
    <div v-if="show" :class="[containerClass, 'backdrop-blur-sm border shadow-sm']">
      <div class="flex">
        <div class="flex-shrink-0">
          <component :is="iconComponent" :class="['h-5 w-5', iconClass]" aria-hidden="true" />
        </div>
        <div class="ml-3 w-full">
          <h3 v-if="title" :class="['text-sm font-medium', titleClass]">{{ title }}</h3>
          <div :class="['text-sm', contentClass, { 'mt-2': title }]">
            <slot />
          </div>
        </div>
        <div class="ml-auto pl-3">
          <div class="-mx-1.5 -my-1.5">
            <button
              type="button"
              @click="$emit('close')"
              :class="[
                'inline-flex rounded-md p-1.5 focus:outline-none focus:ring-2 focus:ring-offset-2 transition-colors',
                closeButtonClass
              ]"
            >
              <span class="sr-only">Dismiss</span>
              <CloseOutlined class="h-5 w-5" />
            </button>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import {
  CheckCircleFilled,
  CloseCircleFilled,
  WarningFilled,
  InfoCircleFilled,
  CloseOutlined
} from '@ant-design/icons-vue'

type AlertType = 'success' | 'error' | 'warning' | 'info'

const props = defineProps({
  show: {
    type: Boolean,
    default: true
  },
  type: {
    type: String as () => AlertType,
    default: 'info'
  },
  title: {
    type: String,
    default: ''
  }
})

defineEmits(['close'])

// Map type sang icon component
const iconComponent = computed(() => {
  switch (props.type) {
    case 'success': return CheckCircleFilled
    case 'error': return CloseCircleFilled
    case 'warning': return WarningFilled
    case 'info':
    default: return InfoCircleFilled
  }
})

// Cấu hình màu sắc theo Brand Palette
const colorConfig = computed(() => {
  switch (props.type) {
    case 'success': // Dùng tông Olive/Sage (Xanh lá)
      return {
        container: 'bg-[#dde5b6]/40 border-[#adc178]/30', // Sage Translucent
        icon: 'text-[#6a994e]', // Olive đậm hơn chút
        title: 'text-[#386641]', // Xanh rêu đậm
        content: 'text-[#386641]',
        closeBtn: 'text-[#6a994e] hover:bg-[#adc178]/20 focus:ring-offset-[#dde5b6] focus:ring-[#6a994e]'
      }
    case 'error': // Dùng tông đỏ đất (pha Mocha với đỏ)
      return {
        container: 'bg-red-50/80 border-red-100', // Giữ đỏ nhạt chuẩn UI nhưng làm mờ
        icon: 'text-red-500',
        title: 'text-red-800',
        content: 'text-red-700',
        closeBtn: 'text-red-500 hover:bg-red-100 focus:ring-offset-red-50 focus:ring-red-600'
      }
    case 'warning': // Dùng tông Cream/Coffee (Vàng/Nâu)
      return {
        container: 'bg-[#f0ead2]/60 border-[#e6dfc0]', // Cream Translucent
        icon: 'text-[#d4a373]', // Coffee sữa
        title: 'text-[#9c6644]', // Nâu đất
        content: 'text-[#7f5539]',
        closeBtn: 'text-[#d4a373] hover:bg-[#e6dfc0]/50 focus:ring-offset-[#f0ead2] focus:ring-[#9c6644]'
      }
    case 'info':
    default: // Dùng tông Blue (nhưng pha chút xám cho sang) hoặc giữ nguyên
      return {
        container: 'bg-blue-50/80 border-blue-100',
        icon: 'text-blue-500',
        title: 'text-blue-800',
        content: 'text-blue-700',
        closeBtn: 'text-blue-500 hover:bg-blue-100 focus:ring-offset-blue-50 focus:ring-blue-600'
      }
  }
})

const containerClass = computed(() => `rounded-lg p-4 ${colorConfig.value.container}`)
const iconClass = computed(() => colorConfig.value.icon)
const titleClass = computed(() => colorConfig.value.title)
const contentClass = computed(() => colorConfig.value.content)
const closeButtonClass = computed(() => colorConfig.value.closeBtn)
</script>