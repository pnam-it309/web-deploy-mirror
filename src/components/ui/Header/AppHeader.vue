<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUIStore } from '../../../stores/ui'
import { MOCK_PRODUCTS } from '../../../constants/mock'

const router = useRouter()
const ui = useUIStore()
const q = ref('')

function submitSearch() {
  router.push({ name: 'catalog', query: { q: q.value } })
}

const suggestions = computed(() => {
  const keyword = q.value.trim().toLowerCase()
  if (!keyword) return []
  return MOCK_PRODUCTS.filter((p) => (p.name + ' ' + p.sku).toLowerCase().includes(keyword)).slice(
    0,
    5,
  )
})
</script>

<template>
  <header
    class="sticky top-0 z-20 bg-white/80 dark:bg-slate-900/80 backdrop-blur border-b border-slate-200 dark:border-slate-800"
  >
    <div class="mx-auto max-w-7xl px-3 sm:px-4 lg:px-6 py-3 flex items-center gap-3">
      <div class="flex items-center gap-2 cursor-pointer" @click="router.push('/')">
        <span
          class="w-8 h-8 rounded bg-primary inline-flex items-center justify-center text-white font-bold"
          >B2B</span
        >
        <span class="font-semibold">UDPM Distribution</span>
      </div>
      <div class="relative flex-1 hidden md:block">
        <form @submit.prevent="submitSearch">
          <input
            v-model="q"
            type="search"
            class="w-full rounded-md border border-slate-300 dark:border-slate-700 bg-grayLight dark:bg-slate-800 px-4 py-2 outline-none"
            placeholder="Tìm theo tên, SKU, mô tả..."
          />
        </form>
        <div
          v-if="suggestions.length"
          class="absolute mt-1 w-full bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-700 rounded shadow"
        >
          <ul>
            <li v-for="s in suggestions" :key="s.sku">
              <button
                class="w-full text-left px-3 py-2 hover:bg-gray-50 dark:hover:bg-slate-800 flex items-center gap-2"
                @click="router.push({ name: 'product-detail', params: { sku: s.sku } })"
              >
                <img :src="s.thumbnail" class="w-8 h-6 object-cover rounded" loading="lazy" />
                <div class="flex-1">
                  <div class="text-sm font-medium">{{ s.name }}</div>
                  <div class="text-xs text-slate-500">SKU: {{ s.sku }}</div>
                </div>
              </button>
            </li>
          </ul>
        </div>
      </div>
      <div class="flex items-center gap-2">
        <button
          class="btn-secondary hidden sm:inline-flex"
          title="Upload Excel"
          @click="router.push('/admin/upload')"
        >
          📤
        </button>
        <button class="btn-secondary hidden sm:inline-flex" title="User">👤</button>
        <button class="btn-primary" @click="ui.openQuote()">Yêu cầu báo giá</button>
      </div>
    </div>
    <div class="md:hidden px-3 pb-3">
      <form @submit.prevent="submitSearch">
        <input
          v-model="q"
          type="search"
          class="w-full rounded-md border border-slate-300 dark:border-slate-700 bg-grayLight dark:bg-slate-800 px-4 py-2 outline-none"
          placeholder="Tìm theo tên, SKU, mô tả..."
        />
      </form>
    </div>
  </header>
</template>
