<template>
    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
        <div class="bg-white rounded-3xl w-full max-w-2xl overflow-hidden shadow-2xl border-2 border-indigo-900">
            <div class="p-6 border-b border-gray-100 flex justify-between items-center bg-sky-50">
                <h2 class="text-xl font-bold text-gray-900">
                    {{ mode === 'create' ? 'Thêm Chức năng mới' : 'Chỉnh sửa Chức năng' }}</h2>
                <button @click="close" class="text-gray-400 hover:text-gray-600 transition-colors">
                    <XMarkIcon class="w-6 h-6" />
                </button>
            </div>

            <form @submit.prevent="handleSubmit" class="p-6 space-y-4 max-h-[70vh] overflow-y-auto">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div class="space-y-2 col-span-2">
                        <label class="block text-sm font-medium text-gray-700">Sản phẩm <span
                                class="text-red-500">*</span></label>
                        <select v-model="form.appId"
                            class="w-full h-12 px-4 rounded-xl border-2 border-gray-100 focus:border-indigo-500 transition-colors outline-none bg-white"
                            required>
                            <option value="" disabled>Chọn sản phẩm...</option>
                            <option v-for="app in apps" :key="app.id" :value="app.id">{{ app.name }}</option>
                        </select>
                    </div>

                    <div class="space-y-2 col-span-2">
                        <label class="block text-sm font-medium text-gray-700">Tên chức năng <span
                                class="text-red-500">*</span></label>
                        <AdminInput v-model="form.name" placeholder="Nhập tên chức năng..." required />
                    </div>

                    <div class="space-y-2">
                        <label class="block text-sm font-medium text-gray-700">Thứ tự hiển thị</label>
                        <AdminInput v-model.number="form.sortOrder" type="number" placeholder="0" />
                    </div>

                    <div class="space-y-2">
                        <label class="block text-sm font-medium text-gray-700">Trạng thái</label>
                        <select v-model="form.status"
                            class="w-full h-12 px-4 rounded-xl border-2 border-gray-100 focus:border-indigo-500 transition-colors outline-none bg-white">
                            <option value="ACTIVE">Hoạt động</option>
                            <option value="INACTIVE">Ẩn</option>
                        </select>
                    </div>

                    <div class="space-y-2 col-span-2 text-center">
                        <label class="block text-sm font-medium text-gray-700 mb-2">Ảnh minh họa (Preview)</label>
                        <div class="flex flex-col items-center gap-2">
                            <input type="file" ref="fileInput" class="hidden" @change="handleFileChange"
                                accept="image/*" />
                            <div class="w-full h-40 rounded-xl border-2 border-dashed border-gray-200 flex items-center justify-center cursor-pointer hover:bg-gray-50 transition-colors overflow-hidden relative group"
                                @click="fileInput?.click()">
                                <img v-if="previewUrl" :src="previewUrl" class="w-full h-full object-cover" />
                                <div v-else class="flex flex-col items-center text-gray-400">
                                    <PhotoIcon class="w-10 h-10" />
                                    <span>Click để tải ảnh lên</span>
                                </div>
                                <div v-if="previewUrl"
                                    class="absolute inset-0 bg-black/40 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                                    <PhotoIcon class="w-8 h-8 text-white" />
                                </div>
                            </div>
                            <p class="text-xs text-gray-400">Dung lượng tối đa 2MB. Hỗ trợ JPG, PNG.</p>
                        </div>
                    </div>

                    <div class="space-y-2 col-span-2">
                        <label class="block text-sm font-medium text-gray-700">Mô tả</label>
                        <textarea v-model="form.description" rows="4"
                            class="w-full p-4 rounded-xl border-2 border-gray-100 focus:border-indigo-500 transition-colors outline-none resize-none"
                            placeholder="Mô tả chi tiết về chức năng..."></textarea>
                    </div>
                </div>
            </form>

            <div class="p-6 border-t border-gray-100 flex justify-end gap-3 bg-gray-50">
                <AdminButton variant="outline" @click="close" :disabled="loading">Hủy</AdminButton>
                <AdminButton @click="handleSubmit" :loading="loading" :disabled="loading">
                    {{ mode === 'create' ? 'Tạo mới' : 'Cập nhật' }}
                </AdminButton>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch, reactive } from 'vue'
import { XMarkIcon, PhotoIcon } from '@heroicons/vue/24/outline'
import AdminInput from '@/components/admin/AdminInput.vue'
import AdminButton from '@/components/admin/AdminButton.vue'
import { createFeature, updateFeature, uploadFeatureImage } from '@/services/admin/feature.service'
import { getApps } from '@/services/admin/app.service'
import { toast } from 'vue3-toastify'

const props = defineProps<{
    isOpen: boolean
    mode: 'create' | 'edit'
    item?: any
}>()

const emit = defineEmits(['close', 'refresh'])

const loading = ref(false)
const apps = ref<any[]>([])
const fileInput = ref<HTMLInputElement | null>(null)
const selectedFile = ref<File | null>(null)
const previewUrl = ref('')

const form = reactive({
    appId: '',
    name: '',
    description: '',
    sortOrder: 0,
    status: 'ACTIVE'
})

const loadApps = async () => {
    try {
        apps.value = await getApps()
    } catch (e) {
        console.error(e)
    }
}

const handleFileChange = (e: any) => {
    const file = e.target.files[0]
    if (file) {
        if (file.size > 2 * 1024 * 1024) {
            toast.error('Ảnh quá lớn (tối đa 2MB)')
            return
        }
        selectedFile.value = file
        previewUrl.value = URL.createObjectURL(file)
    }
}

watch(() => props.isOpen, (newVal) => {
    if (newVal) {
        loadApps()
        if (props.mode === 'edit' && props.item) {
            Object.assign(form, {
                appId: props.item.appId || '',
                name: props.item.name || '',
                description: props.item.description || '',
                sortOrder: props.item.sortOrder || 0,
                status: props.item.status || 'ACTIVE'
            })
            previewUrl.value = props.item.imagePreview || ''
        } else {
            Object.assign(form, {
                appId: '',
                name: '',
                description: '',
                sortOrder: 0,
                status: 'ACTIVE'
            })
            previewUrl.value = ''
            selectedFile.value = null
        }
    }
})

const close = () => {
    emit('close')
}

const handleSubmit = async () => {
    if (!form.appId) {
        toast.error('Vui lòng chọn sản phẩm')
        return
    }
    if (!form.name) {
        toast.error('Vui lòng nhập tên chức năng')
        return
    }

    loading.value = true
    try {
        let featureId = props.item?.id
        if (props.mode === 'create') {
            // Backend create API might return ID. The current service returns boolean.
            // Looking at feature.service.ts, createFeature returns boolean.
            // If it returns boolean, I might need to know the ID to upload the image...
            // Or the backend handles it differently.
            // Usually create returns the created object or ID.
            // Let's assume it returns boolean and I refresh.
            await createFeature(form)
            toast.success('Thêm chức năng thành công')
        } else {
            await updateFeature(featureId, form)

            if (selectedFile.value) {
                await uploadFeatureImage(featureId, selectedFile.value)
            }
            toast.success('Cập nhật chức năng thành công')
        }
        emit('refresh')
        close()
    } catch (error: any) {
        console.error(error)
        toast.error(error.response?.data?.message || 'Có lỗi xảy ra')
    } finally {
        loading.value = false
    }
}
</script>
