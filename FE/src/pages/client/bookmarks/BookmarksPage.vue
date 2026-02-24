<template>
    <div class="min-h-screen bg-gray-50 dark:bg-[#0d0d1a] py-16 transition-colors duration-300">
        <div class="container mx-auto px-4 max-w-7xl">

            <!-- ── Header ── -->
            <div class="mb-12 text-center">
                <div class="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-pink-50 dark:bg-pink-900/20 border border-pink-200 dark:border-pink-800/40 text-pink-600 dark:text-pink-400 text-xs font-bold uppercase tracking-widest mb-5">
                    <svg class="w-3.5 h-3.5" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                    </svg>
                    Yêu thích của bạn
                </div>
                <h1 class="text-4xl md:text-5xl font-bold text-gray-900 dark:text-white mb-3 tracking-tight">
                    Sản phẩm yêu thích
                </h1>
                <p class="text-gray-500 dark:text-gray-400 text-lg">
                    <span v-if="bookmarks.length > 0">
                        <strong class="text-violet-600 dark:text-violet-400">{{ bookmarks.length }}</strong> dự án trong bộ sưu tập của bạn
                    </span>
                    <span v-else>Chưa có dự án nào được lưu lại</span>
                </p>
            </div>

            <!-- ╔══════════════════════════════════════════╗ -->
            <!-- ║  EMPTY STATE (Cảm xúc & Hành động)     ║ -->
            <!-- ╚══════════════════════════════════════════╝ -->
            <div v-if="bookmarks.length === 0" class="max-w-2xl mx-auto">
                <div class="empty-state-card rounded-3xl overflow-hidden">
                    <!-- Illustration top section -->
                    <div class="empty-illustration relative h-52 flex items-center justify-center overflow-hidden">
                        <!-- Animated blobs -->
                        <div class="empty-blob empty-blob-1"></div>
                        <div class="empty-blob empty-blob-2"></div>
                        <div class="empty-blob empty-blob-3"></div>

                        <!-- Floating cards illustration -->
                        <div class="relative z-10 flex items-center gap-4">
                            <div v-for="i in 3" :key="i"
                                class="empty-card-float"
                                :style="`animation-delay: ${(i-1) * 0.3}s`">
                                <div class="w-16 h-20 rounded-xl bg-white/20 backdrop-blur-sm border border-white/30 flex items-center justify-center">
                                    <svg class="w-8 h-8 text-white/50" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                            d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                    </svg>
                                </div>
                            </div>
                        </div>

                        <!-- Heart trail -->
                        <div v-for="i in 5" :key="`h${i}`"
                            class="absolute text-white/20 text-2xl select-none"
                            :style="`top:${15 + i*14}%; left:${8 + i*18}%; animation: float-heart ${2 + i*0.4}s ease-in-out ${i*0.2}s infinite alternate;`">
                            ♥
                        </div>
                    </div>

                    <!-- Text & CTA -->
                    <div class="p-10 text-center bg-white dark:bg-gray-900">
                        <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-3">
                            Chưa có dự án nào lọt vào mắt xanh? 👀
                        </h2>
                        <p class="text-gray-500 dark:text-gray-400 leading-relaxed mb-8 max-w-sm mx-auto">
                            Khám phá hàng trăm dự án sáng tạo từ sinh viên FPL. Nhấn ❤️ để lưu những dự án bạn yêu thích!
                        </p>
                        <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name }"
                            class="explore-btn">
                            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M5 3l14 9-14 9V3z" />
                            </svg>
                            Khám phá ngay!
                        </router-link>
                    </div>
                </div>
            </div>

            <!-- ╔══════════════════════════════════════════╗ -->
            <!-- ║  BENTO GRID — Favorites                 ║ -->
            <!-- ╚══════════════════════════════════════════╝ -->
            <div v-else>
                <!-- Bento layout: first card is large, rest are normal -->
                <div class="bento-grid">

                    <!-- FEATURED (first item) -->
                    <div v-if="bookmarks[0]" class="bento-featured group">
                        <div class="relative h-full rounded-2xl overflow-hidden cursor-pointer"
                            @click="navigateTo(bookmarks[0])">
                            <!-- Image / Gradient -->
                            <div class="absolute inset-0">
                                <img v-if="bookmarks[0].thumbnail" :src="bookmarks[0].thumbnail" :alt="bookmarks[0].name"
                                    class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105" />
                                <div v-else class="w-full h-full"
                                    :style="getGradient(bookmarks[0])"></div>
                            </div>
                            <!-- Always-on dark overlay -->
                            <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-black/30 to-transparent"></div>

                            <!-- Remove button -->
                            <button @click.stop="handleRemoveLike(bookmarks[0].id)"
                                class="remove-btn absolute top-4 right-4 z-20">
                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                                </svg>
                            </button>

                            <!-- Content -->
                            <div class="absolute bottom-0 left-0 right-0 p-6 z-10">
                                <span v-if="bookmarks[0].domainName"
                                    class="inline-block px-3 py-1 rounded-full bg-white/20 backdrop-blur-sm text-white text-[10px] font-bold uppercase tracking-wider mb-3">
                                    {{ bookmarks[0].domainName }}
                                </span>
                                <h2 class="text-2xl font-bold text-white mb-2 line-clamp-2">{{ bookmarks[0].name }}</h2>
                                <p class="text-gray-300 text-sm line-clamp-2">{{ bookmarks[0].description }}</p>
                                <div class="mt-4 inline-flex items-center gap-2 text-white/80 text-sm font-semibold group-hover:text-white transition-colors">
                                    Xem dự án
                                    <svg class="w-4 h-4 group-hover:translate-x-1 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6"/>
                                    </svg>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- REST OF ITEMS -->
                    <div v-for="(item, idx) in bookmarks.slice(1)" :key="item.id"
                        class="bento-item group"
                        :style="`animation-delay: ${(idx + 1) * 80}ms`">
                        <div class="relative h-full rounded-2xl overflow-hidden bg-white dark:bg-gray-800 border border-gray-100 dark:border-gray-700 cursor-pointer hover:shadow-xl transition-all duration-300 hover:-translate-y-1"
                            @click="navigateTo(item)">

                            <!-- Thumbnail -->
                            <div class="relative h-40 overflow-hidden">
                                <img v-if="item.thumbnail" :src="item.thumbnail" :alt="item.name"
                                    class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110" />
                                <div v-else class="w-full h-full" :style="getGradient(item)">
                                    <div class="absolute inset-0 flex items-center justify-center">
                                        <svg class="w-10 h-10 text-white/40" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                                                d="M10 20l4-16m4 4l4 4-4 4M6 16l-4-4 4-4"/>
                                        </svg>
                                    </div>
                                </div>
                                <div class="absolute inset-0 bg-gradient-to-t from-black/40 to-transparent"></div>

                                <!-- Remove -->
                                <button @click.stop="handleRemoveLike(item.id)"
                                    class="remove-btn absolute top-3 right-3 z-20 opacity-0 group-hover:opacity-100 transition-opacity">
                                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                                    </svg>
                                </button>
                            </div>

                            <!-- Info -->
                            <div class="p-4">
                                <span v-if="item.domainName"
                                    class="inline-block px-2 py-0.5 rounded-full bg-violet-50 dark:bg-violet-900/30 text-violet-600 dark:text-violet-400 text-[10px] font-bold uppercase tracking-wider mb-2">
                                    {{ item.domainName }}
                                </span>
                                <h3 class="font-bold text-gray-900 dark:text-white text-sm line-clamp-1 group-hover:text-violet-600 transition-colors">
                                    {{ item.name }}
                                </h3>
                                <p class="text-gray-500 dark:text-gray-400 text-xs mt-1 line-clamp-2">{{ item.description }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useLikeStore } from '@/stores/like.store';
