<script setup lang="ts">
import { ref, onMounted } from 'vue';
import searchAnalyticsService, {
    type SearchKeywordResponse,
    type SearchTrendResponse,
    type SearchQueryData,
} from '@/services/admin/searchAnalytics.service';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import { toast } from 'vue3-toastify';
import { ChartBarIcon, MagnifyingGlassIcon, ExclamationTriangleIcon } from '@heroicons/vue/24/outline';

import { formatDateTime } from '@/utils';

const topKeywords = ref<SearchKeywordResponse[]>([]);
const noResultQueries = ref<SearchQueryData[]>([]);
const searchTrends = ref<SearchTrendResponse[]>([]);
const isLoading = ref(false);
const selectedPeriod = ref('week');

const periods = [
    { value: 'day', label: 'Last 24 Hours' },
    { value: 'week', label: 'Last 7 Days' },
    { value: 'month', label: 'Last 30 Days' },
];

const loadData = async () => {
    isLoading.value = true;
    try {
        const [keywords, noResults, trends] = await Promise.all([
            searchAnalyticsService.getTopKeywords(selectedPeriod.value, 20),
            searchAnalyticsService.getNoResultQueries(selectedPeriod.value),
            searchAnalyticsService.getSearchTrends(selectedPeriod.value),
        ]);

        topKeywords.value = keywords;
        noResultQueries.value = noResults;
        searchTrends.value = trends;
    } catch (error: any) {
        toast.error('Failed to load analytics data');
        console.error(error);
    } finally {
        isLoading.value = false;
    }
};

onMounted(loadData);

const handlePeriodChange = () => {
    loadData();
};
</script>

