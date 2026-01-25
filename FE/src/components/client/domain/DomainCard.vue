<template>
    <div class="group relative flex bg-white dark:bg-dark-light border border-gray-100 dark:border-gray-700 rounded-xl hover:border-primary/50 hover:shadow-xl hover:shadow-primary/10 transition-all duration-300 cursor-pointer h-full overflow-hidden"
        :class="layout === 'list' ? 'flex-row items-center p-5 gap-6' : 'flex-col items-center justify-center p-8'">
        
        <!-- Decoration Background -->
        <div class="absolute inset-0 bg-gradient-to-br from-primary/5 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>

        <!-- Icon Container -->
        <div class="relative z-10 rounded-2xl bg-gray-50 dark:bg-gray-800 flex items-center justify-center group-hover:bg-primary group-hover:scale-110 group-hover:-rotate-3 transition-all duration-300 shadow-sm"
            :class="layout === 'list' ? 'w-14 h-14 flex-shrink-0' : 'w-20 h-20 mb-6'">
            <img v-if="domain.icon && domain.icon.startsWith('http')" :src="domain.icon" :alt="domain.name"
                class="object-contain filter transition-all duration-300 group-hover:brightness-0 group-hover:invert"
                :class="layout === 'list' ? 'w-7 h-7' : 'w-10 h-10'" />
            <span v-else class="text-3xl text-gray-400 group-hover:text-white transition-colors duration-300">
                <FolderIcon :class="layout === 'list' ? 'w-7 h-7' : 'w-10 h-10'" />
            </span>
        </div>

        <!-- Content -->
        <div class="relative z-10" :class="{ 'flex-1 text-left': layout === 'list', 'text-center': layout !== 'list' }">
            <!-- Name -->
            <h3 class="text-lg font-bold text-gray-900 dark:text-white group-hover:text-primary transition-colors font-sans tracking-tight">
                {{ domain.name }}
            </h3>

            <!-- Product Count -->
            <span class="mt-2 text-sm font-medium text-gray-500 dark:text-gray-400 group-hover:text-primary/80 block">
                {{ domain.productCount || 0 }} products
            </span>
        </div>
    </div>

</template>

<script setup lang="ts">
import { FolderIcon } from '@heroicons/vue/24/outline';

withDefaults(defineProps<{
    domain: {
        id: string;
        name: string;
        icon?: string;
        productCount?: number;
    };
    layout?: 'grid' | 'list';
}>(), {
    layout: 'grid'
});
</script>