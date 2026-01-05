<template>
    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
        <div class="bg-white rounded-3xl w-full max-w-md overflow-hidden shadow-2xl border-2 border-indigo-900">
            <div class="p-6 border-b border-gray-100 flex justify-between items-center bg-slate-50">
                <h2 class="text-xl font-bold text-gray-900">
                    {{ mode === 'create' ? 'Thêm Công nghệ mới' : 'Chỉnh sửa Công nghệ' }}</h2>
                <button @click="close" class="text-gray-400 hover:text-gray-600 transition-colors">
                    <XMarkIcon class="w-6 h-6" />
                </button>
            </div>

            <form @submit.prevent="handleSubmit" class="p-6 space-y-6">
                <div class="space-y-2">
                    <label class="block text-sm font-medium text-gray-700">Tên công nghệ <span
                            class="text-red-500">*</span></label>
                    <AdminInput v-model="form.name" placeholder="Ví dụ: VueJS, Spring Boot..." required />
                </div>

                <div class="space-y-2">
                    <label class="block text-sm font-medium text-gray-700">Icon (URL)</label>
                    <AdminInput v-model="form.icon" placeholder="https://cdn.icon.../vue.svg" />
                    <div v-if="form.icon"
                        class="mt-2 flex justify-center p-4 bg-gray-50 rounded-xl border border-dashed border-gray-200">
                        <img :src="form.icon" class="w-16 h-16 object-contain" @error="handleImageError" />
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
import { createTechnology, updateTechnology } from '@/services/admin/technology.service'
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
    icon: ''
})

const handleImageError = (e: any) => {
    e.target.src = 'https://placehold.co/64x64?text=Icon'
}

watch(() => props.isOpen, (newVal) => {
    if (newVal) {
        if (props.mode === 'edit' && props.item) {
            Object.assign(form, {
                name: props.item.name || '',
                icon: props.item.icon || ''
            })
        } else {
            Object.assign(form, {
                name: '',
                icon: ''
            })
        }
    }
})

const close = () => {
    emit('close')
}

const handleSubmit = async () => {
    if (!form.name) {
        toast.error('Vui lòng nhập tên công nghệ')
        return
    }

    loading.value = true
    try {
        if (props.mode === 'create') {
            await createTechnology(form)
            toast.success('Thêm công nghệ thành công')
        } else {
            await updateTechnology(props.item.id, form)
            toast.success('Cập nhật công nghệ thành công')
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