<template>
    <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div class="mb-6 shrink-0">
             <div class="flex items-center justify-between mb-2">
                 <div>
                    <span class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Analytics</span>
                    <h1 class="text-3xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight mt-1">Phân tích tìm kiếm</h1>
                 </div>
                 <div class="flex gap-3">
                    <select v-model="selectedPeriod" @change="handlePeriodChange"
                        class="px-4 py-2 border border-blue-200 dark:border-blue-900/50 rounded-lg bg-blue-50 dark:bg-blue-900/20 text-blue-700 dark:text-blue-300 focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all outline-none cursor-pointer font-bold text-sm">
                        <option v-for="period in periods" :key="period.value" :value="period.value">
                            {{ period.label }}
                        </option>
                    </select>
                </div>
            </div>
        </div>

        <div v-if="isLoading" class="flex-1 flex justify-center items-center">
            <BaseSpinner size="lg" />
        </div>

        <div v-else class="flex-1 overflow-auto custom-scrollbar flex flex-col gap-6">
            <!-- Summary Cards -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div class="bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-100 dark:border-gray-700 shadow-sm relative overflow-hidden group">
                    <div class="absolute right-0 top-0 w-32 h-32 bg-blue-500/10 rounded-full blur-3xl -mr-16 -mt-16 transition-all group-hover:bg-blue-500/20"></div>
                     <div class="flex items-center justify-between relative z-10">
                        <div>
                             <p class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1">Total Searches</p>
                            <p class="text-3xl font-bold text-gray-900 dark:text-white">{{searchTrends.reduce((sum, t) => sum + t.searchCount, 0)}}</p>
                        </div>
                        <div class="p-3 bg-blue-50 dark:bg-blue-900/30 rounded-lg text-blue-600 dark:text-blue-400">
                            <MagnifyingGlassIcon class="w-8 h-8" />
                        </div>
                    </div>
                </div>

                <div class="bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-100 dark:border-gray-700 shadow-sm relative overflow-hidden group">
                    <div class="absolute right-0 top-0 w-32 h-32 bg-green-500/10 rounded-full blur-3xl -mr-16 -mt-16 transition-all group-hover:bg-green-500/20"></div>
                    <div class="flex items-center justify-between relative z-10">
                        <div>
                            <p class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1">Unique Keywords</p>
                            <p class="text-3xl font-bold text-gray-900 dark:text-white">{{ topKeywords.length }}</p>
                        </div>
                        <div class="p-3 bg-green-50 dark:bg-green-900/30 rounded-lg text-green-600 dark:text-green-400">
                             <ChartBarIcon class="w-8 h-8" />
                        </div>
                    </div>
                </div>

                <div class="bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-100 dark:border-gray-700 shadow-sm relative overflow-hidden group">
                    <div class="absolute right-0 top-0 w-32 h-32 bg-red-500/10 rounded-full blur-3xl -mr-16 -mt-16 transition-all group-hover:bg-red-500/20"></div>
                    <div class="flex items-center justify-between relative z-10">
                        <div>
                             <p class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1">No Results</p>
                            <p class="text-3xl font-bold text-gray-900 dark:text-white">{{ noResultQueries.length }}</p>
                        </div>
                        <div class="p-3 bg-red-50 dark:bg-red-900/30 rounded-lg text-red-600 dark:text-red-400">
                            <ExclamationTriangleIcon class="w-8 h-8" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <!-- Top Keywords -->
                <div class="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm p-6 flex flex-col h-[500px]">
                    <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                        <span class="w-1.5 h-6 bg-blue-500 rounded-full"></span>
                        Top Search Keywords
                    </h2>
                    
                    <div v-if="topKeywords.length === 0" class="flex-1 flex flex-col items-center justify-center text-center p-8">
                        <div class="w-16 h-16 bg-gray-50 dark:bg-gray-700 rounded-full flex items-center justify-center mb-4">
                            <MagnifyingGlassIcon class="w-8 h-8 text-gray-400" />
                        </div>
                        <p class="text-gray-500 dark:text-gray-400 font-medium">No search data available</p>
                    </div>
                    
                    <div v-else class="flex-1 overflow-y-auto custom-scrollbar space-y-3 pr-2">
                        <div v-for="(keyword, index) in topKeywords" :key="index"
                            class="flex items-center justify-between p-3 rounded-lg border border-gray-100 dark:border-gray-700 hover:border-blue-300 dark:hover:border-blue-700 hover:bg-blue-50 dark:hover:bg-blue-900/20 transition-all group">
                            <div class="flex items-center gap-4">
                                <span class="w-6 h-6 flex items-center justify-center rounded bg-gray-100 dark:bg-gray-700 text-xs font-bold text-gray-500 dark:text-gray-300 group-hover:bg-blue-200 dark:group-hover:bg-blue-800 group-hover:text-blue-700 dark:group-hover:text-blue-200 transition-colors">{{ index + 1 }}</span>
                                <span class="font-bold text-gray-700 dark:text-gray-200 group-hover:text-blue-700 dark:group-hover:text-blue-300 transition-colors">{{ keyword.keyword }}</span>
                            </div>
                            <div class="flex items-center gap-3">
                                <div class="bg-gray-100 dark:bg-gray-700 group-hover:bg-white dark:group-hover:bg-gray-800 px-3 py-1 rounded-full border border-gray-200 dark:border-gray-600 transition-colors">
                                    <span class="text-xs font-bold text-gray-600 dark:text-gray-300 group-hover:text-blue-600 dark:group-hover:text-blue-400">{{ keyword.searchCount }} searches</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Search Trends -->
                <div class="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm p-6 flex flex-col h-[500px]">
                    <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                        <span class="w-1.5 h-6 bg-green-500 rounded-full"></span>
                        Search Trends
                    </h2>
                    
                    <div v-if="searchTrends.length === 0" class="flex-1 flex flex-col items-center justify-center text-center p-8">
                         <div class="w-16 h-16 bg-gray-50 dark:bg-gray-700 rounded-full flex items-center justify-center mb-4">
                            <ChartBarIcon class="w-8 h-8 text-gray-400" />
                        </div>
                        <p class="text-gray-500 dark:text-gray-400 font-medium">No trend data available</p>
                    </div>
                    
                    <div v-else class="flex-1 overflow-y-auto custom-scrollbar space-y-4 pr-2">
                        <div v-for="trend in searchTrends" :key="trend.date" class="group">
                             <div class="flex justify-between items-end mb-1">
                                <span class="text-xs font-bold text-gray-500 dark:text-gray-400">{{ trend.date }}</span>
                                <span class="text-sm font-bold text-gray-900 dark:text-white">{{ trend.searchCount }}</span>
                            </div>
                            <div class="w-full bg-gray-100 dark:bg-gray-700 rounded-full h-2.5 overflow-hidden">
                                <div class="bg-green-500 h-2.5 rounded-full transition-all duration-500 group-hover:bg-green-400 relative"
                                    :style="{ width: `${(trend.searchCount / Math.max(...searchTrends.map(t => t.searchCount))) * 100}%` }">
                                     <div class="absolute inset-0 bg-white/20 animate-pulse"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- No Result Queries -->
                <div class="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm p-6 lg:col-span-2">
                    <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                        <span class="w-1.5 h-6 bg-red-500 rounded-full"></span>
                        Searches with No Results (Content Gaps)
                    </h2>
                    
                    <div v-if="noResultQueries.length === 0" class="text-center py-12 bg-gray-50 dark:bg-gray-900/50 rounded-lg border border-dashed border-gray-200 dark:border-gray-700">
                        <div class="w-16 h-16 bg-green-100 dark:bg-green-900/30 text-green-600 dark:text-green-400 rounded-full flex items-center justify-center mx-auto mb-4">
                            <svg class="w-8 h-8" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" /></svg>
                        </div>
                        <p class="text-gray-900 dark:text-white font-bold">Great job!</p>
                        <p class="text-gray-500 dark:text-gray-400 text-sm mt-1">No queries without results found in this period.</p>
                    </div>
                    
                    <div v-else class="overflow-x-auto custom-scrollbar">
                        <table class="w-full border-collapse">
                            <thead>
                                <tr class="border-b border-gray-100 dark:border-gray-700">
                                    <th class="px-4 py-3 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Query</th>
                                    <th class="px-4 py-3 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Category</th>
                                    <th class="px-4 py-3 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Time</th>
                                </tr>
                            </thead>
                            <tbody class="divide-y divide-gray-50 dark:divide-gray-700/50">
                                <tr v-for="query in noResultQueries.slice(0, 10)" :key="query.id"
                                    class="hover:bg-red-50 dark:hover:bg-red-900/10 transition-colors">
                                    <td class="px-4 py-3">
                                        <span class="font-bold text-red-600 dark:text-red-400 flex items-center gap-2">
                                            <ExclamationTriangleIcon class="w-4 h-4" />
                                            {{ query.queryText }}
                                        </span>
                                    </td>
                                    <td class="px-4 py-3">
                                        <span
                                            class="inline-flex items-center px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wide bg-gray-100 dark:bg-gray-700 text-gray-600 dark:text-gray-300 border border-gray-200 dark:border-gray-600">
                                            {{ query.category }}
                                        </span>
                                    </td>
                                    <td class="px-4 py-3 text-xs font-mono text-gray-500 dark:text-gray-400">{{ formatDateTime(query.createdAt) }}</td>
                                </tr>
                            </tbody>
                        </table>
                        <div v-if="noResultQueries.length > 10" class="mt-4 text-center">
                            <button class="text-xs font-bold text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 uppercase tracking-wide">
                                Showing 10 of {{ noResultQueries.length }} queries
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
    width: 6px;
    height: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
    background: #f1f1f1;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 3px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
    background: #555;
}
</style>
