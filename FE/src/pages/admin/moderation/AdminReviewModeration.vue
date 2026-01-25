<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { ReviewService, type Review } from '@/services/admin/review.service';
import { toast } from 'vue3-toastify';
import BaseCard from '@/components/base/BaseCard.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import BasePagination from '@/components/base/BasePagination.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';


import { formatDateTime } from '@/utils';

const reviews = ref<Review[]>([]);
const isLoading = ref(false);
const currentStatus = ref<'PENDING' | 'APPROVED' | 'REJECTED'>('PENDING');
const currentPage = ref(0);
const totalPages = ref(0);
const selectedIds = ref<string[]>([]);
const isAllSelected = ref(false);

const columns = [
    { label: '', class: 'w-10' },
    { label: 'Sản phẩm', class: 'w-1/4' },
    { label: 'Người dùng', class: 'w-1/5' },
    { label: 'Đánh giá', class: 'w-24' },
    { label: 'Nội dung', class: 'w-1/3' },
    { label: 'Ngày tạo', class: 'w-32' },
    { label: 'Hành động', class: 'w-32 text-right' },
];

const fetchReviews = async () => {
    isLoading.value = true;
    selectedIds.value = [];
    isAllSelected.value = false;
    try {
        const data = await ReviewService.getReviews(currentStatus.value, currentPage.value);
        reviews.value = data.content;
        totalPages.value = data.totalPages;
    } catch (e) {
        toast.error("Lỗi khi tải danh sách đánh giá");
    } finally {
        isLoading.value = false;
    }
};

onMounted(fetchReviews);

watch(currentStatus, () => {
    currentPage.value = 0;
    fetchReviews();
});

watch(currentPage, fetchReviews);

const toggleSelectAll = () => {
    if (isAllSelected.value) {
        selectedIds.value = reviews.value.map(r => r.id);
    } else {
        selectedIds.value = [];
    }
};



const handleApprove = async (id: string) => {
    try {
        await ReviewService.approveReview(id);
        toast.success("Đã duyệt đánh giá");
        fetchReviews();
    } catch (e) {
        toast.error("Lỗi khi duyệt");
    }
};

const handleReject = async (id: string) => {
    if (!confirm("Bạn có chắc muốn từ chối đánh giá này?")) return;
    try {
        await ReviewService.rejectReview(id);
        toast.success("Đã từ chối đánh giá");
        fetchReviews();
    } catch (e) {
        toast.error("Lỗi khi từ chối");
    }
};

const handleBatchApprove = async () => {
    if (selectedIds.value.length === 0) return;
    try {
        await ReviewService.batchApprove(selectedIds.value);
        toast.success(`Đã duyệt ${selectedIds.value.length} đánh giá`);
        fetchReviews();
    } catch (e) {
        toast.error("Lỗi xử lý hàng loạt");
    }
};

const handleBatchReject = async () => {
    if (selectedIds.value.length === 0) return;
    if (!confirm(`Bạn có chắc muốn từ chối ${selectedIds.value.length} đánh giá?`)) return;
    try {
        await ReviewService.batchReject(selectedIds.value);
        toast.success(`Đã từ chối ${selectedIds.value.length} đánh giá`);
        fetchReviews();
    } catch (e) {
        toast.error("Lỗi xử lý hàng loạt");
    }
};
</script>

