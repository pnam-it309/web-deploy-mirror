<script setup lang="ts">
import { ref } from 'vue'

const emit = defineEmits<{
  (e: 'apply', filters: Record<string, any>): void
}>()

const open = ref(false)
const selected = ref<Record<string, any>>({
  category: [] as string[],
  brand: [] as string[],
  availability: [] as string[],
  unit: [] as string[],
  warehouse: [] as string[]
})

function apply() {
  emit('apply', JSON.parse(JSON.stringify(selected.value)))
  open.value = false
}

function openDrawer() { open.value = true }
function closeDrawer() { open.value = false }
// expose methods for parent
defineExpose({ openDrawer, closeDrawer })
</script>

<template>
  <div>
    <!-- Backdrop -->
    <div v-if="open" class="fixed inset-0 z-40 bg-black/40" @click="open=false" />
    <!-- Drawer -->
    <div :class="['fixed z-50 top-0 bottom-0 left-0 w-80 bg-white dark:bg-slate-900 border-r border-slate-200 dark:border-slate-800 transition-transform', open ? 'translate-x-0' : '-translate-x-full']">
      <div class="p-3 border-b border-slate-200 dark:border-slate-800 flex items-center justify-between">
        <h3 class="font-semibold">Bộ lọc</h3>
        <button class="btn-secondary px-2 py-1" @click="open=false">✕</button>
      </div>
      <div class="p-3 space-y-3">
        <div>
          <h4 class="font-medium mb-1">Danh mục</h4>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Xi măng" v-model="selected.category"/> Xi măng</label>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Gạch" v-model="selected.category"/> Gạch</label>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Thép" v-model="selected.category"/> Thép</label>
        </div>
        <div>
          <h4 class="font-medium mb-1">Thương hiệu</h4>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="PC40" v-model="selected.brand"/> PC40</label>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="BLOCK" v-model="selected.brand"/> BLOCK</label>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="VietSteel" v-model="selected.brand"/> VietSteel</label>
        </div>
        <div>
          <h4 class="font-medium mb-1">Tồn kho</h4>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="in_stock" v-model="selected.availability"/> Còn hàng</label>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="contact" v-model="selected.availability"/> Liên hệ</label>
        </div>
        <div>
          <h4 class="font-medium mb-1">Đơn vị</h4>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="xe" v-model="selected.unit"/> Xe</label>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="kien" v-model="selected.unit"/> Kiện</label>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="vien" v-model="selected.unit"/> Viên</label>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="cay" v-model="selected.unit"/> Cây</label>
        </div>
        <div>
          <h4 class="font-medium mb-1">Kho</h4>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Kho A" v-model="selected.warehouse"/> Kho A</label>
          <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Kho B" v-model="selected.warehouse"/> Kho B</label>
        </div>
      </div>
      <div class="p-3 border-t border-slate-200 dark:border-slate-800">
        <button class="btn-primary w-full" @click="apply">Áp dụng</button>
      </div>
    </div>
  </div>
</template>
