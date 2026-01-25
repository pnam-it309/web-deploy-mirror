<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import apiClient from '@/services/api/api';

import type { ProductDetail } from '@/services/client/client.service';
import BaseSpinner from '@/components/base/BaseSpinner.vue';

const route = useRoute();
const router = useRouter();
const token = route.params.token as string;

const product = ref<ProductDetail | null>(null);
const isLoading = ref(true);
const error = ref('');

onMounted(async () => {
    try {
        const res = await apiClient.get(`/customer/preview/${token}`);
        if (res.data.data) {
            product.value = res.data.data;
        } else {
            error.value = "Không tìm thấy dữ liệu preview";
        }
    } catch (e: any) {
        error.value = e.response?.data?.message || "Link preview không hợp lệ hoặc đã hết hạn";
    } finally {
        isLoading.value = false;
    }
});
</script>

<template>
    <div class="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <!-- Preview Banner -->
        <div class="bg-primary text-white px-4 py-3 text-center sticky top-0 z-50 shadow-md backdrop-blur-md bg-opacity-95">
            <span class="font-bold uppercase tracking-widest text-xs flex items-center justify-center gap-2">
                <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
                Chế độ xem trước (Preview Mode)
            </span>
        </div>

        <div v-if="isLoading" class="flex items-center justify-center h-[80vh]">
            <BaseSpinner size="lg" />
        </div>

        <div v-else-if="error" class="flex flex-col items-center justify-center h-[80vh] text-center px-4">
            <div class="bg-red-100 text-red-600 p-4 rounded-full mb-4">
                <svg class="w-12 h-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
            </div>
            <h2 class="text-2xl font-bold text-gray-800 dark:text-white mb-2">Không thể xem preview</h2>
            <p class="text-gray-600 dark:text-gray-400 mb-6">{{ error }}</p>
            <button @click="router.push('/')" class="px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors">
                Về trang chủ
            </button>
        </div>

        <!-- Render Product Detail but override some behaviors if needed -->
        <!-- Note: We are reusing the ProductDetailPage but checking logic inside it might need adjustment if it relies strictly on ID -->
        <!-- However, since we fetch data here, we can pass it down or store it in a way ProductDetail can use -->
        <!-- For simplicity, since ProductDetailPage fetches by ID from route, we might need a modified approach or reuse components -->
        <!-- BETTER APPROACH: ProductDetailPage usually fetches by ID. Here we have the full data object. -->
        <!-- Refactoring ProductDetailPage to accept 'initialData' prop would be best, but for now let's just use the components that ProductDetailPage uses -->
        
        <div v-else class="pb-20">
             <!-- We can reuse the same layout if we refactor ProductDetailPage, 
                  OR we can just route to a special route where the component handles it. 
                  Since we manually fetched data, let's try to inject it or modify ProductDetailPage to accept props. -->
             <!-- Actually, simplest way is to register a route /preview/:token that specifically loads a component that reuses parts of ProductDetail -->
             
             <!-- Let's try to pass the data to a 'dumb' version of ProductDetail if possible, 
                  but since ProductDetail fetches data itself, we might duplicate some template code or refactor. 
                  
                  Given time constraints, I will duplicate the structure using the same components for a consistent look.
             -->
             
             <!-- Hero Section -->
            <div class="relative bg-[#1A1A1A] text-white overflow-hidden min-h-[60vh] flex items-center">
                <!-- Background Image logic similar to Detail Page -->
                 <div class="absolute inset-0 z-0 opacity-40">
                    <img :src="product?.thumbnail || 'https://placehold.co/1920x1080?text=No+Image'" 
                        class="w-full h-full object-cover blur-sm scale-110" />
                    <div class="absolute inset-0 bg-gradient-to-t from-[#1A1A1A] via-[#1A1A1A]/50 to-transparent"></div>
                </div>

                <div class="container mx-auto px-4 relative z-10 py-12 grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
                    <div>
                         <div v-if="product?.domainName"
                            class="inline-block px-3 py-1 mb-4 text-xs font-bold tracking-wider uppercase bg-yellow-500 text-[#1A1A1A] rounded-full">
                            {{ product.domainName }}
                        </div>
                        <h1 class="text-3xl md:text-5xl lg:text-6xl font-serif font-bold mb-6 leading-tight">
                            {{ product?.name }}
                        </h1>
                        <p class="text-lg md:text-xl text-gray-300 mb-8 max-w-2xl leading-relaxed">
                            {{ product?.shortDescription || product?.description }}
                        </p>
                    </div>
                     <!-- Thumbnail/Gallery -->
                    <div class="relative group">
                         <div class="relative rounded-lg overflow-hidden shadow-2xl border-4 border-white/10 dark:border-gray-700/50 transform group-hover:scale-[1.02] transition-transform duration-500">
                             <img :src="product?.thumbnail || 'https://placehold.co/800x600?text=App+Preview'" 
                                class="w-full h-auto object-cover aspect-video" />
                         </div>
                    </div>
                </div>
            </div>
             
             <!-- Content Sections ... (Simplified for Preview) -->
             <div class="container mx-auto px-4 py-12">
                 <div class="grid grid-cols-1 lg:grid-cols-3 gap-12">
                     <div class="lg:col-span-2 space-y-12">
                         <!-- Description -->
                        <section>
                            <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-3">
                                Chi tiết sản phẩm
                            </h2>
                            <div class="prose prose-lg dark:prose-invert max-w-none text-gray-600 dark:text-gray-300">
                                <div v-html="product?.longDescription || product?.description"></div>
                            </div>
                        </section>
                     </div>
                     
                     <div class="space-y-8">
                         <!-- Tech Stack -->
                         <div class="bg-white dark:bg-gray-800 rounded-2xl p-6 shadow-sm border border-gray-100 dark:border-gray-700">
                            <h3 class="text-lg font-bold text-gray-900 dark:text-white mb-4">Công nghệ sử dụng</h3>
                            <div class="flex flex-wrap gap-2">
                                <div v-for="tech in product?.technologies" :key="tech.name"
                                    class="flex items-center gap-2 px-3 py-2 bg-gray-50 dark:bg-gray-700 rounded-lg border border-gray-100 dark:border-gray-600">
                                    <img v-if="tech.icon" :src="tech.icon" class="w-5 h-5 object-contain" />
                                    <span class="text-sm font-medium text-gray-700 dark:text-gray-200">{{ tech.name }}</span>
                                </div>
                            </div>
                        </div>
                     </div>
                 </div>
             </div>
        </div>
    </div>
</template>
