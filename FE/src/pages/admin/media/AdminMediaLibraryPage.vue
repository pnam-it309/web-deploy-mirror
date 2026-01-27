<template>
    <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div class="mb-6 shrink-0">
             <div class="flex items-center justify-between mb-4">
                 <div></div>
                <div class="flex gap-3">
                    <input v-model="searchQuery" @input="debouncedSearch" type="text" placeholder="Tìm kiếm..."
                        class="px-4 py-2 border border-gray-200 dark:border-gray-700 rounded-lg bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-200 focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all outline-none min-w-[240px] shadow-sm" />
                    <select v-model="filterType" @change="fetchMedia"
                        class="px-4 py-2 border border-gray-200 dark:border-gray-700 rounded-lg bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-200 focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition-all outline-none cursor-pointer shadow-sm">
                        <option value="">Tất cả loại</option>
                        <option value="image">Ảnh</option>
                        <option value="video">Video</option>
                        <option value="document">Tài liệu</option>
                    </select>
                </div>
            </div>
            
            <!-- Stats -->
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div class="bg-white dark:bg-gray-800 rounded-xl p-4 border border-gray-200 dark:border-gray-700 shadow-sm flex flex-col items-center justify-center text-center hover:shadow-md transition-shadow">
                    <div class="text-3xl font-bold text-gray-900 dark:text-white">{{ totalCount }}</div>
                    <div class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mt-1">Tổng media</div>
                </div>
                <div class="bg-white dark:bg-gray-800 rounded-xl p-4 border border-gray-200 dark:border-gray-700 shadow-sm flex flex-col items-center justify-center text-center hover:shadow-md transition-shadow">
                    <div class="text-3xl font-bold text-blue-600 dark:text-blue-400">{{ imageCount }}</div>
                    <div class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mt-1">Hình ảnh</div>
                </div>
                <div class="bg-white dark:bg-gray-800 rounded-xl p-4 border border-gray-200 dark:border-gray-700 shadow-sm flex flex-col items-center justify-center text-center hover:shadow-md transition-shadow">
                    <div class="text-3xl font-bold text-purple-600 dark:text-purple-400">{{ videoCount }}</div>
                    <div class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mt-1">Video</div>
                </div>
                <div class="bg-white dark:bg-gray-800 rounded-xl p-4 border border-gray-200 dark:border-gray-700 shadow-sm flex flex-col items-center justify-center text-center hover:shadow-md transition-shadow">
                    <div class="text-3xl font-bold text-green-600 dark:text-green-400">{{ documentCount }}</div>
                    <div class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mt-1">Tài liệu</div>
                </div>
            </div>
        </div>

        <!-- Media Grid -->
        <div class="flex-1 overflow-auto custom-scrollbar p-1">
            <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-6">
                <div v-for="media in mediaList" :key="media.id"
                    class="group relative bg-white dark:bg-gray-800 rounded-xl overflow-hidden border border-gray-200 dark:border-gray-700 hover:border-blue-500 dark:hover:border-blue-500 hover:shadow-lg transition-all cursor-pointer"
                    @click="selectMedia(media)">
                    <div class="aspect-square bg-gray-50 dark:bg-gray-900/50 relative overflow-hidden">
                        <img v-if="media.fileType === 'image'" :src="media.thumbnailUrl || media.fileUrl"
                            :alt="media.originalName" class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110" />
                        <div v-else class="w-full h-full flex items-center justify-center bg-gray-100 dark:bg-gray-800">
                            <svg v-if="media.fileType === 'video'" class="w-12 h-12 text-purple-500" fill="currentColor"
                                viewBox="0 0 20 20">
                                <path
                                    d="M2 6a2 2 0 012-2h6a2 2 0 012 2v8a2 2 0 01-2 2H4a2 2 0 01-2-2V6zM14.553 7.106A1 1 0 0014 8v4a1 1 0 00.553.894l2 1A1 1 0 0018 13V7a1 1 0 00-1.447-.894l-2 1z" />
                            </svg>
                            <svg v-else class="w-12 h-12 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd"
                                    d="M4 4a2 2 0 012-2h4.586A2 2 0 0112 2.586L15.414 6A2 2 0 0116 7.414V16a2 2 0 01-2 2H6a2 2 0 01-2-2V4z"
                                    clip-rule="evenodd" />
                            </svg>
                        </div>
    
                        <!-- Hover Actions -->
                        <div
                            class="absolute inset-0 bg-black/60 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-2 backdrop-blur-sm z-10">
                            <button @click.stop="copyUrl(media.fileUrl)"
                                class="p-2.5 bg-white text-gray-900 rounded-lg hover:bg-gray-100 hover:scale-110 transition-all shadow-lg" title="Sao chép URL">
                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                                </svg>
                            </button>
                            <button @click.stop="deleteMedia(media.id)"
                                class="p-2.5 bg-red-500 text-white rounded-lg hover:bg-red-600 hover:scale-110 transition-all shadow-lg" title="Xoá">
                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                </svg>
                            </button>
                        </div>
                    </div>
                    <div class="px-3 py-3 border-t border-gray-100 dark:border-gray-700 bg-white dark:bg-gray-800">
                        <p class="text-sm font-bold text-gray-900 dark:text-white truncate" :title="media.originalName">{{ media.originalName }}</p>
                        <p class="text-[10px] font-bold text-gray-400 dark:text-gray-500 uppercase mt-0.5">{{ formatSize(media.fileSize) }}</p>
                    </div>
                </div>
            </div>
    
            <!-- Empty State -->
            <div v-if="mediaList.length === 0" class="h-full flex flex-col items-center justify-center text-center p-8 text-gray-400 dark:text-gray-500 italic border-2 border-dashed border-gray-200 dark:border-gray-700 rounded-xl min-h-[300px]">
                <div class="bg-gray-50 dark:bg-gray-800 w-20 h-20 rounded-full flex items-center justify-center mx-auto mb-4 animate-bounce-slow">
                    <svg class="w-10 h-10 text-gray-300 dark:text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                            d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                </div>
                <h3 class="text-lg font-bold text-gray-900 dark:text-white">Chưa có media nào</h3>
                <p class="text-gray-500 dark:text-gray-400 text-sm mt-1">Tải lên media bằng cách kéo thả vào các form.</p>
            </div>
        </div>

        <BaseModal :is-open="!!selectedMedia" title="Chi tiết Media" @close="selectedMedia = null">
            <div v-if="selectedMedia" class="space-y-5">
                <div class="aspect-video bg-gray-50 dark:bg-gray-900 rounded-lg overflow-hidden flex items-center justify-center border border-gray-100 dark:border-gray-700 relative">
                    <img v-if="selectedMedia.fileType === 'image'" :src="selectedMedia.fileUrl" class="max-h-full max-w-full object-contain" />
                     <video v-else-if="selectedMedia.fileType === 'video'" :src="selectedMedia.fileUrl" controls class="max-h-full max-w-full"></video>
                     <div v-else class="text-gray-400 dark:text-gray-500 text-sm font-medium">Preview not available</div>
                </div>

                <div class="grid grid-cols-2 gap-5">
                     <div class="col-span-2">
                        <label class="block text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">Tên file</label>
                        <BaseInput v-model="selectedMedia.originalName" class="dark:text-white" />
                     </div>
                     <div class="col-span-2">
                        <label class="block text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">Tags (cách nhau bởi dấu phẩy)</label>
                        <BaseInput v-model="selectedMedia.tags" placeholder="nature, product, featured..." class="dark:text-white" />
                     </div>
                </div>

                <div class="grid grid-cols-2 gap-4 text-xs font-mono bg-gray-50 dark:bg-gray-800 p-3 rounded-lg border border-gray-100 dark:border-gray-700">
                    <div class="text-gray-600 dark:text-gray-400"><span class="font-bold text-gray-900 dark:text-gray-200">Size:</span> {{ formatSize(selectedMedia.fileSize) }}</div>
                    <div class="text-gray-600 dark:text-gray-400"><span class="font-bold text-gray-900 dark:text-gray-200">Type:</span> {{ selectedMedia.mimeType }}</div>
                    <div class="col-span-2 truncate text-gray-600 dark:text-gray-400"><span class="font-bold text-gray-900 dark:text-gray-200">URL:</span> {{ selectedMedia.fileUrl }}</div>
                </div>
            </div>
            <template #footer>
                <BaseButton variant="outline" @click="selectedMedia = null">Đóng</BaseButton>
                <BaseButton variant="primary" @click="updateMedia">Lưu thay đổi</BaseButton>
            </template>
        </BaseModal>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import request from '@/services/request'