<template>
    <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div class="mb-6 shrink-0">
             <div class="flex flex-col">
                <span class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Kiểm duyệt nội dung</span>
                <h1 class="text-3xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight mt-1">Kiểm duyệt đánh giá</h1>
             </div>
        </div>

        <div class="flex-1 overflow-auto custom-scrollbar flex flex-col gap-4">
            <!-- Toolbar -->
            <div class="flex flex-col md:flex-row justify-between items-center bg-white dark:bg-gray-800 p-4 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm">
                <div class="flex bg-gray-100 dark:bg-gray-700 p-1 rounded-lg">
                    <button v-for="status in ['PENDING', 'APPROVED', 'REJECTED']" :key="status"
                        @click="currentStatus = status as any" :class="[
                            'px-4 py-2 text-sm font-bold rounded-md transition-all uppercase tracking-wide',
                            currentStatus === status ? 'bg-white dark:bg-gray-600 text-primary dark:text-white shadow-sm' : 'text-gray-500 dark:text-gray-400 hover:text-gray-700 dark:hover:text-gray-200'
                        ]">
                        {{ status === 'PENDING' ? 'Chờ duyệt' : (status === 'APPROVED' ? 'Đã duyệt' : 'Đã từ chối') }}
                    </button>
                </div>

                <div class="flex gap-2" v-if="selectedIds.length > 0">
                    <BaseButton size="sm" variant="outline" class="!text-red-600 !border-red-200 hover:!bg-red-50 dark:hover:!bg-red-900/30"
                        @click="handleBatchReject">
                        Từ chối ({{ selectedIds.length }})
                    </BaseButton>
                    <BaseButton size="sm" variant="primary" class="!bg-green-600 border-green-600 hover:!bg-green-700" @click="handleBatchApprove">
                        Duyệt ({{ selectedIds.length }})
                    </BaseButton>
                </div>
            </div>

            <!-- Table -->
            <div class="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm overflow-hidden flex-1 flex flex-col">
                <div v-if="isLoading" class="p-12 text-center flex-1 flex items-center justify-center">
                    <BaseSpinner size="lg" />
                </div>

                <div v-else class="flex-1 overflow-auto custom-scrollbar">
                    <table class="w-full text-left border-collapse">
                        <thead class="bg-gray-50 dark:bg-gray-900/50 border-b border-gray-100 dark:border-gray-700 sticky top-0 z-10 backdrop-blur-sm">
                            <tr>
                                <th scope="col" class="p-4 w-10">
                                    <div class="flex items-center">
                                        <input type="checkbox" v-model="isAllSelected" @change="toggleSelectAll"
                                            class="w-4 h-4 text-primary bg-white dark:bg-gray-700 border-gray-300 dark:border-gray-600 rounded focus:ring-primary">
                                    </div>
                                </th>
                                <th v-for="col in columns" :key="col.label" scope="col" class="px-6 py-4 text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider"
                                    :class="col.class">
                                    {{ col.label }}
                                </th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-50 dark:divide-gray-700/50">
                            <tr v-if="reviews.length === 0">
                                <td colspan="8" class="px-6 py-12 text-center text-gray-400 dark:text-gray-500 italic">
                                    Không có dữ liệu
                                </td>
                            </tr>
                            <tr v-for="review in reviews" :key="review.id" class="hover:bg-primary/5 dark:hover:bg-primary/5 transition-colors">
                                <td class="w-4 p-4">
                                    <div class="flex items-center">
                                        <input type="checkbox" :value="review.id" v-model="selectedIds"
                                            class="w-4 h-4 text-primary bg-white dark:bg-gray-700 border-gray-300 dark:border-gray-600 rounded focus:ring-primary">
                                    </div>
                                </td>
                                <td class="px-6 py-4 font-bold text-gray-900 dark:text-white">
                                    {{ review.app?.name || 'Unknown App' }}
                                </td>
                                <td class="px-6 py-4">
                                    <div class="flex flex-col">
                                        <span class="font-medium text-gray-900 dark:text-white">{{ review.userName || 'Guest' }}</span>
                                        <span class="text-xs text-gray-500 dark:text-gray-400">{{ review.userEmail }}</span>
                                    </div>
                                </td>
                                <td class="px-6 py-4">
                                    <div class="flex text-yellow-500 text-sm">
                                        <span v-for="i in 5" :key="i" v-text="i <= review.rating ? '★' : '☆'"></span>
                                    </div>
                                </td>
                                <td class="px-6 py-4">
                                    <p class="line-clamp-2 text-sm text-gray-600 dark:text-gray-300" :title="review.comment">{{ review.comment }}</p>
                                </td>
                                <td class="px-6 py-4 text-xs font-mono text-gray-400 dark:text-gray-500">
                                    {{ formatDateTime(review.createdAt) }}
                                </td>
                                <td class="px-6 py-4 text-right">
                                    <div class="flex justify-end gap-2" v-if="currentStatus === 'PENDING'">
                                        <button @click="handleReject(review.id)"
                                            class="text-red-500 hover:text-red-700 dark:text-red-400 dark:hover:text-red-300 font-bold text-[10px] border border-red-200 dark:border-red-900/50 bg-red-50 dark:bg-red-900/20 px-2 py-1 rounded uppercase tracking-wide transition-colors">
                                            Từ chối
                                        </button>
                                        <button @click="handleApprove(review.id)"
                                            class="text-green-600 hover:text-green-800 dark:text-green-400 dark:hover:text-green-300 font-bold text-[10px] border border-green-200 dark:border-green-900/50 bg-green-50 dark:bg-green-900/20 px-2 py-1 rounded uppercase tracking-wide transition-colors">
                                            Duyệt
                                        </button>
                                    </div>
                                    <div v-else class="text-xs">
                                        <span v-if="review.moderationStatus === 'APPROVED'"
                                            class="text-green-600 bg-green-100 dark:bg-green-900/30 dark:text-green-300 px-2 py-1 rounded-full font-bold uppercase text-[10px]">Đã duyệt</span>
                                        <span v-if="review.moderationStatus === 'REJECTED'"
                                            class="text-red-600 bg-red-100 dark:bg-red-900/30 dark:text-red-300 px-2 py-1 rounded-full font-bold uppercase text-[10px]">Đã từ chối</span>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <!-- Pagination -->
                <div class="p-4 border-t border-gray-100 dark:border-gray-700 bg-gray-50 dark:bg-gray-900/50 flex justify-end">
                    <BasePagination :current-page="currentPage + 1" :total-pages="totalPages"
                        @page-change="(p) => currentPage = p - 1" />
                </div>
            </div>
        </div>
    </div>
</template>
