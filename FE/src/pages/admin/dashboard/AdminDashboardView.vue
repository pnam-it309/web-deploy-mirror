<template>
    <AdminLayout>
        <div class="space-y-6">

            <!-- Top Stats Row -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <SummaryStatsCard title="Lĩnh vực" :value="stats.totalDomains" :icon="FolderIcon" color="indigo" />
                <SummaryStatsCard title="Sản phẩm" :value="stats.totalApps" :icon="CubeIcon" color="emerald" />
                <SummaryStatsCard title="Chức năng" :value="stats.totalFeatures" :icon="PuzzlePieceIcon" color="amber"
                    trend="up" trend-value="12%" />
                <SummaryStatsCard title="Người dùng" :value="stats.totalUsers" :icon="UsersIcon" color="rose" />
            </div>


            <!-- 2. Charts Section -->
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <AdminCard title="Hiệu suất bán hàng">
                    <template #actions>
                        <select
                            class="text-sm border-gray-300 rounded-md shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50">
                            <option>7 ngày qua</option>
                            <option>Tháng này</option>
                            <option>Năm nay</option>
                        </select>
                    </template>
                    <div class="h-64">
                        <AdminChart type="line" :data="salesChartData" :options="chartOptions" />
                    </div>
                </AdminCard>

                <AdminCard title="Phương thức thanh toán">
                    <div class="h-64 flex justify-center">
                        <AdminChart type="doughnut" :data="paymentMethodData" :options="doughnutOptions" />
                    </div>
                </AdminCard>
            </div>

            <!-- 3. Recent Transactions & Top Products -->
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <!-- Recent Transactions (Takes 2 columns) -->
                <div class="lg:col-span-2">
                    <AdminCard title="Giao dịch gần đây">
                        <template #actions>
                            <AdminButton variant="ghost" size="sm">Xem tất cả</AdminButton>
                        </template>
                        <AdminTable :columns="transactionColumns" :data="transactions" actions>
                            <template #cell-status="{ item }">
                                <AdminBadge :variant="getStatusVariant(item.status)">
                                    {{ item.status }}
                                </AdminBadge>
                            </template>
                            <template #actions>
                                <AdminButton variant="ghost" size="icon">
                                    <EllipsisVerticalIcon class="w-5 h-5" />
                                </AdminButton>
                            </template>
                        </AdminTable>

                    </AdminCard>
                </div>

                <!-- Revenue Performance (Takes 1 column) -->
                <div class="lg:col-span-1">
                    <AdminCard title="Doanh thu theo vùng">
                        <div class="h-64">
                            <AdminChart type="bar" :data="revenueData" :options="chartOptions" />
                        </div>
                        <div class="mt-4 space-y-3">
                            <div class="flex items-center justify-between text-sm">
                                <span class="text-gray-600">Miền Bắc</span>
                                <span class="font-semibold">45%</span>
                            </div>
                            <div class="flex items-center justify-between text-sm">
                                <span class="text-gray-600">Miền Nam</span>
                                <span class="font-semibold">35%</span>
                            </div>
                            <div class="flex items-center justify-between text-sm">
                                <span class="text-gray-600">Miền Trung</span>
                                <span class="font-semibold">20%</span>
                            </div>
                        </div>
                    </AdminCard>
                </div>
            </div>
        </div>
    </AdminLayout>
</template>

<script setup lang="ts">
import AdminLayout from '@/layouts/admin/AdminLayout.vue'
import SummaryStatsCard from '@/components/admin/dashboard/SummaryStatsCard.vue'
import AdminCard from '@/components/admin/AdminCard.vue'
import AdminChart from '@/components/admin/dashboard/AdminChart.vue'
import AdminTable from '@/components/admin/dashboard/AdminTable.vue'
import AdminButton from '@/components/admin/AdminButton.vue'
import AdminBadge from '@/components/admin/AdminBadge.vue'
import { EllipsisVerticalIcon, FolderIcon, CubeIcon, PuzzlePieceIcon, UsersIcon } from '@heroicons/vue/24/outline'

import { ref, onMounted } from 'vue'
import { getDashboardStats, DashboardStats } from '@/services/admin/dashboard.service'

const stats = ref<DashboardStats>({
    totalDomains: 0,
    totalApps: 0,
    totalFeatures: 0,
    totalUsers: 0
})

onMounted(async () => {
    try {
        const data = await getDashboardStats()
        stats.value = data
    } catch (error) {
        console.error('Failed to fetch dashboard stats', error)
    }
})

const salesChartData = {
    labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
    datasets: [
        {
            label: 'Doanh thu',
            data: [1200, 1900, 3000, 5000, 2300, 3400, 4500], // Mock data
            borderColor: '#4f46e5',
            backgroundColor: 'rgba(79, 70, 229, 0.1)',
            tension: 0.4,
            fill: true
        },
        {
            label: 'Chi phí',
            data: [800, 1200, 2000, 3500, 1800, 2500, 3000], // Mock data
            borderColor: '#ef4444',
            backgroundColor: 'rgba(239, 68, 68, 0.1)',
            tension: 0.4,
            fill: true
        }
    ]
}

const paymentMethodData = {
    labels: ['Thẻ tín dụng', 'Ví điện tử', 'Chuyển khoản'],
    datasets: [
        {
            data: [55, 30, 15],
            backgroundColor: ['#4f46e5', '#10b981', '#f59e0b'],
            hoverOffset: 4
        }
    ]
}

const revenueData = {
    labels: ['Bắc', 'Trung', 'Nam'],
    datasets: [
        {
            label: 'Doanh thu (Triệu VNĐ)',
            data: [450, 200, 350],
            backgroundColor: '#f43f5e',
            borderRadius: 4
        }
    ]
}


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

const doughnutOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: {
            position: 'bottom' as const
        }
    }
}

// Mock Data for Table
const transactionColumns = [
    { key: 'id', label: 'ID Giao dịch' },
    { key: 'customer', label: 'Khách hàng' },
    { key: 'date', label: 'Ngày' },
    { key: 'amount', label: 'Số tiền' },
    { key: 'status', label: 'Trạng thái' },
    { key: 'actions', label: '' }
]

const transactions = [
    { id: '#TRX-001', customer: 'Nguyễn Văn A', date: '2023-10-25', amount: '1,200,000 đ', status: 'Completed' },
    { id: '#TRX-002', customer: 'Trần Thị B', date: '2023-10-25', amount: '500,000 đ', status: 'Pending' },
    { id: '#TRX-003', customer: 'Lê Văn C', date: '2023-10-24', amount: '2,300,000 đ', status: 'Completed' },
    { id: '#TRX-004', customer: 'Phạm Thị D', date: '2023-10-24', amount: '150,000 đ', status: 'Failed' },
    { id: '#TRX-005', customer: 'Hoàng Văn E', date: '2023-10-23', amount: '3,000,000 đ', status: 'Processing' },
]

const getStatusVariant = (status: string) => {
    switch (status) {
        case 'Completed': return 'success'
        case 'Pending': return 'warning'
        case 'Failed': return 'danger'
        case 'Processing': return 'info'
        default: return 'default'
    }
}
</script>