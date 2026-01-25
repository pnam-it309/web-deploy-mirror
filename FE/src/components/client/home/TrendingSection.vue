<template>
    <section class="py-16 bg-gradient-to-br from-orange-50 to-amber-50 dark:from-gray-900 dark:to-gray-800">
        <div class="container mx-auto px-4">
            <div class="flex items-center justify-between mb-8">
                <div>
                    <h2 class="text-3xl font-bold text-gray-900 dark:text-white flex items-center gap-3">
                        <svg class="w-8 h-8 text-orange-500" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd"
                                d="M12.395 2.553a1 1 0 00-1.45-.385c-.345.23-.614.558-.822.88-.214.33-.403.713-.57 1.116-.334.804-.614 1.768-.84 2.734a31.365 31.365 0 00-.613 3.58 2.64 2.64 0 01-.945-1.067c-.328-.68-.398-1.534-.398-2.654A1 1 0 005.05 6.05 6.981 6.981 0 003 11a7 7 0 1011.95-4.95c-.592-.591-.98-.985-1.348-1.467-.363-.476-.724-1.063-1.207-2.03zM12.12 15.12A3 3 0 017 13s.879.5 2.5.5c0-1 .5-4 1.25-4.5.5 1 .786 1.293 1.371 1.879A2.99 2.99 0 0113 13a2.99 2.99 0 01-.879 2.121z"
                                clip-rule="evenodd" />
                        </svg>
                        Đang thịnh hành
                    </h2>
                    <p class="text-gray-600 dark:text-gray-400 mt-2">Sản phẩm được xem nhiều nhất {{ periodLabel }}</p>
                </div>

                <!-- Period Toggle -->
                <div class="flex bg-white dark:bg-gray-800 rounded-lg p-1 border border-gray-200 dark:border-gray-700">
                    <button @click="period = 'week'" class="px-4 py-2 text-sm font-medium rounded-md transition-all"
                        :class="period === 'week' ? 'bg-orange-500 text-white' : 'text-gray-600 dark:text-gray-400 hover:text-gray-900'">
                        Tuần
                    </button>
                    <button @click="period = 'month'" class="px-4 py-2 text-sm font-medium rounded-md transition-all"
                        :class="period === 'month' ? 'bg-orange-500 text-white' : 'text-gray-600 dark:text-gray-400 hover:text-gray-900'">
                        Tháng
                    </button>
                </div>
            </div>

            <!-- Loading -->
            <div v-if="isLoading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <div v-for="i in 4" :key="i" class="animate-pulse">
                    <div class="bg-gray-200 dark:bg-gray-700 rounded-xl h-48"></div>
                    <div class="mt-3 h-4 bg-gray-200 dark:bg-gray-700 rounded w-3/4"></div>
                    <div class="mt-2 h-3 bg-gray-200 dark:bg-gray-700 rounded w-1/2"></div>
                </div>
            </div>

            <!-- Trending Products -->
            <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <div v-for="(product, index) in trendingProducts" :key="product.id"
                    @click="navigateToProduct(product.id)"
                    class="group cursor-pointer bg-white dark:bg-gray-800 rounded-xl overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 border border-gray-100 dark:border-gray-700">
                    <!-- Ranking Badge -->
                    <div class="relative">
                        <div class="absolute top-3 left-3 z-10 w-8 h-8 rounded-full flex items-center justify-center font-bold text-sm"
                            :class="getRankingClass(index)">
                            {{ index + 1 }}
                        </div>

                        <!-- Thumbnail -->
                        <div class="aspect-video overflow-hidden">
                            <img :src="product.thumbnail || 'https://placehold.co/640x360?text=No+Image'"
                                :alt="product.name"
                                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" />
                        </div>
                    </div>

                    <!-- Content -->
                    <div class="p-4">
                        <span
                            class="inline-block px-2 py-1 text-xs font-semibold text-orange-600 dark:text-orange-400 bg-orange-50 dark:bg-orange-900/30 rounded-full mb-2">
                            {{ product.domainName }}
                        </span>
                        <h3
                            class="font-bold text-gray-900 dark:text-white group-hover:text-orange-600 dark:group-hover:text-orange-400 transition-colors line-clamp-1">
                            {{ product.name }}
                        </h3>
                        <div class="flex items-center gap-2 mt-2 text-sm text-gray-500 dark:text-gray-400">
                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                            </svg>
                            {{ formatViews(product.viewCount) }} lượt xem
                        </div>
                    </div>
                </div>
            </div>

            <!-- Empty State -->
            <div v-if="!isLoading && trendingProducts.length === 0" class="text-center py-12">
                <p class="text-gray-500 dark:text-gray-400">Chưa có dữ liệu trending</p>
            </div>
        </div>
    </section>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ROUTES_CONSTANTS } from '@/constants/path'
import { getPublicFeaturedProducts } from '@/services/client/client.service'

interface TrendingProduct {
    id: string
    name: string
    thumbnail?: string
    domainName?: string
    viewCount: number
}

const router = useRouter()
const period = ref<'week' | 'month'>('week')
const isLoading = ref(true)
const trendingProducts = ref<TrendingProduct[]>([])

const periodLabel = computed(() => period.value === 'week' ? 'tuần này' : 'tháng này')

const fetchTrending = async () => {
    isLoading.value = true
    try {
        // Using featured products API for now, can be replaced with dedicated trending API
        const products = await getPublicFeaturedProducts()
        // Sort by viewCount and take top 8
        trendingProducts.value = products
            .sort((a: any, b: any) => (b.viewCount || 0) - (a.viewCount || 0))
            .slice(0, 8)
    } catch (error) {
        console.error('Failed to fetch trending:', error)
    } finally {
        isLoading.value = false
    }
}

const getRankingClass = (index: number) => {
    switch (index) {
        case 0: return 'bg-yellow-500 text-white'
        case 1: return 'bg-gray-400 text-white'
        case 2: return 'bg-amber-700 text-white'
        default: return 'bg-gray-200 dark:bg-gray-600 text-gray-700 dark:text-gray-300'
    }
}

const formatViews = (count: number) => {
    if (!count) return '0'
    if (count >= 1000) return (count / 1000).toFixed(1) + 'k'
    return count.toString()
}

const navigateToProduct = (id: string) => {
    router.push({
        name: ROUTES_CONSTANTS.CUSTOMER.children.APP_DETAIL.name,
        params: { id }
    })
}

watch(period, () => {
    fetchTrending()
})

onMounted(() => {
    fetchTrending()
})
</script>
