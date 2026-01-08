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
        const all = res.content || [];

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
    <div class="p-6 h-full flex flex-col">
        <div class="mb-6 shrink-0 flex justify-between items-start">
            <div>
                <BaseBreadcrumb :items="[{ label: 'Admin', to: '/admin' }, { label: 'Cấu hình Trang chủ' }]" />
                <h1 class="text-2xl font-bold text-dark font-serif uppercase">Cấu hình Sản phẩm nổi bật</h1>
                <p class="text-gray-500 text-sm mt-1">Kéo thả để sắp xếp thứ tự hiển thị trên trang chủ</p>
            </div>
            <BaseButton variant="outline" @click="loadData">Làm mới</BaseButton>
        </div>

        <div v-if="isLoading" class="flex-1 flex justify-center items-center">
            <BaseSpinner size="lg" />
        </div>

        <div v-else class="grid grid-cols-1 lg:grid-cols-2 gap-6 flex-1 overflow-hidden">
            <!-- FEATURED LIST -->
            <div class="flex flex-col min-h-0 bg-white border border-gray-200 rounded-lg shadow-sm">
                <div class="p-4 border-b border-gray-100 bg-orange-50 flex justify-between items-center">
                    <h3 class="font-bold text-orange-800 flex items-center gap-2">
                        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z" />
                        </svg>
                        Đang hiển thị ({{ featuredApps.length }})
                    </h3>
                    <span class="text-xs text-orange-600 bg-orange-100 px-2 py-1 rounded">Draggable</span>
                </div>

                <div class="flex-1 overflow-y-auto p-4 custom-scrollbar bg-gray-50/50">
                    <draggable v-model="featuredApps" item-key="id" @end="onDragEnd" ghost-class="ghost"
                        class="space-y-3">
                        <template #item="{ element }">
                            <div
                                class="bg-white p-3 rounded border border-gray-200 shadow-sm flex items-center gap-3 cursor-move hover:border-orange-300 transition-colors group">
                                <div class="font-bold text-gray-400 w-6 text-center">#{{ featuredApps.indexOf(element) +
                                    1 }}</div>
                                <img :src="element.thumbnail" class="w-10 h-10 rounded object-cover bg-gray-100" />
                                <div class="flex-1 min-w-0">
                                    <div class="font-bold text-dark truncate">{{ element.name }}</div>
                                    <div class="text-xs text-gray-500">{{ element.domainName }}</div>
                                </div>
                                <BaseButton size="sm" variant="danger"
                                    class="!p-1.5 opacity-0 group-hover:opacity-100 transition-opacity"
                                    @click.stop="toggleFeatured(element)" title="Bỏ đính ghim">
                                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M6 18L18 6M6 6l12 12" />
                                    </svg>
                                </BaseButton>
                            </div>
                        </template>
                    </draggable>

                    <div v-if="featuredApps.length === 0" class="text-center py-10 text-gray-400 italic">
                        Chưa có sản phẩm nào được ghim.
                    </div>
                </div>
            </div>

            <!-- OTHER APPS -->
            <div class="flex flex-col min-h-0 bg-white border border-gray-200 rounded-lg shadow-sm">
                <div class="p-4 border-b border-gray-100 bg-gray-50 flex justify-between items-center">
                    <h3 class="font-bold text-gray-700">Danh sách sản phẩm khác ({{ otherApps.length }})</h3>
                </div>
                <div class="flex-1 overflow-y-auto p-4 custom-scrollbar">
                    <div class="space-y-2">
                        <div v-for="app in otherApps" :key="app.id"
                            class="flex items-center gap-3 p-3 hover:bg-gray-50 rounded border border-transparent hover:border-gray-100 transition-colors">
                            <img :src="app.thumbnail" class="w-10 h-10 rounded object-cover bg-gray-100" />
                            <div class="flex-1 min-w-0">
                                <div class="font-bold text-dark truncate">{{ app.name }}</div>
                                <div class="text-xs text-gray-500 flex items-center gap-2">
                                    <span>{{ app.domainName }}</span>
                                    <span v-if="app.approvalStatus !== 'APPROVED'"
                                        class="text-xs px-1.5 rounded bg-gray-200 text-gray-600">{{ app.approvalStatus
                                        }}</span>
                                </div>
                            </div>
                            <BaseButton size="sm" variant="outline"
                                class="!text-orange-600 !border-orange-200 hover:!bg-orange-50"
                                @click="toggleFeatured(app)">
                                + Ghim
                            </BaseButton>
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
