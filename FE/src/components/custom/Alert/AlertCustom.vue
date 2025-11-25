<template>
  <div v-if="show" :class="containerClass">
    <div class="flex">
      <div class="flex-shrink-0">
        <CheckCircleFilled v-if="type === 'success'" class="h-5 w-5" />
        <CloseCircleFilled v-if="type === 'error'" class="h-5 w-5" />
        <WarningFilled v-if="type === 'warning'" class="h-5 w-5" />
        <InfoCircleFilled v-if="type === 'info'" class="h-5 w-5" />
      </div>
      <div class="ml-3">
        <h3 v-if="title" class="text-sm font-medium">{{ title }}</h3>
        <div class="text-sm" :class="{ 'mt-2': title }">
          <slot />
        </div>
      </div>
      <div class="ml-auto pl-3">
        <div class="-mx-1.5 -my-1.5">
          <button
            @click="$emit('close')"
            type="button"
            :class="closeButtonClass"
            class="inline-flex rounded-md p-1.5 focus:outline-none focus:ring-2 focus:ring-offset-2"
          >
            <span class="sr-only">Dismiss</span>
            <CloseOutlined class="h-5 w-5" />
          </button>
        </div>
      </div>
    </div>
  </div>
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

const colorClasses = computed(() => {
  switch (props.type) {
    case 'success':
      return {
        bg: 'bg-green-50',
        text: 'text-green-800',
        icon: 'text-green-400',
        button: 'text-green-500 hover:bg-green-100 focus:ring-green-600 focus:ring-offset-green-50'
      }
    case 'error':
      return {
        bg: 'bg-red-50',
        text: 'text-red-800',
        icon: 'text-red-400',
        button: 'text-red-500 hover:bg-red-100 focus:ring-red-600 focus:ring-offset-red-50'
      }
    case 'warning':
      return {
        bg: 'bg-yellow-50',
        text: 'text-yellow-800',
        icon: 'text-yellow-400',
        button: 'text-yellow-500 hover:bg-yellow-100 focus:ring-yellow-600 focus:ring-offset-yellow-50'
      }
    case 'info':
    default:
      return {
        bg: 'bg-blue-50',
        text: 'text-blue-800',
        icon: 'text-blue-400',
        button: 'text-blue-500 hover:bg-blue-100 focus:ring-blue-600 focus:ring-offset-blue-50'
      }
  }
})

const containerClass = computed(
  () => `rounded-md p-4 ${colorClasses.value.bg} ${colorClasses.value.text}`
)
const closeButtonClass = computed(() => colorClasses.value.button)
</script>