<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import {
  CubeIcon,
  FolderIcon,
  UserGroupIcon,
  EyeIcon,
  SparklesIcon,
  CpuChipIcon,
  ArrowTrendingUpIcon,
  StarIcon,
} from '@heroicons/vue/24/outline';
import { getDashboardStats, type DashboardStats } from '@/services/admin/dashboard.service';
import draggable from 'vuedraggable';
import { AppService } from '@/services/admin/app.service';
import { toast } from 'vue3-toastify';
import { useThemeStore } from '@/stores/theme.store';

const themeStore = useThemeStore();
const featuredApps = ref<any[]>([]);
const otherApps = ref<any[]>([]);
const isLoading = ref(false);

const stats = ref<DashboardStats>({
  totalApps: 0,
  totalDomains: 0,
  totalTechnologies: 0,
  totalStudents: 0,
  totalViews: 0,
  totalFeatures: 0
});

// Sparkline mock data for chart (7-day view trend)
const chartData = ref([420, 680, 550, 890, 740, 1020, 1205]);
const chartMax = computed(() => Math.max(...chartData.value));
const chartPoints = computed(() => {
  const w = 280, h = 80;
  return chartData.value.map((v, i) => {
    const x = (i / (chartData.value.length - 1)) * w;
    const y = h - (v / chartMax.value) * h * 0.85 - h * 0.05;
    return `${x},${y}`;
  }).join(' ');
});

const loadData = async () => {
  isLoading.value = true;
  try {
    const res = await AppService.getAll({ size: 1000 });
    const all = res.content || [];
    featuredApps.value = all
      .filter((a: any) => a.isFeatured)
      .sort((a: any, b: any) => (a.homepageSortOrder || 0) - (b.homepageSortOrder || 0));
    otherApps.value = all.filter((a: any) => !a.isFeatured);
  } catch (e) {
    console.error(e);
  } finally {
    isLoading.value = false;
  }
};

onMounted(async () => {
  isLoading.value = true;
  try {
    const [statsRes] = await Promise.all([getDashboardStats(), loadData()]);
    if (statsRes) stats.value = statsRes;
  } catch (e) {
    console.error(e);
  } finally {
    isLoading.value = false;
  }
});

const onDragEnd = async () => {
  try {
    const payload = featuredApps.value.map((app, index) => ({
      id: app.id,
      homepageSortOrder: index + 1
    }));
    await AppService.updateHomepageOrder(payload);
    toast.success('Đã cập nhật thứ tự hiển thị!');
  } catch {
    toast.error('Lỗi cập nhật thứ tự');
  }
};

const toggleFeatured = async (app: any) => {
  try {
    await AppService.toggleFeatured(app.id);
    const isNowFeatured = !app.isFeatured;
    toast.success(isNowFeatured ? 'Đã thêm vào Hot! 🔥' : 'Đã bỏ khỏi Hot!');
    loadData();
  } catch {
    toast.error('Lỗi cập nhật trạng thái');
  }
};

