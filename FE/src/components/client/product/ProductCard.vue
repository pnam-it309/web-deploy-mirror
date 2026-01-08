<template>
    <div @click="navigateToDetail"
        class="cursor-pointer group bg-white rounded-2xl border border-gray-100 overflow-hidden hover:shadow-xl hover:border-gray-200 transition-all duration-300 h-full flex flex-col">
        <!-- Thumbnail -->
        <div class="relative w-full aspect-video bg-gray-100 overflow-hidden">
            <img :src="product.thumbnail || 'https://placehold.co/640x360?text=No+Image'" :alt="product.name"
                loading="lazy"
                class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" />
            <!-- Overlay (Optional) -->
            <div class="absolute inset-0 bg-black/0 group-hover:bg-black/10 transition-colors duration-300"></div>
        </div>

        <!-- Content -->
        <div class="p-5 flex-1 flex flex-col">
            <!-- Domain Tag -->
            <div class="mb-3">
                <span class="inline-block px-3 py-1 text-xs font-semibold text-blue-600 bg-blue-50 rounded-full">
                    {{ product.domainName }}
                </span>
            </div>

            <!-- Title -->
            <h3 class="text-lg font-bold text-gray-900 mb-2 line-clamp-1 group-hover:text-blue-600 transition-colors">
                {{ product.name }}
            </h3>

            <!-- Description -->
            <p class="text-gray-500 text-sm mb-4 line-clamp-2 flex-1">
                {{ product.description }}
            </p>

            <!-- Tech Stack -->
            <div class="flex items-center gap-2 mt-auto pt-4 border-t border-gray-100">
                <div class="flex -space-x-2">
                    <img v-for="(tech, idx) in product.technologies?.slice(0, 3)" :key="idx" :src="tech.icon"
                        :title="tech.name" class="w-8 h-8 rounded-full border-2 border-white bg-gray-50 object-cover" />
                    <div v-if="(product.technologies?.length || 0) > 3"
                        class="w-8 h-8 rounded-full border-2 border-white bg-gray-100 flex items-center justify-center text-xs font-bold text-gray-500">
                        +{{ product.technologies!.length - 3 }}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { ROUTES_CONSTANTS } from '@/constants/path';

export interface Product {
    id: string;
    name: string;
    description: string;
    thumbnail?: string;
    domainName?: string;
    technologies?: { name: string; icon: string }[];
}

const props = defineProps<{
    product: Product
}>();

const router = useRouter();

const navigateToDetail = () => {
    router.push({
        name: ROUTES_CONSTANTS.CUSTOMER.children.APP_DETAIL.name,
        params: { id: props.product.id }
    });
};
</script>