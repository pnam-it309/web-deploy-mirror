<template>
    <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div class="mb-6 shrink-0">
             <div class="flex items-center justify-between mb-2">
                 <div>
                    <span class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Security</span>
                    <h1 class="text-3xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight mt-1">IP Whitelist</h1>
                 </div>
                <button @click="showAddModal = true"
                    class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-lg transition-all shadow-lg shadow-blue-600/20 text-sm uppercase tracking-wide flex items-center gap-2">
                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
                    Thêm IP
                </button>
            </div>
            <p class="text-sm text-gray-500 dark:text-gray-400 mt-1 max-w-2xl">
                Quản lý danh sách IP được phép truy cập vào trang quản trị.
            </p>
        </div>

        <!-- IP List -->
        <div class="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm overflow-hidden flex-1">
            <div class="overflow-auto custom-scrollbar h-full">
                <table class="min-w-full divide-y divide-gray-100 dark:divide-gray-700">
                    <thead class="bg-gray-50 dark:bg-gray-900/50 sticky top-0 z-10 backdrop-blur-sm">
                        <tr>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                IP Address</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Mô tả</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Trạng thái</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Hết hạn</th>
                            <th class="px-6 py-4 text-right text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Thao tác</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-50 dark:divide-gray-700/50">
                        <tr v-for="ip in ipList" :key="ip.id" class="hover:bg-primary/5 dark:hover:bg-primary/5 transition-colors">
                            <td class="px-6 py-4 whitespace-nowrap">
                                <code class="text-xs font-mono font-bold text-blue-600 dark:text-blue-400 bg-blue-50 dark:bg-blue-900/30 px-2 py-1 rounded border border-blue-100 dark:border-blue-900/50">{{ ip.ipAddress }}</code>
                            </td>
                            <td class="px-6 py-4 text-sm text-gray-600 dark:text-gray-300 font-medium">
                                {{ ip.description || '-' }}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <span :class="ip.isActive
                                    ? 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400'
                                    : 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400'"
                                    class="px-2 py-1 rounded-full text-[10px] font-bold uppercase tracking-wide">
                                    {{ ip.isActive ? 'Active' : 'Disabled' }}
                                </span>
                            </td>
                            <td class="px-6 py-4 text-xs font-mono text-gray-500 dark:text-gray-400">
                                {{ ip.expiresAt ? formatDateNVV(ip.expiresAt) : 'Never' }}
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-right">
                                <div class="flex items-center justify-end gap-2">
                                    <button @click="toggleActive(ip)" class="text-xs font-bold border rounded px-2 py-1 transition-colors uppercase tracking-wide"
                                        :class="ip.isActive 
                                            ? 'text-orange-600 border-orange-200 hover:bg-orange-50 dark:text-orange-400 dark:border-orange-900/50 dark:hover:bg-orange-900/20'
                                            : 'text-blue-600 border-blue-200 hover:bg-blue-50 dark:text-blue-400 dark:border-blue-900/50 dark:hover:bg-blue-900/20'">
                                        {{ ip.isActive ? 'Disable' : 'Enable' }}
                                    </button>
                                    <button @click="deleteIp(ip.id)" class="text-xs font-bold text-red-600 border border-red-200 hover:bg-red-50 dark:text-red-400 dark:border-red-900/50 dark:hover:bg-red-900/20 rounded px-2 py-1 transition-colors uppercase tracking-wide">
                                        Xoá
                                    </button>
                                </div>
                            </td>
                        </tr>
                        <tr v-if="ipList.length === 0">
                            <td colspan="5" class="py-12 text-center text-gray-400 dark:text-gray-500 italic">
                                Danh sách IP trống
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Add Modal -->
        <Teleport to="body">
            <div v-if="showAddModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
                <div class="fixed inset-0 bg-black/60 backdrop-blur-sm transition-opacity" @click="showAddModal = false"></div>
                <div class="relative bg-white dark:bg-gray-800 rounded-xl shadow-2xl max-w-md w-full p-6 border border-gray-100 dark:border-gray-700 transform transition-all scale-100">
                    <h3 class="text-xl font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                         <span class="p-2 bg-blue-100 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 rounded-lg">
                            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" /></svg>
                        </span>
                        Thêm IP mới
                    </h3>

                    <div class="space-y-5">
                        <div>
                            <label class="block text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">IP
                                Address</label>
                            <input v-model="newIp.ipAddress" type="text" placeholder="192.168.1.1 hoặc 192.168.1.0/24"
                                class="w-full px-4 py-2 border border-gray-200 dark:border-gray-700 rounded-lg bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all outline-none font-mono text-sm" />
                        </div>
                        <div>
                            <label class="block text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">Mô tả</label>
                            <input v-model="newIp.description" type="text" placeholder="Office IP, VPN, etc."
                                class="w-full px-4 py-2 border border-gray-200 dark:border-gray-700 rounded-lg bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all outline-none" />
                        </div>
                    </div>

                    <div class="flex justify-end gap-3 mt-8 pt-4 border-t border-gray-100 dark:border-gray-700">
                        <button @click="showAddModal = false"
                            class="px-4 py-2 text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white font-bold text-sm transition-colors">Huỷ</button>
                        <button @click="addIp"
                            class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-bold text-sm shadow-lg shadow-blue-600/20 transition-all transform active:scale-95">Thêm</button>
                    </div>
                </div>
            </div>
        </Teleport>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import request from '@/services/request'
import { toast } from 'vue3-toastify'
import { formatDateNVV } from '@/utils'

interface IpWhitelist {
    id: string
    ipAddress: string
    description: string
    isActive: boolean
    expiresAt: number | null
}

const ipList = ref<IpWhitelist[]>([])
const showAddModal = ref(false)
const newIp = ref({ ipAddress: '', description: '' })

const fetchIpList = async () => {
    try {
        const res = await request.get('/admin/ip-whitelist')
        ipList.value = res.data.data || []
    } catch (error) {
        console.error('Failed to fetch IP list:', error)
    }
}

const addIp = async () => {
    try {
        await request.post('/admin/ip-whitelist', newIp.value)
        toast.success('Thêm IP thành công')
        showAddModal.value = false
        newIp.value = { ipAddress: '', description: '' }
        fetchIpList()
    } catch (err: any) {
        toast.error(err.response?.data?.message || 'Thêm thất bại')
    }
}

const toggleActive = async (ip: IpWhitelist) => {
    try {
        await request.put(`/admin/ip-whitelist/${ip.id}`, { isActive: !ip.isActive })
        toast.success('Cập nhật thành công')
        fetchIpList()
    } catch (error) {
        toast.error('Cập nhật thất bại')
    }
}

const deleteIp = async (id: string) => {
    if (!confirm('Xoá IP này?')) return
    try {
        await request.delete(`/admin/ip-whitelist/${id}`)
        toast.success('Đã xoá')
        fetchIpList()
    } catch (error) {
        toast.error('Xoá thất bại')
    }
}



onMounted(fetchIpList)
</script>