const statCards = computed(() => [
  {
    label: 'Dự án',
    value: stats.value.totalApps,
    icon: CubeIcon,
    trend: '+12%',
    trendUp: true,
    hint: 'so với tháng trước',
    color: '#8b5cf6',
    bg: 'rgba(139, 92, 246, 0.12)',
    glow: 'rgba(139, 92, 246, 0.3)',
  },
  {
    label: 'Sinh viên',
    value: stats.value.totalStudents,
    icon: UserGroupIcon,
    trend: '+5%',
    trendUp: true,
    hint: 'tài khoản đăng ký',
    color: '#06b6d4',
    bg: 'rgba(6, 182, 212, 0.12)',
    glow: 'rgba(6, 182, 212, 0.3)',
  },
  {
    label: 'Lượt xem',
    value: stats.value.totalViews,
    icon: EyeIcon,
    trend: '+24%',
    trendUp: true,
    hint: 'tổng lượt xem',
    color: '#10b981',
    bg: 'rgba(16, 185, 129, 0.12)',
    glow: 'rgba(16, 185, 129, 0.3)',
  },
  {
    label: 'Lĩnh vực',
    value: stats.value.totalDomains,
    icon: FolderIcon,
    trend: null,
    trendUp: null,
    hint: 'đang hoạt động',
    color: '#f59e0b',
    bg: 'rgba(245, 158, 11, 0.12)',
    glow: 'rgba(245, 158, 11, 0.3)',
  },
  {
    label: 'Công nghệ',
    value: stats.value.totalTechnologies,
    icon: CpuChipIcon,
    trend: null,
    trendUp: null,
    hint: 'trong hệ thống',
    color: '#f43f5e',
    bg: 'rgba(244, 63, 94, 0.12)',
    glow: 'rgba(244, 63, 94, 0.3)',
  },
  {
    label: 'Chức năng',
    value: stats.value.totalFeatures,
    icon: SparklesIcon,
    trend: null,
    trendUp: null,
    hint: 'tính năng hệ thống',
    color: '#6366f1',
    bg: 'rgba(99, 102, 241, 0.12)',
    glow: 'rgba(99, 102, 241, 0.3)',
  },
]);
</script>

