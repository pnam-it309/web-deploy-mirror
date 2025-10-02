<script setup lang="ts">
import { ref } from 'vue'

const emit = defineEmits<{
  (e: 'apply', filters: Record<string, any>): void
}>()

const open = ref<{[key: string]: boolean}>({
  category: true,
  brand: true,
  availability: true,
  unit: true,
  warehouse: false,
  packaging: false,
  tags: false
})

const selected = ref<Record<string, any>>({
  category: [] as string[],
  brand: [] as string[],
  availability: [] as string[],
  unit: [] as string[],
  warehouse: [] as string[],
  packagingMin: '',
  packagingMax: '',
  tags: [] as string[]
})

function toggle(key: string) { open.value[key] = !open.value[key] }
function apply() { emit('apply', JSON.parse(JSON.stringify(selected.value))) }
</script>

<template>
  <aside class="hidden lg:block w-72 shrink-0 pr-4">
    <div class="sticky top-[68px] space-y-3">
      <div class="rounded border border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900" v-for="group in ['category','brand','availability','unit','warehouse','packaging','tags']" :key="group">
        <button class="w-full flex items-center justify-between px-3 py-2 font-medium" @click="toggle(group)">
          <span class="capitalize">{{ group }}</span>
          <span>{{ open[group] ? '−' : '+' }}</span>
        </button>
        <div v-show="open[group]" class="px-3 pb-3 text-sm">
          <template v-if="group==='category'">
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Xi măng" v-model="selected.category"/> Xi măng</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Gạch" v-model="selected.category"/> Gạch</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Thép" v-model="selected.category"/> Thép</label>
          </template>
          <template v-else-if="group==='brand'">
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="PC40" v-model="selected.brand"/> PC40</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="BLOCK" v-model="selected.brand"/> BLOCK</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="VietSteel" v-model="selected.brand"/> VietSteel</label>
          </template>
          <template v-else-if="group==='availability'">
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="in_stock" v-model="selected.availability"/> Còn hàng</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="contact" v-model="selected.availability"/> Liên hệ</label>
          </template>
          <template v-else-if="group==='unit'">
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="xe" v-model="selected.unit"/> Xe</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="kien" v-model="selected.unit"/> Kiện</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="vien" v-model="selected.unit"/> Viên</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="cay" v-model="selected.unit"/> Cây</label>
          </template>
          <template v-else-if="group==='warehouse'">
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Kho A" v-model="selected.warehouse"/> Kho A</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Kho B" v-model="selected.warehouse"/> Kho B</label>
          </template>
          <template v-else-if="group==='packaging'">
            <div class="flex items-center gap-2">
              <input type="number" min="0" class="w-24 rounded border border-slate-300 dark:border-slate-700 px-2 py-1 bg-grayLight dark:bg-slate-800" placeholder="Min" v-model="selected.packagingMin"/>
              <span>—</span>
              <input type="number" min="0" class="w-24 rounded border border-slate-300 dark:border-slate-700 px-2 py-1 bg-grayLight dark:bg-slate-800" placeholder="Max" v-model="selected.packagingMax"/>
            </div>
          </template>
          <template v-else>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Bán theo xe" v-model="selected.tags"/> Bán theo xe</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Có ảnh" v-model="selected.tags"/> Có ảnh</label>
            <label class="flex items-center gap-2 py-1"><input type="checkbox" value="Datasheet" v-model="selected.tags"/> Datasheet</label>
          </template>
        </div>
      </div>
      <button class="btn-primary w-full" @click="apply">Áp dụng bộ lọc</button>
    </div>
  </aside>
</template>
