<!-- đếch dùng đến bg, nguyên cái file filter -->
<script setup lang="ts">
import { ref, watch } from 'vue'

const emit = defineEmits<{
  (e: 'update:filters', filters: Record<string, string>): void
}>()

const category = ref('')
const brand = ref('')
const availability = ref('')
const unit = ref('')
const warehouse = ref('')

watch([category, brand, availability, unit, warehouse], () => {
  emit('update:filters', {
    category: category.value,
    brand: brand.value,
    availability: availability.value,
    unit: unit.value,
    warehouse: warehouse.value
  })
})
</script>

<template>
  <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-2">
    <select v-model="category" class="border rounded-md px-3 py-2 bg-white dark:bg-slate-800 border-slate-300 dark:border-slate-700">
      <option value="">Danh mục</option>
      <option value="xi-mang">Xi măng</option>
      <option value="gach">Gạch</option>
      <option value="thep">Thép</option>
    </select>
    <select v-model="brand" class="border rounded-md px-3 py-2 bg-white dark:bg-slate-800 border-slate-300 dark:border-slate-700">
      <option value="">Thương hiệu</option>
      <option value="pc40">PC40</option>
      <option value="block">Block</option>
    </select>
    <select v-model="availability" class="border rounded-md px-3 py-2 bg-white dark:bg-slate-800 border-slate-300 dark:border-slate-700">
      <option value="">Tồn kho</option>
      <option value="in">Còn hàng</option>
      <option value="out">Hết hàng</option>
    </select>
    <select v-model="unit" class="border rounded-md px-3 py-2 bg-white dark:bg-slate-800 border-slate-300 dark:border-slate-700">
      <option value="">Đơn vị</option>
      <option value="xe">Xe</option>
      <option value="kien">Kiện</option>
      <option value="vien">Viên</option>
      <option value="cay">Cây</option>
    </select>
    <select v-model="warehouse" class="border rounded-md px-3 py-2 bg-white dark:bg-slate-800 border-slate-300 dark:border-slate-700">
      <option value="">Kho</option>
      <option value="kho-a">Kho A</option>
      <option value="kho-b">Kho B</option>
    </select>
  </div>
</template>
