<template>
    <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div class="mb-6 shrink-0">
             <div class="flex items-center justify-between mb-2">
                     <div></div>
                 <div class="flex gap-3">
                    <select v-model="filterEntity" @change="fetchLogs"
                        class="px-4 py-2 border border-gray-200 dark:border-gray-700 rounded-lg bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-200 focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all outline-none cursor-pointer">
                        <option value="">Tất cả loại</option>
                        <option value="App">Sản phẩm</option>
                        <option value="Domain">Lĩnh vực</option>
                        <option value="Technology">Công nghệ</option>
                        <option value="Feature">Chức năng</option>
                    </select>
                </div>
            </div>
        </div>

        <!-- Audit Log Table -->
        <div class="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm overflow-hidden flex-1 flex flex-col">
            <div class="flex-1 overflow-auto custom-scrollbar">
                <table class="min-w-full divide-y divide-gray-100 dark:divide-gray-700">
                    <thead class="bg-gray-50 dark:bg-gray-900/50 sticky top-0 z-10 backdrop-blur-sm">
                        <tr>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Thời gian</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Hành động</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Loại</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Người thực hiện</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Chi tiết</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-50 dark:divide-gray-700/50">
                        <tr v-for="log in logs" :key="log.id" class="hover:bg-primary/5 dark:hover:bg-primary/5 transition-colors">
                            <td class="px-6 py-4 whitespace-nowrap text-xs font-mono text-gray-500 dark:text-gray-400">
                                {{ formatDateTime(log.createdAt) }}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <span :class="getActionClass(log.action)"
                                    class="px-2 py-1 rounded-full text-[10px] font-bold uppercase tracking-wide">
                                    {{ getActionLabel(log.action) }}
                                </span>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm font-bold text-gray-900 dark:text-white">
                                {{ log.entityType }}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600 dark:text-gray-300">
                                {{ log.userName || log.userEmail || 'System' }}
                            </td>
                            <td class="px-6 py-4">
                                <button @click="showDetail(log)" class="text-blue-600 hover:text-blue-700 dark:text-blue-400 dark:hover:text-blue-300 text-xs font-bold uppercase tracking-wide flex items-center gap-1 group">
                                    Xem chi tiết
                                    <svg class="w-3 h-3 transition-transform group-hover:translate-x-1" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" /></svg>
                                </button>
                            </td>
                        </tr>
                         <tr v-if="logs.length === 0">
                            <td colspan="5" class="py-12 text-center text-gray-400 dark:text-gray-500 italic">
                                Không có nhật ký
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            
            <!-- Pagination -->
            <div class="border-t border-gray-100 dark:border-gray-700 bg-gray-50 dark:bg-gray-900/50 p-4 flex justify-end">
                 <div class="flex gap-1">
                    <button v-for="p in totalPages" :key="p" @click="page = p - 1; fetchLogs()" class="w-8 h-8 flex items-center justify-center rounded-lg text-sm font-bold transition-all"
                        :class="page === p - 1 ? 'bg-blue-600 text-white shadow-lg shadow-blue-600/30' : 'bg-white dark:bg-gray-800 text-gray-600 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-700'">
                        {{ p }}
                    </button>
                </div>
            </div>
        </div>

        <!-- Detail Modal -->
        <Teleport to="body">
            <div v-if="selectedLog" class="fixed inset-0 z-50 flex items-center justify-center p-4">
                <div class="fixed inset-0 bg-black/60 backdrop-blur-sm transition-opacity" @click="selectedLog = null"></div>
                <div
                    class="relative bg-white dark:bg-gray-800 rounded-xl shadow-2xl max-w-4xl w-full max-h-[85vh] overflow-hidden flex flex-col border border-gray-100 dark:border-gray-700 transform transition-all scale-100">
                    <div class="flex justify-between items-center p-6 border-b border-gray-100 dark:border-gray-700 bg-gray-50 dark:bg-gray-900/50 shrink-0">
                        <div>
                             <h3 class="text-xl font-bold text-gray-900 dark:text-white">Chi tiết thay đổi</h3>
                             <p class="text-xs text-gray-500 dark:text-gray-400 mt-1 font-mono">ID: {{ selectedLog.id }}</p>
                        </div>
                        <button @click="selectedLog = null" class="w-8 h-8 flex items-center justify-center rounded-full bg-white dark:bg-gray-700 text-gray-500 hover:text-gray-900 dark:hover:text-white shadow-sm hover:shadow transition-all">
                            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>

                    <div class="p-6 overflow-y-auto custom-scrollbar">
                        <div class="grid grid-cols-2 gap-6">
                            <div class="space-y-2">
                                <h4 class="text-xs font-bold text-red-600 dark:text-red-400 uppercase tracking-wider flex items-center gap-2">
                                    <span class="w-2 h-2 rounded-full bg-red-500"></span>
                                    Giá trị cũ
                                </h4>
                                <div class="p-4 bg-red-50 dark:bg-red-900/10 rounded-xl border border-red-100 dark:border-red-900/30 overflow-auto max-h-[400px] custom-scrollbar">
                                    <pre class="text-xs font-mono text-gray-800 dark:text-gray-200 whitespace-pre-wrap leading-relaxed" v-if="selectedLog.oldValue">{{ formatJson(selectedLog.oldValue) }}</pre>
                                    <span v-else class="text-xs text-gray-400 italic">Không có dữ liệu (Create action?)</span>
                                </div>
                            </div>
                            
                            <div class="space-y-2">
                                 <h4 class="text-xs font-bold text-green-600 dark:text-green-400 uppercase tracking-wider flex items-center gap-2">
                                    <span class="w-2 h-2 rounded-full bg-green-500"></span>
                                    Giá trị mới
                                </h4>
                                <div class="p-4 bg-green-50 dark:bg-green-900/10 rounded-xl border border-green-100 dark:border-green-900/30 overflow-auto max-h-[400px] custom-scrollbar">
                                    <pre class="text-xs font-mono text-gray-800 dark:text-gray-200 whitespace-pre-wrap leading-relaxed" v-if="selectedLog.newValue">{{ formatJson(selectedLog.newValue) }}</pre>
                                     <span v-else class="text-xs text-gray-400 italic">Không có dữ liệu (Delete action?)</span>
                                </div>
                            </div>
                        </div>
                        
                        <div class="mt-6 flex gap-6 text-xs text-gray-500 dark:text-gray-400 border-t border-gray-100 dark:border-gray-700 pt-4">
                            <p class="flex items-center gap-2">
                                <span class="font-bold uppercase text-[10px] tracking-wider bg-gray-100 dark:bg-gray-700 px-2 py-0.5 rounded">IP Address</span> 
                                <span class="font-mono">{{ selectedLog.ipAddress || 'N/A' }}</span>
                            </p>
                            <p class="flex items-center gap-2">
                                <span class="font-bold uppercase text-[10px] tracking-wider bg-gray-100 dark:bg-gray-700 px-2 py-0.5 rounded">User Agent</span> 
                                <span class="font-mono truncate max-w-md" :title="selectedLog.userAgent">{{ selectedLog.userAgent || 'N/A' }}</span>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </Teleport>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '@/services/request'
