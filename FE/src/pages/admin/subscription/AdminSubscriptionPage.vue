<template>
    <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div class="mb-6 shrink-0">
             <div class="flex items-center justify-between mb-2">
                 <div>
                    <span class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Marketing</span>
                    <h1 class="text-3xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight mt-1">Quản lý đăng ký Email</h1>
                 </div>
                <button @click="exportList"
                    class="px-4 py-2 bg-green-600 hover:bg-green-700 text-white font-bold rounded-lg transition-all shadow-lg shadow-green-600/20 text-sm uppercase tracking-wide flex items-center gap-2">
                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" /></svg>
                    Xuất danh sách
                </button>
            </div>
            
            <!-- Stats -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-6">
                <div class="bg-white dark:bg-gray-800 rounded-xl p-5 border border-gray-200 dark:border-gray-700 shadow-sm flex items-center justify-between">
                    <div>
                        <div class="text-3xl font-bold text-gray-900 dark:text-white">{{ totalCount }}</div>
                        <div class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mt-1">Tổng đăng ký</div>
                    </div>
                    <div class="p-3 bg-blue-50 dark:bg-blue-900/20 rounded-lg text-blue-600 dark:text-blue-400">
                         <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" /></svg>
                    </div>
                </div>
                <div class="bg-white dark:bg-gray-800 rounded-xl p-5 border border-gray-200 dark:border-gray-700 shadow-sm flex items-center justify-between">
                    <div>
                        <div class="text-3xl font-bold text-green-600 dark:text-green-400">{{ verifiedCount }}</div>
                        <div class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mt-1">Đã xác nhận</div>
                    </div>
                    <div class="p-3 bg-green-50 dark:bg-green-900/20 rounded-lg text-green-600 dark:text-green-400">
                         <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                    </div>
                </div>
                <div class="bg-white dark:bg-gray-800 rounded-xl p-5 border border-gray-200 dark:border-gray-700 shadow-sm flex items-center justify-between">
                    <div>
                         <div class="text-3xl font-bold text-yellow-500 dark:text-yellow-400">{{ pendingCount }}</div>
                        <div class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mt-1">Chờ xác nhận</div>
                    </div>
                    <div class="p-3 bg-yellow-50 dark:bg-yellow-900/20 rounded-lg text-yellow-600 dark:text-yellow-400">
                         <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                    </div>
                </div>
            </div>
        </div>

        <!-- Subscribers Table -->
        <div class="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm overflow-hidden flex-1">
            <div class="overflow-auto custom-scrollbar h-full">
                <table class="min-w-full divide-y divide-gray-100 dark:divide-gray-700">
                    <thead class="bg-gray-50 dark:bg-gray-900/50 sticky top-0 z-10 backdrop-blur-sm">
                        <tr>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Email</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Tên</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Trạng thái</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Ngày đăng ký</th>
                            <th class="px-6 py-4 text-right text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Thao tác</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-50 dark:divide-gray-700/50">
                        <tr v-for="sub in subscribers" :key="sub.id" class="hover:bg-primary/5 dark:hover:bg-primary/5 transition-colors">
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-bold text-gray-900 dark:text-white">
                                {{ sub.email }}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600 dark:text-gray-300">
                                {{ sub.name || '-' }}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <span :class="sub.isVerified
                                    ? 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400'
                                    : 'bg-yellow-100 text-yellow-700 dark:bg-yellow-900/30 dark:text-yellow-400'"
                                    class="px-2 py-1 rounded-full text-[10px] font-bold uppercase tracking-wide">
                                    {{ sub.isVerified ? 'Đã xác nhận' : 'Chờ xác nhận' }}
                                </span>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-xs font-mono text-gray-500 dark:text-gray-400">
                                {{ formatDateNVV(sub.subscribedAt) }}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-right">
                                <button @click="deleteSubscriber(sub.id)" class="text-xs font-bold text-red-600 hover:text-red-700 dark:text-red-400 dark:hover:text-red-300 uppercase tracking-wide border border-red-200 dark:border-red-900/50 px-2 py-1 rounded hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors">
                                    Xoá
                                </button>
                            </td>
                        </tr>
                        <tr v-if="subscribers.length === 0">
                            <td colspan="5" class="py-12 text-center text-gray-400 dark:text-gray-500 italic">
                                Chưa có người đăng ký nào
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import request from '@/services/request'
import { toast } from 'vue3-toastify'
import { formatDateNVV } from '@/utils'

interface Subscriber {
    id: string
    email: string
    name: string
    isVerified: boolean
    subscribedAt: number
}

const subscribers = ref<Subscriber[]>([])

const totalCount = computed(() => subscribers.value.length)
const verifiedCount = computed(() => subscribers.value.filter(s => s.isVerified).length)
const pendingCount = computed(() => subscribers.value.filter(s => !s.isVerified).length)

const fetchSubscribers = async () => {
    try {
        const res = await request.get('/admin/subscriptions')
        subscribers.value = res.data.data || []
    } catch (error) {
        console.error('Failed to fetch subscribers:', error)
    }
}

const deleteSubscriber = async (id: string) => {
    if (!confirm('Xoá subscriber này?')) return
    try {
        await request.delete(`/admin/subscriptions/${id}`)
        toast.success('Đã xoá')
        fetchSubscribers()
    } catch (error) {
        toast.error('Xoá thất bại')
    }
}

const exportList = () => {
    const csv = subscribers.value
        .map(s => `${s.email},${s.name || ''},${s.isVerified ? 'Verified' : 'Pending'}`)
        .join('\n')

    const blob = new Blob(['Email,Name,Status\n' + csv], { type: 'text/csv' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'subscribers.csv'
    a.click()
    URL.revokeObjectURL(url)
}

onMounted(fetchSubscribers)
</script>
