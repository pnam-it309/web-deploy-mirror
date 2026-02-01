<template>
    <div @click="navigateToDetail"
        class="cursor-pointer group bg-white dark:bg-dark-light rounded-xl border border-gray-100 dark:border-gray-700 overflow-hidden hover:shadow-2xl hover:shadow-primary/10 hover:border-primary/50 transition-all duration-500 h-full flex flex-col transform hover:-translate-y-1">
        <!-- Thumbnail -->
        <div class="relative w-full aspect-video bg-gray-100 dark:bg-gray-800 overflow-hidden">
            <img :src="product.thumbnail || 'https://placehold.co/640x360?text=No+Preview'" :alt="product.name"
                loading="lazy"
                class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110" />
            <!-- Overlay -->
            <div class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent opacity-60 group-hover:opacity-80 transition-opacity"></div>
            
            <!-- Like Button with tooltip -->
            <div class="absolute top-3 right-3 z-10 opacity-0 group-hover:opacity-100 transition-all duration-300 translate-y-[-10px] group-hover:translate-y-0">
                <LikeButton :product-id="product.id" class="!backdrop-blur shadow-sm" />
            </div>

            <!-- Floating Domain Tag -->
            <div class="absolute bottom-3 left-3">
                <span class="inline-block px-3 py-1 text-[10px] font-bold uppercase tracking-wider text-white bg-primary/90 backdrop-blur-md rounded-full shadow-lg">
                    {{ product.domainName }}
                </span>
            </div>
        </div>

        <!-- Content -->
        <div class="p-5 flex-1 flex flex-col relative">
            <!-- Title -->
            <h3
                class="text-lg font-bold text-gray-900 dark:text-white mb-2 line-clamp-1 group-hover:text-primary transition-colors font-serif">
                {{ product.name }}
            </h3>

            <!-- Description -->
            <p class="text-gray-500 dark:text-gray-400 text-sm mb-4 line-clamp-2 flex-1">
                {{ product.description }}
            </p>

            <!-- Tech Stack -->
            <div class="flex items-center gap-2 mt-auto pt-4 border-t border-gray-100">
                <div class="flex -space-x-2">
                    <img v-for="(tech, idx) in safeTechnologies.slice(0, 3)" :key="idx" :src="tech.icon"
                        :title="tech.name" class="w-8 h-8 rounded-full border-2 border-white dark:border-gray-800 bg-gray-50 dark:bg-gray-700 object-cover transition-colors" />
                    <div v-if="technologiesCount > 3"
                        class="w-8 h-8 rounded-full border-2 border-white dark:border-gray-800 bg-gray-100 dark:bg-gray-700 flex items-center justify-center text-xs font-bold text-gray-500 dark:text-gray-400">
                        +{{ technologiesCount - 3 }}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { computed } from 'vue';
import { ROUTES_CONSTANTS } from '@/constants/path';
import LikeButton from '@/components/common/LikeButton.vue';
import { encodeId } from '@/utils';

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

const safeTechnologies = computed(() => {
    return Array.isArray(props.product.technologies) ? props.product.technologies : [];
});

const technologiesCount = computed(() => safeTechnologies.value.length);

const router = useRouter();

const navigateToDetail = () => {
    router.push({
        name: ROUTES_CONSTANTS.CUSTOMER.children.APP_DETAIL.name,
        params: { id: encodeId(props.product.id) }
    });
};
</script>