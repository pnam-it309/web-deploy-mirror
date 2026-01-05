<template>
    <div
        class="group relative flex bg-white border border-gray-100 rounded-2xl hover:border-[#1e293b] hover:shadow-lg transition-all duration-300 cursor-pointer h-full"
        :class="layout === 'list' ? 'flex-row items-center p-4 gap-6' : 'flex-col items-center justify-center p-6'">
        <!-- Icon Container -->
        <div
            class="rounded-full bg-gray-50 flex items-center justify-center group-hover:bg-[#1e293b] transition-colors duration-300"
            :class="layout === 'list' ? 'w-12 h-12 flex-shrink-0' : 'w-16 h-16 mb-4'">
            <img v-if="domain.icon && domain.icon.startsWith('http')" :src="domain.icon" :alt="domain.name"
                class="object-contain filter group-hover:brightness-0 group-hover:invert transition-all duration-300"
                :class="layout === 'list' ? 'w-6 h-6' : 'w-8 h-8'" />
            <span v-else class="text-2xl text-gray-400 group-hover:text-white transition-colors duration-300">
                <FolderIcon :class="layout === 'list' ? 'w-6 h-6' : 'w-8 h-8'" />
            </span>
        </div>

        <!-- Content -->
        <div :class="{ 'flex-1 text-left': layout === 'list', 'text-center': layout !== 'list' }">
            <!-- Name -->
            <h3 class="text-lg font-semibold text-gray-800 group-hover:text-[#1e293b] transition-colors">
                {{ domain.name }}
            </h3>

            <!-- Product Count (Optional) -->
            <span class="mt-1 text-sm text-gray-500 group-hover:text-gray-600 block">
                {{ domain.productCount || 0 }} sản phẩm
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
