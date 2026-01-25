<script setup lang="ts">
import { ref, onMounted } from 'vue';
import draggable from 'vuedraggable';
import { AppService } from '@/services/admin/app.service';
import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import { toast } from 'vue3-toastify';

const featuredApps = ref<any[]>([]);
const otherApps = ref<any[]>([]);
const isLoading = ref(false);

const loadData = async () => {
    isLoading.value = true;
    try {
        // Fetch approved apps mostly? Or all? Usually only Approved apps should be featured.
        // But let's fetch all for now, maybe filter by status locally if needed.
        const res = await AppService.getAll({ size: 1000 });
        const all = res.data || [];

        // Filter only approved for homepage?
        // Let's assume we can feature any app, but typically Approved.

        featuredApps.value = all.filter((a: any) => a.isFeatured).sort((a: any, b: any) => (a.homepageSortOrder || 0) - (b.homepageSortOrder || 0));
        otherApps.value = all.filter((a: any) => !a.isFeatured);
    } catch (e) {
        console.error(e);
    } finally {
        isLoading.value = false;
    }
};

onMounted(loadData);

const onDragEnd = async () => {
    try {
        const payload = featuredApps.value.map((app, index) => ({
            id: app.id,
            homepageSortOrder: index + 1
        }));
        await AppService.updateHomepageOrder(payload);
        toast.success("Đã cập nhật thứ tự hiển thị!");
    } catch (e) {
        toast.error("Lỗi cập nhật thứ tự");
    }
};

const toggleFeatured = async (app: any) => {
    try {
        await AppService.toggleFeatured(app.id);
        const isNowFeatured = !app.isFeatured;
        toast.success(isNowFeatured ? "Đã thêm vào Hot!" : "Đã bỏ khỏi Hot!");
        loadData();
    } catch (e) {
        toast.error("Lỗi cập nhật trạng thái");
    }
};
</script>

