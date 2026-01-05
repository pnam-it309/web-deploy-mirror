<template>
    <AdminLayout>
        <div class="space-y-6">
            <div class="space-y-6">

                <!-- Top Stats Row -->
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    <SummaryStatsCard title="Lĩnh vực" :value="stats.totalDomains" :icon="FolderIcon" color="indigo" />
                    <SummaryStatsCard title="Sản phẩm" :value="stats.totalApps" :icon="CubeIcon" color="emerald" />
                    <SummaryStatsCard title="Chức năng" :value="stats.totalFeatures" :icon="PuzzlePieceIcon"
                        color="amber" trend="up" trend-value="12%" />
                    <SummaryStatsCard title="Người dùng" :value="stats.totalActiveUsers" :icon="UsersIcon"
                        color="rose" />
                </div>


                <!-- 2. Charts Section -->
                <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
                    <!-- Products by Domain -->
                    <AdminCard title="Sản phẩm theo Lĩnh vực">
                        <div class="h-64 flex justify-center">
                            <AdminChart type="doughnut" :data="domainChartData" :options="doughnutOptions" />
                        </div>
                    </AdminCard>

                    <!-- Products by Status -->
                    <AdminCard title="Trạng thái Sản phẩm">
                        <div class="h-64 flex justify-center">
                            <AdminChart type="pie" :data="statusChartData" :options="doughnutOptions" />
                        </div>
                    </AdminCard>
                </div>

                <!-- 3. Recent Transactions & Top Products -->
                <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

                    <!-- Top Viewed Apps (Bar Chart) - Takes 2 columns -->
                    <div class="lg:col-span-2">
                        <AdminCard title="Top sản phẩm xem nhiều nhất">
                            <div class="h-64">
                                <AdminChart type="bar" :data="topAppsData" :options="barOptions" />
                            </div>
                        </AdminCard>
                    </div>

                    <div class="lg:col-span-1">
                        <div
                            class="bg-indigo-50 rounded-xl p-6 h-full flex flex-col justify-center items-center text-center">
                            <h3 class="text-indigo-900 font-semibold text-lg mb-2">Quản lý nội dung</h3>
                            <p class="text-indigo-600 text-sm mb-4">Truy cập nhanh vào quản lý sản phẩm</p>
                            <button @click="$router.push({ name: 'admin-apps' })"
                                class="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition">
                                Xem danh sách
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </AdminLayout>
</template>

<script setup lang="ts">
import AdminLayout from '@/layouts/admin/AdminLayout.vue'
import AdminCard from '@/components/admin/AdminCard.vue'
import AdminChart from '@/components/admin/dashboard/AdminChart.vue'
import SummaryStatsCard from '@/components/admin/dashboard/SummaryStatsCard.vue'
import { FolderIcon, CubeIcon, PuzzlePieceIcon, UsersIcon } from '@heroicons/vue/24/outline'

import { ref, onMounted, computed } from 'vue'
import { getDashboardStats, DashboardStats } from '@/services/admin/dashboard.service'

const stats = ref<DashboardStats>({
    totalDomains: 0,
    totalApps: 0,
    totalFeatures: 0,
    totalActiveUsers: 0
})

onMounted(async () => {
    try {
        const data = await getDashboardStats()
        stats.value = data
    } catch (error) {
        console.error('Failed to fetch dashboard stats', error)
    }
})

const domainChartData = computed(() => ({
    labels: Object.keys(stats.value.domainDistribution || {}),
    datasets: [{
        data: Object.values(stats.value.domainDistribution || {}),
        backgroundColor: ['#4f46e5', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'],
        hoverOffset: 4
    }]
}));

const statusChartData = computed(() => ({
    labels: Object.keys(stats.value.statusDistribution || {}),
    datasets: [{
        data: Object.values(stats.value.statusDistribution || {}),
        backgroundColor: ['#94a3b8', '#fbbf24', '#22c55e', '#ef4444'], // Draft, Pending, Approved, Rejected
        hoverOffset: 4
    }]
}));

// Top Viewed Apps (Bar Chart)
const topAppsData = computed(() => ({
    labels: (stats.value.topViewedApps || []).map(a => a.name.substring(0, 15) + '...'),
    datasets: [{
        label: 'Lượt xem',
        data: (stats.value.topViewedApps || []).map(a => a.viewCount),
        backgroundColor: '#6366f1',
        borderRadius: 4
    }]
}));



const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: {
            display: false
        }
    },
    scales: {
        y: {
            beginAtZero: true,
            grid: {
                display: true,
                drawBorder: false,
                color: '#f3f4f6'
            }
        },
        x: {
            grid: {
                display: false
            }
        }
    }
}

const barOptions = chartOptions;

const doughnutOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: {
            position: 'bottom' as const
        }
    }
}

// Removed unused Mock Data for Table
</script>
<!-- HMR Refresh Fix -->
