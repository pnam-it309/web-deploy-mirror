<template>
    <div class="relative group inline-flex">
        <button
            v-bind="$attrs"
            @click.stop="handleToggleLike"
            :disabled="isLoading"
            class="like-btn relative"
            :class="isLiked ? 'liked' : 'not-liked'"
        >
            <!-- Confetti particles -->
            <span v-for="i in 8" :key="i" class="confetti-particle"
                :class="[`p-${i}`, isAnimating ? 'burst' : '']">
            </span>

            <!-- Heart SVG -->
            <svg class="heart-icon w-5 h-5 relative z-10" :class="{ 'heart-beat': isAnimating }"
                viewBox="0 0 24 24" stroke="currentColor" :fill="isLiked ? 'currentColor' : 'none'">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
            </svg>

            <!-- Count -->
            <span v-if="showCount && likeCount > 0" class="ml-1 text-xs font-bold relative z-10">{{ likeCount }}</span>
        </button>

        <!-- Tooltip -->
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

defineOptions({ inheritAttrs: false });

const props = defineProps<{
    productId: string;
    showCount?: boolean;
}>();

const likeStore = useLikeStore();
const isLoading = ref(false);
const isLiked = ref(false);
const likeCount = ref(0);
const isAnimating = ref(false);

onMounted(async () => { await fetchLikeStatus(); });

const fetchLikeStatus = async () => {
    try {
        const status = await likeStore.getLikeStatus(props.productId);
        isLiked.value = status.isLiked;
        likeCount.value = status.likeCount;
    } catch { /* silent */ }
};

const handleToggleLike = async () => {
    isLoading.value = true;
    try {
        const result = await likeStore.toggleLike(props.productId);
        isLiked.value = result.isLiked;
        likeCount.value = result.likeCount;

        if (result.isLiked) {
            // Trigger animation only when liking
            isAnimating.value = true;
            setTimeout(() => { isAnimating.value = false; }, 700);
        }

        toast.success(isLiked.value ? 'Đã thích sản phẩm ❤️' : 'Đã bỏ thích');
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

<style scoped>
/* ── Button base ── */
.like-btn {
    display: inline-flex;
    align-items: center;
    padding: 8px 10px;
    border-radius: 999px;
    border: none;
    cursor: pointer;
    transition: all 0.25s ease;
    overflow: visible;
    position: relative;
}

.not-liked {
    background: rgba(255, 255, 255, 0.9);
    color: #6b7280;
    box-shadow: 0 2px 8px rgba(0,0,0,0.12);
}

.not-liked:hover {
    background: rgba(255, 255, 255, 1);
    color: #ef4444;
    transform: scale(1.08);
}

.liked {
    background: linear-gradient(135deg, #ef4444, #f43f5e);
    color: white;
    box-shadow: 0 4px 16px rgba(239, 68, 68, 0.45);
}

.liked:hover {
    box-shadow: 0 6px 20px rgba(239, 68, 68, 0.6);
    transform: scale(1.08);
}

/* ── Heart beat animation ── */
.heart-icon {
    transition: transform 0.15s ease;
}

.heart-beat {
    animation: heart-bounce 0.6s cubic-bezier(0.36, 0.07, 0.19, 0.97);
}

@keyframes heart-bounce {
    0%   { transform: scale(1); }
    20%  { transform: scale(1.5); }
    40%  { transform: scale(0.9); }
    60%  { transform: scale(1.25); }
    80%  { transform: scale(0.95); }
    100% { transform: scale(1); }
}

/* ── Confetti particles ── */
.confetti-particle {
    position: absolute;
    width: 6px;
    height: 6px;
    border-radius: 50%;
    pointer-events: none;
    opacity: 0;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}

/* Colors */
.p-1 { background: #f87171; }
.p-2 { background: #fbbf24; }
.p-3 { background: #34d399; }
.p-4 { background: #60a5fa; }
.p-5 { background: #a78bfa; }
.p-6 { background: #f472b6; }
.p-7 { background: #fb923c; }
.p-8 { background: #4ade80; }

/* Burst directions */
.p-1.burst { animation: burst-1 0.6s ease forwards; }
.p-2.burst { animation: burst-2 0.6s ease 0.05s forwards; }
.p-3.burst { animation: burst-3 0.6s ease 0.1s forwards; }
.p-4.burst { animation: burst-4 0.6s ease 0.05s forwards; }
.p-5.burst { animation: burst-5 0.6s ease 0.08s forwards; }
.p-6.burst { animation: burst-6 0.6s ease 0.03s forwards; }
.p-7.burst { animation: burst-7 0.6s ease 0.07s forwards; }
.p-8.burst { animation: burst-8 0.6s ease 0.04s forwards; }

@keyframes burst-1 { to { opacity: 0; transform: translate(-22px, -22px) scale(0); } 10% { opacity: 1; } }
@keyframes burst-2 { to { opacity: 0; transform: translate(0, -28px) scale(0); } 10% { opacity: 1; } }
@keyframes burst-3 { to { opacity: 0; transform: translate(22px, -22px) scale(0); } 10% { opacity: 1; } }
@keyframes burst-4 { to { opacity: 0; transform: translate(28px, 0) scale(0); } 10% { opacity: 1; } }
@keyframes burst-5 { to { opacity: 0; transform: translate(22px, 22px) scale(0); } 10% { opacity: 1; } }
@keyframes burst-6 { to { opacity: 0; transform: translate(0, 28px) scale(0); } 10% { opacity: 1; } }
@keyframes burst-7 { to { opacity: 0; transform: translate(-22px, 22px) scale(0); } 10% { opacity: 1; } }
@keyframes burst-8 { to { opacity: 0; transform: translate(-28px, 0) scale(0); } 10% { opacity: 1; } }
</style>