import { ROUTES_CONSTANTS } from '@/constants/path';
import { toast } from 'vue3-toastify';
import { encodeId } from '@/utils';

const likeStore = useLikeStore();
const bookmarks = ref<any[]>([]);
const router = useRouter();

const fetchBookmarks = async () => {
    bookmarks.value = await likeStore.getLikedProducts();
};

const handleRemoveLike = async (id: string) => {
    try {
        await likeStore.toggleLike(id);
        toast.success('Đã bỏ thích sản phẩm');
        await fetchBookmarks();
    } catch {
        toast.error('Có lỗi xảy ra');
    }
};

const navigateTo = (item: any) => {
    router.push({
        name: ROUTES_CONSTANTS.CUSTOMER.children.APP_DETAIL.name,
        params: { id: encodeId(item.id) }
    });
};

const gradients = [
    'linear-gradient(135deg,#7c3aed,#6d28d9)',
    'linear-gradient(135deg,#2563eb,#1d4ed8)',
    'linear-gradient(135deg,#0891b2,#0e7490)',
    'linear-gradient(135deg,#16a34a,#15803d)',
    'linear-gradient(135deg,#dc2626,#b91c1c)',
    'linear-gradient(135deg,#d97706,#b45309)',
    'linear-gradient(135deg,#db2777,#be185d)',
];

