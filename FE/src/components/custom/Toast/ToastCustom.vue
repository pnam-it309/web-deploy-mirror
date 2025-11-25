<template>
  <div
    v-if="isVisible"
    :class="[
      'fixed top-6 right-6 px-4 py-2 rounded shadow-lg text-white',
      type === 'success' ? 'bg-green-600' : 'bg-red-600'
    ]"
  >
    {{ message }}
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps({
  show: { type: Boolean, default: false },
  message: { type: String, default: '' },
  type: { type: String as () => 'success' | 'error', default: 'success' },
  duration: { type: Number, default: 2200 }
})

const isVisible = ref(props.show)
let timer: any = null

watch(
  () => props.show,
  (newVal) => {
    if (newVal) {
      isVisible.value = true
      clearTimeout(timer)
      timer = setTimeout(() => {
        isVisible.value = false
      }, props.duration)
    } else {
      clearTimeout(timer)
      isVisible.value = false
    }
  }
)
</script>