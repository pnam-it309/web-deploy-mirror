<script setup lang="ts">
defineProps<{
    title: string;
    breadcrumb?: string;
}>();
</script>

<template>
    <div class="mb-6">
        <div v-if="breadcrumb" class="text-xs text-gray-500 mb-2 font-medium">
            Admin <span class="mx-1 text-gray-300">/</span> {{ breadcrumb }}
        </div>
        <div class="flex items-center justify-between">
            <h1 class="text-2xl font-bold text-gray-800 uppercase tracking-tight">
                {{ title }}
            </h1>
            <slot />
        </div>
    </div>
</template>
