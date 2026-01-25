<template>
    <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div class="mb-6 shrink-0">
             <div class="flex items-center justify-between mb-2">
                 <div>
                    <span class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Storage</span>
                    <h1 class="text-3xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight mt-1">Thùng rác</h1>
                 </div>
                <button v-if="items.length > 0" @click="emptyTrash"
                    class="px-4 py-2 bg-red-600 hover:bg-red-700 text-white font-bold rounded-lg transition-all shadow-lg shadow-red-600/20 text-sm uppercase tracking-wide flex items-center gap-2">
                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                    Dọn sạch thùng rác
                </button>
            </div>
            <p class="text-sm text-gray-500 dark:text-gray-400 mt-1 max-w-2xl flex items-center gap-2">
                <svg class="w-4 h-4 text-orange-500" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                Các mục sẽ bị xoá vĩnh viễn sau 30 ngày kể từ ngày chuyển vào thùng rác.
            </p>
        </div>

        <!-- Empty State -->
        <div v-if="items.length === 0" class="flex-1 flex flex-col items-center justify-center p-12 text-center bg-white dark:bg-gray-800 rounded-xl border border-dashed border-gray-200 dark:border-gray-700 m-6 mb-0">
            <div class="w-24 h-24 bg-green-50 dark:bg-green-900/20 rounded-full flex items-center justify-center mb-6">
                 <svg class="w-12 h-12 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                        d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-900 dark:text-white mb-2">Thùng rác trống</h3>
            <p class="text-gray-500 dark:text-gray-400">Không có mục nào bị xoá gần đây.</p>
        </div>

        <!-- Trash Items -->
        <div v-else class="flex-1 overflow-auto custom-scrollbar">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 pb-6">
                <div v-for="item in items" :key="item.id"
                    class="bg-white dark:bg-gray-800 rounded-xl p-4 border border-gray-200 dark:border-gray-700 shadow-sm hover:shadow-md transition-all group flex flex-col">
                    <div class="flex gap-4 mb-4">
                        <div class="relative w-20 h-20 rounded-lg overflow-hidden shrink-0">
                            <img :src="item.thumbnail || 'https://placehold.co/100x60'"
                                class="w-full h-full object-cover transition-transform group-hover:scale-110" />
                            <div class="absolute inset-0 bg-black/10 group-hover:bg-black/0 transition-colors"></div>
                        </div>
                        <div class="flex-1 min-w-0">
                            <h3 class="font-bold text-gray-900 dark:text-white mb-1 truncate text-lg" :title="item.name">{{ item.name }}</h3>
                            <div class="space-y-1">
                                <p class="text-xs text-gray-500 dark:text-gray-400 flex items-center gap-1">
                                    <span class="w-1.5 h-1.5 rounded-full bg-red-400"></span>
                                    Xoá: {{ formatDateNVV(item.deletedAt) }}
                                </p>
                                <p class="text-xs font-bold text-orange-500 flex items-center gap-1 bg-orange-50 dark:bg-orange-900/20 w-max px-2 py-0.5 rounded">
                                    <svg class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                                    Còn {{ item.daysRemaining }} ngày
                                </p>
                            </div>
                        </div>
                    </div>
                
                    <div class="flex gap-3 mt-auto pt-4 border-t border-gray-100 dark:border-gray-700">
                        <button @click="restoreItem(item.id)"
                            class="flex-1 py-2 px-3 bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-400 text-xs font-bold uppercase tracking-wide rounded-lg hover:bg-green-100 dark:hover:bg-green-900/40 transition-colors flex items-center justify-center gap-2">
                            <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" /></svg>
                            Khôi phục
                        </button>
                        <button @click="permanentDelete(item.id)"
                            class="flex-1 py-2 px-3 bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-400 text-xs font-bold uppercase tracking-wide rounded-lg hover:bg-red-100 dark:hover:bg-red-900/40 transition-colors flex items-center justify-center gap-2">
                             <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" /></svg>
                            Xoá
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '@/services/request'
import { toast } from 'vue3-toastify'
import { formatDateNVV } from '@/utils'

interface TrashItem {
    id: string
    name: string
    thumbnail: string
    deletedAt: number
    daysRemaining: number
}

const items = ref<TrashItem[]>([])

const fetchTrash = async () => {
    try {
        const res = await request.get('/admin/trash/apps')
        items.value = res.data.data.content || []
    } catch (error) {
        console.error('Failed to fetch trash:', error)
    }
}

const restoreItem = async (id: string) => {
    try {
        await request.post(`/admin/trash/apps/${id}/restore`)
        toast.success('Khôi phục thành công')
        fetchTrash()
    } catch (error) {
        toast.error('Khôi phục thất bại')
    }
}

const permanentDelete = async (id: string) => {
    if (!confirm('Bạn có chắc muốn xoá vĩnh viễn?')) return
    try {
        await request.delete(`/admin/trash/apps/${id}/permanent`)
        toast.success('Đã xoá vĩnh viễn')
        fetchTrash()
    } catch (error) {
        toast.error('Xoá thất bại')
    }
}

const emptyTrash = async () => {
    if (!confirm('Xoá tất cả các mục trong thùng rác?')) return
    try {
        const res = await request.delete('/admin/trash/apps/empty')
        toast.success(`Đã xoá ${res.data.data.deletedCount} mục`)
        fetchTrash()
    } catch (error) {
        toast.error('Có lỗi xảy ra')
    }
}



onMounted(fetchTrash)
</script>
