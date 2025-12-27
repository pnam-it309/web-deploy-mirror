<template>
    <div
        class="group relative flex flex-col items-center justify-center p-6 bg-white border border-gray-100 rounded-2xl hover:border-[#1e293b] hover:shadow-lg transition-all duration-300 cursor-pointer h-full">
        <!-- Icon Container -->
        <div
            class="w-16 h-16 mb-4 rounded-full bg-gray-50 flex items-center justify-center group-hover:bg-[#1e293b] transition-colors duration-300">
            <img v-if="domain.icon && domain.icon.startsWith('http')" :src="domain.icon" :alt="domain.name"
                class="w-8 h-8 object-contain filter group-hover:brightness-0 group-hover:invert transition-all duration-300" />
            <span v-else class="text-2xl text-gray-400 group-hover:text-white transition-colors duration-300">
                <!-- Fallback icon or text if no image -->
                <FolderIcon class="w-8 h-8" />
            </span>
        </div>

        <!-- Name -->
        <h3 class="text-lg font-semibold text-gray-800 group-hover:text-[#1e293b] text-center transition-colors">
            {{ domain.name }}
        </h3>

        <!-- Product Count (Optional) -->
        <span class="mt-2 text-sm text-gray-500 group-hover:text-gray-600">
            {{ domain.productCount || 0 }} sản phẩm
        </span>
    </div>
</template>

<script setup lang="ts">
import { FolderIcon } from '@heroicons/vue/24/outline'; // Assuming you have this or need to import it

defineProps<{
    domain: {
        id: string;
        name: string;
        icon?: string;
        productCount?: number;
    }
}>();
</script>
