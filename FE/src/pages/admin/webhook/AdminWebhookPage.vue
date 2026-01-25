<template>
    <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div class="mb-6 shrink-0">
             <div class="flex items-center justify-between mb-2">
                 <div>
                    <span class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Dev Tools</span>
                    <h1 class="text-3xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight mt-1">Quản lý Webhooks</h1>
                 </div>
                <button @click="showCreateModal = true"
                    class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-lg transition-all shadow-lg shadow-blue-600/20 text-sm uppercase tracking-wide flex items-center gap-2">
                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" /></svg>
                    Tạo Webhook
                </button>
            </div>
            <p class="text-sm text-gray-500 dark:text-gray-400 mt-1 max-w-2xl">
                Tích hợp với hệ thống bên ngoài. Gửi sự kiện thời gian thực khi có thay đổi trong Catalog.
            </p>
        </div>

        <!-- Webhooks Table -->
        <div class="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm overflow-hidden flex-1">
            <div class="overflow-auto custom-scrollbar h-full">
                <table class="min-w-full divide-y divide-gray-100 dark:divide-gray-700">
                    <thead class="bg-gray-50 dark:bg-gray-900/50 sticky top-0 z-10 backdrop-blur-sm">
                        <tr>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Tên</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                URL</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Events</th>
                            <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Trạng thái</th>
                            <th class="px-6 py-4 text-right text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Thao tác</th>
                        </tr>
                    </thead>
                    <tbody class="divide-y divide-gray-50 dark:divide-gray-700/50">
                        <tr v-for="webhook in webhooks" :key="webhook.id"
                            class="hover:bg-primary/5 dark:hover:bg-primary/5 transition-colors">
                            <td class="px-6 py-4 whitespace-nowrap">
                                <span class="font-bold text-gray-900 dark:text-white">{{ webhook.name }}</span>
                            </td>
                            <td class="px-6 py-4">
                                <code
                                    class="text-xs text-gray-600 dark:text-gray-400 bg-gray-100 dark:bg-gray-700 px-2 py-1 rounded border border-gray-200 dark:border-gray-600 font-mono">
                    {{ webhook.url.length > 50 ? webhook.url.substring(0, 50) + '...' : webhook.url }}
                  </code>
                            </td>
                            <td class="px-6 py-4">
                                <div class="flex flex-wrap gap-1">
                                    <span v-for="event in webhook.events?.split(',') || []" :key="event"
                                        class="px-2 py-0.5 text-[10px] font-bold bg-blue-50 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 rounded border border-blue-100 dark:border-blue-900/50 uppercase tracking-tight">
                                        {{ event }}
                                    </span>
                                </div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <div class="flex items-center gap-2">
                                    <span :class="webhook.isActive
                                        ? 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400'
                                        : 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400'"
                                        class="px-2 py-1 rounded-full text-[10px] font-bold uppercase tracking-wide">
                                        {{ webhook.isActive ? 'Active' : 'Disabled' }}
                                    </span>
                                    <span v-if="webhook.failureCount > 0" class="text-xs font-bold text-red-500 flex items-center gap-0.5" title="Failures">
                                        <svg class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                                        {{ webhook.failureCount }}
                                    </span>
                                </div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-right">
                                <div class="flex items-center justify-end gap-2">
                                    <button @click="toggleActive(webhook)"
                                        class="text-xs font-bold border rounded px-2 py-1 transition-colors uppercase tracking-wide"
                                        :class="webhook.isActive 
                                            ? 'text-orange-600 border-orange-200 hover:bg-orange-50 dark:text-orange-400 dark:border-orange-900/50 dark:hover:bg-orange-900/20'
                                            : 'text-blue-600 border-blue-200 hover:bg-blue-50 dark:text-blue-400 dark:border-blue-900/50 dark:hover:bg-blue-900/20'">
                                        {{ webhook.isActive ? 'Disable' : 'Enable' }}
                                    </button>
                                    <button @click="regenerateSecret(webhook.id)"
                                        class="text-xs font-bold text-yellow-600 border border-yellow-200 hover:bg-yellow-50 dark:text-yellow-400 dark:border-yellow-900/50 dark:hover:bg-yellow-900/20 rounded px-2 py-1 transition-colors uppercase tracking-wide">
                                        Regen Secret
                                    </button>
                                    <button @click="deleteWebhook(webhook.id)"
                                        class="text-xs font-bold text-red-600 border border-red-200 hover:bg-red-50 dark:text-red-400 dark:border-red-900/50 dark:hover:bg-red-900/20 rounded px-2 py-1 transition-colors uppercase tracking-wide">
                                        Xoá
                                    </button>
                                </div>
                            </td>
                        </tr>
                         <tr v-if="webhooks.length === 0">
                            <td colspan="5" class="py-12 text-center text-gray-400 dark:text-gray-500 italic">
                                Chưa có webhook nào
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Create Modal -->
        <Teleport to="body">
            <div v-if="showCreateModal" class="fixed inset-0 z-50 flex items-center justify-center p-4">
                <div class="fixed inset-0 bg-black/60 backdrop-blur-sm transition-opacity" @click="showCreateModal = false"></div>
                <div class="relative bg-white dark:bg-gray-800 rounded-xl shadow-2xl max-w-lg w-full p-6 border border-gray-100 dark:border-gray-700 transform transition-all scale-100">
                    <h3 class="text-xl font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                        <span class="p-2 bg-blue-100 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 rounded-lg">
                            <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" /></svg>
                        </span>
                        Tạo Webhook mới
                    </h3>

                    <div class="space-y-5">
                        <div>
                            <label class="block text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">Tên Webhook</label>
                            <input v-model="newWebhook.name" type="text" placeholder="VD: Discord Notification"
                                class="w-full px-4 py-2 border border-gray-200 dark:border-gray-700 rounded-lg bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all outline-none" />
                        </div>
                        <div>
                            <label class="block text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">Target URL</label>
                            <input v-model="newWebhook.url" type="url" placeholder="https://api.example.com/webhook"
                                class="w-full px-4 py-2 border border-gray-200 dark:border-gray-700 rounded-lg bg-gray-50 dark:bg-gray-900 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all outline-none font-mono text-sm" />
                        </div>
                        <div>
                            <label
                                class="block text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-2">Subscribe Events</label>
                            <div class="grid grid-cols-2 gap-3">
                                <label v-for="event in availableEvents" :key="event" class="flex items-center gap-3 p-3 rounded-lg border border-gray-100 dark:border-gray-700 bg-gray-50 dark:bg-gray-900/50 cursor-pointer hover:border-blue-300 dark:hover:border-blue-700 transition-colors">
                                    <input type="checkbox" :value="event" v-model="selectedEvents" class="w-4 h-4 rounded text-blue-600 focus:ring-blue-500 border-gray-300 bg-white dark:bg-gray-800" />
                                    <span class="text-xs font-bold text-gray-700 dark:text-gray-300">{{ event }}</span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="flex justify-end gap-3 mt-8 pt-4 border-t border-gray-100 dark:border-gray-700">
                        <button @click="showCreateModal = false"
                            class="px-4 py-2 text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white font-bold text-sm transition-colors">
                            Huỷ bỏ
                        </button>
                        <button @click="createWebhook"
                            class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-bold text-sm shadow-lg shadow-blue-600/20 transition-all transform active:scale-95">
                            Tạo Webhook
                        </button>
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