<template>
  <div class="dashboard-root min-h-full py-8 transition-colors duration-300"
    :class="themeStore.theme === 'dark' ? 'root-dark' : 'root-light'">
    
    <!-- ── BACKGROUND BLOBS (Dark Mode Only) ── -->
    <div class="dashboard-blobs" v-if="themeStore.theme === 'dark'">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
      <div class="blob blob-3"></div>
    </div>

    <!-- ── HEADER ── -->
    <div class="px-8 mb-8 flex flex-col md:flex-row md:items-center justify-between gap-6 relative z-10">
      <div>
        <h1 class="header-title text-3xl font-black tracking-tight uppercase">Hệ thống Tổng quan</h1>
        <p class="header-subtitle text-sm font-medium mt-1">Sản phẩm kỹ thuật & Đo lường tăng trưởng</p>
      </div>
      <div class="flex items-center gap-4">
      </div>
    </div>

    <!-- ── HERO CHART SECTION ── -->
    <div class="px-8 mb-8 relative z-10">
      <div class="chart-card-shell relative overflow-hidden rounded-3xl p-8 border backdrop-blur-md">
        <div class="relative z-10 flex flex-col lg:flex-row items-center gap-12">
          <div class="lg:w-80 flex-shrink-0 text-center lg:text-left">
            <p class="chart-label text-[10px] font-black uppercase tracking-[0.2em] mb-2">Chỉ số truy cập (7D)</p>
            <div class="chart-value text-6xl font-black tracking-tighter flex items-end justify-center lg:justify-start gap-2">
              {{ stats.totalViews.toLocaleString() }}
              <span class="text-xs font-bold opacity-40 mb-2 uppercase">Lượt</span>
            </div>
            <div class="mt-5 flex items-center justify-center lg:justify-start gap-3">
              <span class="trend-badge flex items-center gap-1.5 text-xs font-black px-3 py-1.5 rounded-full">
                <ArrowTrendingUpIcon class="w-3.5 h-3.5" />
                +24.8%
              </span>
              <span class="chart-subtext text-xs opacity-60">Tăng trưởng ổn định</span>
            </div>
          </div>

          <div class="flex-1 w-full min-h-[160px] flex flex-col">
            <div class="flex justify-between text-[11px] chart-subtext font-black mb-6 px-2 opacity-50 uppercase tracking-widest">
              <span v-for="d in ['Thứ 2','Thứ 3','Thứ 4','Thứ 5','Thứ 6','Thứ 7','Chủ nhật']" :key="d" class="hidden sm:inline">{{ d }}</span>
              <span v-for="d in ['T2','T3','T4','T5','T6','T7','CN']" :key="d" class="sm:hidden">{{ d }}</span>
            </div>
            <svg viewBox="0 0 280 90" class="w-full flex-1 filter-drop-shadow" style="overflow: visible">
              <defs>
                <linearGradient id="lineGrad" x1="0%" y1="0%" x2="100%" y2="0%">
                  <stop offset="0%" stop-color="#8b5cf6" />
                  <stop offset="50%" stop-color="#6366f1" />
                  <stop offset="100%" stop-color="#06b6d4" />
                </linearGradient>
                <filter id="glow-line">
                  <feGaussianBlur stdDeviation="3" result="blur"/>
                  <feMerge><feMergeNode in="blur"/><feMergeNode in="SourceGraphic"/></feMerge>
                </filter>
              </defs>
              <path :d="`M0,90 ${chartPoints} L280,90 Z`" fill="url(#lineGrad)" fill-opacity="0.05" />
              <polyline :points="chartPoints" fill="none" stroke="url(#lineGrad)" stroke-width="4" stroke-linecap="round" stroke-linejoin="round" filter="url(#glow-line)" />
              <circle v-for="(pt, i) in chartPoints.split(' ')" :key="i" :cx="pt.split(',')[0]" :cy="pt.split(',')[1]" r="5" class="chart-dot" fill="white" stroke="url(#lineGrad)" stroke-width="3" />
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- ── STAT CARDS ── -->
    <div class="px-8 mb-10 relative z-10">
      <div v-if="isLoading" class="flex justify-center py-12">
        <BaseSpinner size="lg" />
      </div>
      <div v-else class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-5">
        <div v-for="(card, i) in statCards" :key="card.label"
          class="stat-card group relative overflow-hidden rounded-3xl p-6 transition-all duration-500 hover:-translate-y-2"
          :style="`--item-color: ${card.color}; --item-bg: ${card.bg}; --item-glow: ${card.glow}; animation-delay: ${i * 80}ms`">
          <div class="stat-surface absolute inset-0 opacity-10 group-hover:opacity-20 transition-opacity"></div>
          <div class="stat-glow absolute inset-0 opacity-0 group-hover:opacity-100 transition-opacity"></div>
          <div class="relative z-10">
            <div class="stat-icon-box w-12 h-12 rounded-2xl flex items-center justify-center mb-5 transition-all group-hover:rotate-6 group-hover:scale-110 shadow-lg"
              :style="`background: ${card.bg}; color: ${card.color}`">
              <component :is="card.icon" class="w-6 h-6 stroke-[2]" />
            </div>
            <p class="stat-val text-3xl font-black leading-none tracking-tight">{{ card.value.toLocaleString() }}</p>
            <p class="stat-label text-[10px] font-black uppercase tracking-[0.1em] mt-3 opacity-60">{{ card.label }}</p>
            <div v-if="card.trend" class="mt-4 flex items-center gap-1.5" :class="card.trendUp ? 'text-emerald-500' : 'text-rose-500'">
              <div class="w-1.5 h-1.5 rounded-full bg-current animate-pulse"></div>
              <span class="text-[10px] font-black">{{ card.trend }}</span>
              <ArrowTrendingUpIcon class="w-3.5 h-3.5" v-if="card.trendUp" />
            </div>
            <p v-else class="stat-hint text-[10px] font-medium mt-4 whitespace-nowrap">{{ card.hint }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- ── LOWER PANELS ── -->
    <div class="px-8 pb-10 relative z-10">
      <div class="grid grid-cols-1 xl:grid-cols-2 gap-10 h-[700px]">
        <div class="panel flex flex-col rounded-3xl overflow-hidden border backdrop-blur-lg">
          <div class="panel-header featured flex items-center justify-between px-8 py-6 shrink-0 border-b">
            <div class="flex items-center gap-4">
              <div class="w-10 h-10 rounded-xl bg-amber-500/10 flex items-center justify-center shadow-inner">
                <StarIcon class="w-6 h-6 text-amber-500" />
              </div>
              <div>
                <h3 class="font-black text-sm uppercase tracking-tight panel-title">Trang chủ Highlights</h3>
                <p class="text-[10px] opacity-50 font-bold uppercase tracking-widest mt-0.5">Thứ tự hiển thị ({{ featuredApps.length }})</p>
              </div>
            </div>
            <span class="drag-badge text-[9px] font-black uppercase tracking-widest px-3 py-1.5 rounded-lg border">Manual Sort</span>
          </div>
          <div class="flex-1 overflow-y-auto p-6 panel-scrollbar">
            <draggable v-model="featuredApps" item-key="id" @end="onDragEnd" ghost-class="ghost" class="space-y-4">
              <template #item="{ element }">
                <div class="app-row featured-row group flex items-center gap-5 p-5 rounded-2xl cursor-grab active:cursor-grabbing transition-all border">
                  <span class="app-rank w-8 text-center text-sm font-black opacity-20 group-hover:opacity-40">#{{ featuredApps.indexOf(element) + 1 }}</span>
                  <div class="w-14 h-14 rounded-2xl overflow-hidden shrink-0 shadow-lg border-2 border-transparent group-hover:border-amber-400/50 transition-all">
                    <img :src="element.thumbnail" class="w-full h-full object-cover" />
                  </div>
                  <div class="flex-1 min-w-0">
                    <p class="app-name font-black text-base truncate group-hover:text-amber-500 transition-colors">{{ element.name }}</p>
                    <p class="app-domain text-xs font-bold opacity-40 truncate mt-0.5">{{ element.domainName }}</p>
                  </div>
                  <button @click.stop="toggleFeatured(element)" class="btn-remove opacity-0 group-hover:opacity-100 p-2.5 rounded-xl transition-all hover:scale-110 active:scale-90">
                    <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>
                </div>
              </template>
            </draggable>
          </div>
        </div>

        <div class="panel flex flex-col rounded-3xl overflow-hidden border backdrop-blur-lg">
          <div class="panel-header flex items-center justify-between px-8 py-6 shrink-0 border-b">
            <div class="flex items-center gap-4">
              <div class="w-10 h-10 rounded-xl bg-indigo-500/10 flex items-center justify-center shadow-inner">
                <CubeIcon class="w-6 h-6 text-indigo-500" />
              </div>
              <div>
                <h3 class="font-black text-sm uppercase tracking-tight panel-title">Tất cả sản phẩm</h3>
                <p class="text-[10px] opacity-50 font-bold uppercase tracking-widest mt-0.5">Kho lưu trữ ({{ otherApps.length }})</p>
              </div>
            </div>
          </div>
          <div class="flex-1 overflow-y-auto p-6 panel-scrollbar">
            <div class="space-y-3">
              <div v-for="app in otherApps" :key="app.id" class="app-row group flex items-center gap-5 p-4 rounded-2xl border transition-all">
                 <div class="w-12 h-12 rounded-2xl overflow-hidden shrink-0 shadow-md border group-hover:border-indigo-500/30 transition-all">
                    <img v-if="app.thumbnail" :src="app.thumbnail" class="w-full h-full object-cover" />
                    <div v-else class="w-full h-full bg-indigo-500/5 flex items-center justify-center">
                      <CubeIcon class="w-6 h-6 text-indigo-300" />
                    </div>
                  </div>
                  <div class="flex-1 min-w-0">
                    <p class="app-name font-black text-sm truncate group-hover:text-indigo-500 transition-colors">{{ app.name }}</p>
                    <div class="flex items-center gap-2 mt-1">
                      <p class="app-domain text-xs font-bold opacity-30 truncate">{{ app.domainName }}</p>
                      <span v-if="app.approvalStatus !== 'APPROVED'" class="status-pill text-[9px] font-black uppercase px-2 py-0.5 rounded-full border">
                        {{ app.approvalStatus }}
                      </span>
                    </div>
                  </div>
                  <button @click="toggleFeatured(app)" class="btn-pin hover:scale-105 active:scale-95 transition-all text-[10px] font-black tracking-widest uppercase px-5 py-2.5 rounded-xl shadow-md">
                    Ghim Hot
                  </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* ── Layout & Blobs ── */
.dashboard-root { position: relative; overflow-x: hidden; }
.root-light { background: #ffffff; }
.root-dark { background: #05040d; }

.dashboard-blobs { position: fixed; inset: 0; pointer-events: none; z-index: 0; }
.blob { position: absolute; filter: blur(120px); border-radius: 50%; opacity: 0.15; }
.blob-1 { width: 600px; height: 600px; background: #7c3aed; top: -200px; right: -100px; }
.blob-2 { width: 500px; height: 500px; background: #3b82f6; bottom: -100px; left: -100px; }
.blob-3 { width: 400px; height: 400px; background: #ec4899; top: 40%; left: 30%; filter: blur(150px); opacity: 0.08; }

.root-light .header-title { color: #0f172a; }
.root-dark .header-title { color: white; text-shadow: 0 0 30px rgba(124, 58, 237, 0.4); }
.root-light .header-subtitle { color: #64748b; }
.root-dark .header-subtitle { color: #94a3b8; }

/* ── Buttons ── */
.btn-secondary { background: white; border: 1px solid #e2e8f0; color: #475569; }
.root-dark .btn-secondary { background: rgba(124, 58, 237, 0.05); border-color: rgba(124, 58, 237, 0.2); color: #a78bfa; }

.btn-primary { background: linear-gradient(135deg, #7c3aed, #6366f1); }

/* ── Hero Chart Card ── */
.root-light .chart-card-shell { background: white; border-color: #f1f5f9; box-shadow: 0 20px 50px rgba(0,0,0,0.04); }
.root-dark .chart-card-shell { background: rgba(15, 12, 35, 0.6); border-color: rgba(124, 58, 237, 0.2); box-shadow: 0 30px 100px rgba(0,0,0,0.5); }

.root-light .chart-value { color: #0f172a; }
.root-dark .chart-value { color: white; }
.root-light .chart-label { color: #7c3aed; }
.root-dark .chart-label { color: #c084fc; }

.trend-badge { background: #f0fdf4; color: #16a34a; }
.root-dark .trend-badge { background: rgba(16, 185, 129, 0.15); color: #34d399; }

/* ── Stat Cards ── */
.stat-card { background: white; border: 1px solid #f1f5f9; box-shadow: 0 10px 30px rgba(0,0,0,0.02); animation: slide-up 0.6s cubic-bezier(0.2, 0.8, 0.2, 1) both; }
.root-dark .stat-card { background: rgba(124, 58, 237, 0.03); border-color: rgba(124, 58, 237, 0.1); }
.stat-glow { background: radial-gradient(circle at 50% 0%, var(--item-glow), transparent 70%); }
.root-light .stat-val { color: #0f172a; }
.root-dark .stat-val { color: white; }

/* ── Panels ── */
.root-light .panel { background: white; border-color: #f1f5f9; }
.root-dark .panel { background: rgba(15, 12, 35, 0.4); border-color: rgba(124, 58, 237, 0.1); }
.root-light .panel-header { background: rgba(255,255,255,0.8); border-bottom-color: #f1f5f9; }
.root-dark .panel-header { background: rgba(124, 58, 237, 0.05); border-bottom-color: rgba(124, 58, 237, 0.15); }

.root-light .app-row:hover { background: #f8fafc; border-color: #e2e8f0; }
.root-dark .app-row:hover { background: rgba(124, 58, 237, 0.08); border-color: rgba(124, 58, 237, 0.2); }

.root-light .app-name { color: #1e293b; }
.root-dark .app-name { color: #f1f5f9; }

.btn-remove { background: #fee2e2; color: #dc2626; }
.btn-pin { background: #ede9fe; color: #7c3aed; border: 1px solid #ddd6fe; }
.root-dark .btn-pin { background: rgba(124, 58, 237, 0.15); border-color: rgba(124, 58, 237, 0.4); color: #c084fc; }

/* ── Animations ── */
@keyframes slide-up {
  from { opacity: 0; transform: translateY(30px) scale(0.95); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}
.filter-drop-shadow { filter: drop-shadow(0 0 12px rgba(99, 102, 241, 0.3)); }
</style>