const getGradient = (item: any) => {
    const idx = item.id ? item.id.charCodeAt(0) % gradients.length : 0;
    return { background: gradients[idx] };
};

onMounted(fetchBookmarks);
</script>

<style scoped>
/* ── Empty State ── */
.empty-state-card {
    box-shadow: 0 24px 60px rgba(0,0,0,0.08);
    border: 1px solid rgba(0,0,0,0.05);
}

.empty-illustration {
    background: linear-gradient(135deg, #7c3aed, #2563eb, #06b6d4);
}

.empty-blob {
    position: absolute;
    border-radius: 50%;
    filter: blur(50px);
    opacity: 0.5;
}

.empty-blob-1 {
    width: 250px; height: 250px;
    background: #a855f7;
    top: -80px; left: -60px;
    animation: blob-drift 8s ease-in-out infinite alternate;
}

.empty-blob-2 {
    width: 200px; height: 200px;
    background: #3b82f6;
    bottom: -60px; right: -40px;
    animation: blob-drift 12s ease-in-out 2s infinite alternate;
}

.empty-blob-3 {
    width: 150px; height: 150px;
    background: #06b6d4;
    top: 30%; left: 40%;
    animation: blob-drift 10s ease-in-out 4s infinite alternate;
}

@keyframes blob-drift {
    to { transform: translate(30px, 20px) scale(1.1); }
}

.empty-card-float {
    animation: empty-float 3s ease-in-out infinite alternate;
}

@keyframes empty-float {
    to { transform: translateY(-12px); }
}

@keyframes float-heart {
    to { transform: translateY(-10px) scale(1.2); opacity: 0.4; }
}

.explore-btn {
    display: inline-flex;
    align-items: center;
    gap: 10px;
    padding: 14px 32px;
    border-radius: 14px;
    background: linear-gradient(135deg, #7c3aed, #2563eb);
    color: white;
    font-weight: 700;
    font-size: 15px;
    text-decoration: none;
    box-shadow: 0 8px 24px rgba(124, 58, 237, 0.35);
    transition: all 0.3s ease;
}

.explore-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 14px 32px rgba(124, 58, 237, 0.5);
}

/* ── Bento Grid ── */
.bento-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    grid-auto-rows: minmax(180px, auto);
    gap: 20px;
}

.bento-featured {
    grid-column: span 2;
    grid-row: span 2;
    min-height: 420px;
}

.bento-item {
    animation: item-reveal 0.5s ease both;
}

@keyframes item-reveal {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 1024px) {
    .bento-grid {
        grid-template-columns: repeat(2, 1fr);
    }
    .bento-featured {
        grid-column: span 2;
        grid-row: span 1;
        min-height: 300px;
    }
}

@media (max-width: 640px) {
    .bento-grid {
        grid-template-columns: 1fr;
    }
    .bento-featured {
        grid-column: span 1;
    }
}

/* ── Remove Button ── */
.remove-btn {
    padding: 7px;
    border-radius: 50%;
    background: #ef4444;
    color: white;
    border: none;
    cursor: pointer;
    transition: all 0.2s ease;
    box-shadow: 0 2px 8px rgba(239,68,68,0.4);
}

.remove-btn:hover {
    background: #dc2626;
    transform: scale(1.1);
}
</style>