import { formatDateTime } from '@/utils'

interface AuditLog {
    id: string
    entityType: string
    entityId: string
    action: string
    userEmail: string
    userName: string
    oldValue: string
    newValue: string
    ipAddress: string
    userAgent: string
    createdAt: number
}

const logs = ref<AuditLog[]>([])
const page = ref(0)
const totalPages = ref(1)
const filterEntity = ref('')
const selectedLog = ref<AuditLog | null>(null)

const fetchLogs = async () => {
    try {
        let url = `/admin/audit-logs?page=${page.value}&size=20`
        if (filterEntity.value) {
            url = `/admin/audit-logs/entity/${filterEntity.value}?page=${page.value}&size=20`
        }
        const res = await request.get(url)
        logs.value = res.data.data.content || []
        totalPages.value = res.data.data.totalPages || 1
    } catch (error) {
        console.error('Failed to fetch audit logs:', error)
    }
}



const getActionClass = (action: string) => {
    switch (action) {
        case 'CREATE': return 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400'
        case 'UPDATE': return 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-400'
        case 'DELETE': return 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400'
        case 'STATUS_CHANGE': return 'bg-yellow-100 text-yellow-700 dark:bg-yellow-900/30 dark:text-yellow-400'
        default: return 'bg-gray-100 text-gray-700'
    }
}

const getActionLabel = (action: string) => {
    switch (action) {
        case 'CREATE': return 'Tạo mới'
        case 'UPDATE': return 'Cập nhật'
        case 'DELETE': return 'Xoá'
        case 'STATUS_CHANGE': return 'Đổi trạng thái'
        default: return action
    }
}

const showDetail = (log: AuditLog) => {
    selectedLog.value = log
}

const formatJson = (jsonString: string) => {
    try {
        return JSON.stringify(JSON.parse(jsonString), null, 2)
    } catch {
        return jsonString
    }
}

onMounted(fetchLogs)
</script>
