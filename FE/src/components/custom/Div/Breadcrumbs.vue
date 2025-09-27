<script setup lang="ts">
const props = defineProps<{ items: { label: string; to?: string }[] }>()
</script>

<template>
  <nav class="text-sm text-slate-500 dark:text-slate-300 py-2" aria-label="Breadcrumb">
    <ol class="flex items-center gap-1 flex-wrap">
      <li v-for="(it, i) in items" :key="i" class="flex items-center gap-1">
        <template v-if="it.to">
          <router-link :to="it.to" class="hover:text-primary">{{ it.label }}</router-link>
        </template>
        <template v-else>
          <span class="font-medium text-slate-700 dark:text-slate-100">{{ it.label }}</span>
        </template>
        <span v-if="i < items.length - 1" class="px-1">/</span>
      </li>
    </ol>
  </nav>
</template>