<template>
    <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div class="mb-6 shrink-0 flex justify-between items-start">
            <div>
                <BaseBreadcrumb :items="[{ label: 'Admin', to: '/admin' }, { label: 'Cấu hình Trang chủ' }]" />
                <h1 class="text-3xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight">Cấu hình Sản phẩm nổi bật</h1>
                <p class="text-gray-500 dark:text-gray-400 text-sm mt-1">Kéo thả để sắp xếp thứ tự hiển thị trên trang chủ</p>
            </div>
             <button @click="loadData"
                class="px-4 py-2 bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-200 border border-gray-200 dark:border-gray-700 font-bold rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors shadow-sm text-sm uppercase tracking-wide flex items-center gap-2">
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" /></svg>
                Làm mới
            </button>
        </div>

        <div v-if="isLoading" class="flex-1 flex justify-center items-center">
            <BaseSpinner size="lg" />
        </div>

        <div v-else class="grid grid-cols-1 lg:grid-cols-2 gap-8 flex-1 overflow-hidden">
            <!-- FEATURED LIST -->
            <div class="flex flex-col min-h-0 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-xl shadow-sm overflow-hidden h-full">
                <div class="p-4 border-b border-gray-100 dark:border-gray-700 bg-orange-50 dark:bg-orange-900/10 flex justify-between items-center shrink-0">
                    <h3 class="font-bold text-orange-800 dark:text-orange-400 flex items-center gap-2">
                        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z" />
                        </svg>
                        Đang hiển thị ({{ featuredApps.length }})
                    </h3>
                    <span class="text-[10px] font-bold uppercase tracking-wide text-orange-600 dark:text-orange-300 bg-orange-100 dark:bg-orange-900/30 px-2 py-1 rounded">Draggable</span>
                </div>

                <div class="flex-1 overflow-y-auto p-4 custom-scrollbar bg-gray-50/50 dark:bg-gray-900/50 h-full">
                    <draggable v-model="featuredApps" item-key="id" @end="onDragEnd" ghost-class="ghost"
                        class="space-y-3 min-h-[100px]">
                        <template #item="{ element }">
                            <div
                                class="bg-white dark:bg-gray-800 p-4 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm flex items-center gap-4 cursor-move hover:border-orange-300 dark:hover:border-orange-500 transition-all hover:shadow-md group">
                                <div class="font-bold text-gray-400 w-6 text-center text-sm">#{{ featuredApps.indexOf(element) + 1 }}</div>
                                <div class="relative w-12 h-12 rounded-lg overflow-hidden shrink-0 border border-gray-100 dark:border-gray-700">
                                     <img :src="element.thumbnail" class="w-full h-full object-cover bg-gray-100 dark:bg-gray-700" />
                                </div>
                                <div class="flex-1 min-w-0">
                                    <div class="font-bold text-gray-900 dark:text-white truncate">{{ element.name }}</div>
                                    <div class="text-xs text-gray-500 dark:text-gray-400 flex items-center gap-2 mt-0.5">
                                        {{ element.domainName }}
                                        <span class="w-1 h-1 rounded-full bg-gray-300 dark:bg-gray-600"></span>
                                        <span class="text-orange-500 font-bold">Featured</span>
                                    </div>
                                </div>
                                <button
                                    class="p-2 text-red-400 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg opacity-0 group-hover:opacity-100 transition-all"
                                    @click.stop="toggleFeatured(element)" title="Bỏ đính ghim">
                                    <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M6 18L18 6M6 6l12 12" />
                                    </svg>
                                </button>
                            </div>
                        </template>
                    </draggable>

                    <div v-if="featuredApps.length === 0" class="h-full flex flex-col items-center justify-center text-center p-8 text-gray-400 dark:text-gray-500 italic border-2 border-dashed border-gray-200 dark:border-gray-700 rounded-xl">
                        <p>Chưa có sản phẩm nào được ghim.</p>
                        <p class="text-xs mt-2">Chọn sản phẩm từ danh sách bên phải để thêm vào đây.</p>
                    </div>
                </div>
            </div>

            <!-- OTHER APPS -->
            <div class="flex flex-col min-h-0 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 rounded-xl shadow-sm overflow-hidden h-full">
                <div class="p-4 border-b border-gray-100 dark:border-gray-700 bg-gray-50 dark:bg-gray-700/30 flex justify-between items-center shrink-0">
                    <h3 class="font-bold text-gray-700 dark:text-gray-200 flex items-center gap-2">
                         <svg class="w-5 h-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" /></svg>
                        Danh sách sản phẩm khác ({{ otherApps.length }})
                    </h3>
                </div>
                <div class="flex-1 overflow-y-auto p-4 custom-scrollbar h-full">
                    <div class="space-y-2">
                        <div v-for="app in otherApps" :key="app.id"
                            class="flex items-center gap-3 p-3 hover:bg-gray-50 dark:hover:bg-gray-700/50 rounded-xl border border-transparent hover:border-gray-200 dark:hover:border-gray-600 transition-all group">
                             <div class="relative w-10 h-10 rounded-lg overflow-hidden shrink-0 border border-gray-100 dark:border-gray-700">
                                <img :src="app.thumbnail" class="w-full h-full object-cover bg-gray-100 dark:bg-gray-700" />
                             </div>
                            
                            <div class="flex-1 min-w-0">
                                <div class="font-bold text-gray-900 dark:text-white truncate">{{ app.name }}</div>
                                <div class="text-xs text-gray-500 dark:text-gray-400 flex items-center gap-2">
                                    <span>{{ app.domainName }}</span>
                                    <span v-if="app.approvalStatus !== 'APPROVED'"
                                        class="text-[10px] px-1.5 py-0.5 rounded font-bold uppercase tracking-wide bg-gray-100 dark:bg-gray-700 text-gray-500 dark:text-gray-400">{{ app.approvalStatus
                                        }}</span>
                                </div>
                            </div>
                           
                             <button @click="toggleFeatured(app)" 
                                class="px-3 py-1.5 bg-white dark:bg-gray-700 text-gray-600 dark:text-gray-300 border border-gray-200 dark:border-gray-600 rounded-lg text-xs font-bold hover:text-orange-600 dark:hover:text-orange-400 hover:border-orange-200 dark:hover:border-orange-500/50 transition-colors shadow-sm uppercase tracking-wide">
                                + Ghim
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.ghost {
    opacity: 0.5;
    background: #fff7ed;
    border: 1px dashed #f97316;
}
</style>