interface Webhook {
    id: string
    name: string
    url: string
    secret: string
    events: string
    isActive: boolean
    failureCount: number
    lastTriggeredAt: number
}

const webhooks = ref<Webhook[]>([])
const showCreateModal = ref(false)
const newWebhook = ref({ name: '', url: '' })
const selectedEvents = ref<string[]>([])

const availableEvents = ['APP_CREATED', 'APP_UPDATED', 'APP_DELETED', 'APP_PUBLISHED']

const fetchWebhooks = async () => {
    try {
        const res = await request.get('/admin/webhooks')
        webhooks.value = res.data.data || []
    } catch (error) {
        console.error('Failed to fetch webhooks:', error)
    }
}

const createWebhook = async () => {
    try {
        await request.post('/admin/webhooks', {
            name: newWebhook.value.name,
            url: newWebhook.value.url,
            events: selectedEvents.value.join(','),
            isActive: true
        })
        toast.success('Tạo webhook thành công')
        showCreateModal.value = false
        newWebhook.value = { name: '', url: '' }
        selectedEvents.value = []
        fetchWebhooks()
    } catch (error) {
        toast.error('Tạo thất bại')
    }
}

const toggleActive = async (webhook: Webhook) => {
    try {
        await request.put(`/admin/webhooks/${webhook.id}`, { isActive: !webhook.isActive })
        toast.success('Cập nhật thành công')
        fetchWebhooks()
    } catch (error) {
        toast.error('Cập nhật thất bại')
    }
}

const regenerateSecret = async (id: string) => {
    try {
        const res = await request.post(`/admin/webhooks/${id}/regenerate-secret`)
        toast.success('Secret mới: ' + res.data.data.secret)
        fetchWebhooks()
    } catch (error) {
        toast.error('Có lỗi xảy ra')
    }
}

const deleteWebhook = async (id: string) => {
    if (!confirm('Xoá webhook này?')) return
    try {
        await request.delete(`/admin/webhooks/${id}`)
        toast.success('Đã xoá')
        fetchWebhooks()
    } catch (error) {
        toast.error('Xoá thất bại')
    }
}

onMounted(fetchWebhooks)
</script>
