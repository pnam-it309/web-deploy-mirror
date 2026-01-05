<template>
    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
        <div class="bg-white rounded-3xl w-full max-w-2xl overflow-hidden shadow-2xl border-2 border-indigo-900">
            <div class="p-6 border-b border-gray-100 flex justify-between items-center bg-indigo-50">
                <h2 class="text-xl font-bold text-gray-900">
                    {{ mode === 'create' ? 'Thêm Lĩnh vực mới' : 'Chỉnh sửa Lĩnh vực' }}</h2>
                <button @click="close" class="text-gray-400 hover:text-gray-600 transition-colors">
                    <XMarkIcon class="w-6 h-6" />
                </button>
            </div>

            <form @submit.prevent="handleSubmit" class="p-6 space-y-4 max-h-[70vh] overflow-y-auto">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div class="space-y-2 col-span-2">
                        <label class="block text-sm font-medium text-gray-700">Tên lĩnh vực <span
                                class="text-red-500">*</span></label>
                        <AdminInput v-model="form.name" placeholder="Nhập tên lĩnh vực..." required
                            @input="handleNameInput" />
                    </div>

                    <div class="space-y-2">
                        <label class="block text-sm font-medium text-gray-700">Slug</label>
                        <AdminInput v-model="form.slug" placeholder="slug-tu-dong-theo-ten" />
                    </div>

                    <div class="space-y-2">
                        <label class="block text-sm font-medium text-gray-700">Icon (URL hoặc Emoji)</label>
                        <AdminInput v-model="form.icon" placeholder="https://... hoặc 📂" />
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

                    <div class="space-y-2 col-span-2">
                        <label class="block text-sm font-medium text-gray-700">Mô tả</label>
                        <textarea v-model="form.description" rows="3"
                            class="w-full p-4 rounded-xl border-2 border-gray-100 focus:border-indigo-500 transition-colors outline-none resize-none"
                            placeholder="Mô tả ngắn về lĩnh vực..."></textarea>
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
import { XMarkIcon } from '@heroicons/vue/24/outline'
import AdminInput from '@/components/admin/AdminInput.vue'
import AdminButton from '@/components/admin/AdminButton.vue'
import { createDomain, updateDomain } from '@/services/admin/domain.service'
import { toast } from 'vue3-toastify'

const props = defineProps<{
    isOpen: boolean
    mode: 'create' | 'edit'
    item?: any
}>()

const emit = defineEmits(['close', 'refresh'])

const loading = ref(false)
const form = reactive({
    name: '',
    slug: '',
    description: '',
    icon: '',
    sortOrder: 0,
    status: 'ACTIVE'
})

const handleNameInput = () => {
    if (props.mode === 'create') {
        form.slug = form.name
            .toLowerCase()
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .replace(/[đĐ]/g, 'd')
            .replace(/([^0-9a-z-\s])/g, '')
            .replace(/(\s+)/g, '-')
            .replace(/-+/g, '-')
            .replace(/^-+|-+$/g, '')
    }
}

watch(() => props.isOpen, (newVal) => {
    if (newVal) {
        if (props.mode === 'edit' && props.item) {
            Object.assign(form, {
                name: props.item.name || '',
                slug: props.item.slug || '',
                description: props.item.description || '',
                icon: props.item.icon || '',
                sortOrder: props.item.sortOrder || 0,
                status: props.item.status || 'ACTIVE'
            })
        } else {
            Object.assign(form, {
                name: '',
                slug: '',
                description: '',
                icon: '',
                sortOrder: 0,
                status: 'ACTIVE'
            })
        }
    }
})

const close = () => {
    emit('close')
}

const handleSubmit = async () => {
    if (!form.name) {
        toast.error('Vui lòng nhập tên lĩnh vực')
        return
    }

    loading.value = true
    try {
        if (props.mode === 'create') {
            await createDomain(form)
            toast.success('Thêm lĩnh vực thành công')
        } else {
            await updateDomain(props.item.id, form)
            toast.success('Cập nhật lĩnh vực thành công')
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