import { toast } from 'vue3-toastify'
import BaseModal from '@/components/base/BaseModal.vue'
import BaseButton from '@/components/base/BaseButton.vue'
import BaseInput from '@/components/base/BaseInput.vue'

interface Media {
    id: string
    fileName: string
    originalName: string
    fileUrl: string
    fileType: string
    mimeType: string
    fileSize: number
    thumbnailUrl: string
    tags: string
    folder: string
    usageCount: number
}

const mediaList = ref<Media[]>([])
const searchQuery = ref('')
const filterType = ref('')
const selectedMedia = ref<Media | null>(null)

const totalCount = computed(() => mediaList.value.length)
const imageCount = computed(() => mediaList.value.filter(m => m.fileType === 'image').length)
const videoCount = computed(() => mediaList.value.filter(m => m.fileType === 'video').length)
const documentCount = computed(() => mediaList.value.filter(m => m.fileType === 'document').length)

let searchTimeout: number | null = null
const debouncedSearch = () => {
    if (searchTimeout) clearTimeout(searchTimeout)
    searchTimeout = window.setTimeout(() => fetchMedia(), 300)
}

const fetchMedia = async () => {
    try {
        let url = '/admin/media-library?page=0&size=50'
        if (filterType.value) url += `&type=${filterType.value}`
        if (searchQuery.value) url += `&search=${searchQuery.value}`

        const res = await request.get(url)
        mediaList.value = res.data.data.content || []
    } catch (error) {
        console.error('Failed to fetch media:', error)
    }
}

const copyUrl = (url: string) => {
    navigator.clipboard.writeText(url)
    toast.success('Đã sao chép URL')
}

const deleteMedia = async (id: string) => {
    if (!confirm('Xoá media này?')) return
    try {
        await request.delete(`/admin/media-library/${id}`)
        toast.success('Đã xoá')
        fetchMedia()
    } catch (error) {
        toast.error('Xoá thất bại')
    }
}

const selectMedia = (media: Media) => {
    selectedMedia.value = media
}

const updateMedia = async () => {
    if (!selectedMedia.value) return;
    try {
        await request.put(`/admin/media-library/${selectedMedia.value.id}`, {
            tags: selectedMedia.value.tags,
            originalName: selectedMedia.value.originalName
        });
        toast.success("Cập nhật thành công!");
        selectedMedia.value = null; // Close modal
        fetchMedia();
    } catch (error) {
        toast.error("Cập nhật thất bại");
    }
}

const formatSize = (bytes: number) => {
    if (!bytes) return '0 B'
    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i]
}

onMounted(fetchMedia)
</script>
