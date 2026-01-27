<template>
    <div class="relative group inline-flex">
        <button
            v-bind="$attrs"
            @click.stop="handleToggleLike"
            :disabled="isLoading"
            class="p-2 rounded-full transition-all duration-300 backdrop-blur-sm"
            :class="isLiked ? 'bg-red-500/90 text-white hover:bg-red-600' : 'bg-white/90 text-gray-600 hover:bg-gray-100'"
        >
            <svg class="w-5 h-5" :class="{'fill-current': isLiked}" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
                    d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
            </svg>
            <span v-if="showCount && likeCount > 0" class="ml-1 text-xs font-bold">{{ likeCount }}</span>
        </button>

        <!-- Custom Tooltip -->
        <div class="absolute right-0 top-full mt-2 px-2 py-1 bg-gray-900/90 text-white text-xs rounded whitespace-nowrap opacity-0 group-hover:opacity-100 transition-opacity duration-200 pointer-events-none z-50 backdrop-blur-sm shadow-lg">
            {{ isLiked ? 'Bỏ thích' : 'Thích sản phẩm' }}
            <div class="absolute bottom-full right-3 border-4 border-transparent border-b-gray-900/90"></div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useLikeStore } from '@/stores/like.store';
import { toast } from 'vue3-toastify';

defineOptions({
    inheritAttrs: false
});

const props = defineProps<{
    productId: string;
    showCount?: boolean;
}>();

const likeStore = useLikeStore();
const isLoading = ref(false);
const isLiked = ref(false);
const likeCount = ref(0);

onMounted(async () => {
    await fetchLikeStatus();
});

const fetchLikeStatus = async () => {
    try {
        const status = await likeStore.getLikeStatus(props.productId);
        isLiked.value = status.isLiked;
        likeCount.value = status.likeCount;
    } catch (error) {
        console.error('Failed to fetch like status:', error);
    }
};

const handleToggleLike = async () => {
    isLoading.value = true;
    try {
        const result = await likeStore.toggleLike(props.productId);
        isLiked.value = result.isLiked;
        likeCount.value = result.likeCount;
        toast.success(isLiked.value ? 'Đã thích sản phẩm' : 'Đã bỏ thích');
    } catch (error: any) {
        if (error.response?.status === 401) {
            toast.info('Vui lòng đăng nhập để thích sản phẩm');
        } else {
            toast.error('Có lỗi xảy ra');
        }
    } finally {
        isLoading.value = false;
    }
};
</script>
