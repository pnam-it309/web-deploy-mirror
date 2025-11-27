<template>
  <div class="custom-form-item mb-5">
    <label class="block text-[#5a483e] text-sm font-semibold mb-2 ml-1">
      <span v-if="props.required" class="text-red-500 mr-0.5">*</span>
      {{ props.label }}
    </label>
    
    <!-- Wrapper cho Ant Design Form Item -->
    <a-form-item :name="props.name" :rules="props.rules" class="mb-0">
      <slot></slot>
    </a-form-item>
  </div>
</template>

<script setup lang="ts">
// Không cần import defineProps từ 'vue' khi dùng <script setup> (tự động có sẵn)
// import { defineProps } from 'vue' 

const props = defineProps<{
  label: string
  name: string
  rules?: object[]
  required?: boolean
}>()
</script>

<style scoped>
/* Tinh chỉnh style của Ant Design Form Item nếu cần đè */
:deep(.ant-form-item) {
  margin-bottom: 0;
}
</style>