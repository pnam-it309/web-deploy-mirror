<template>
    <AdminLayout>
        <div>
            <div class="space-y-4">
                <h1 class="text-2xl font-bold text-gray-900 ml-1">Quản lý Chức năng</h1>
                <AdminCard class="border-2 border-[#1e293b] rounded-3xl shadow-none">
                    <div class="flex justify-between items-center mb-4">
                        <div class="flex gap-2">
                            <div class="w-72">
                                <AdminInput v-model="searchQuery" placeholder="Tìm kiếm chức năng..." />
                            </div>
                            <AdminButton @click="loadData" variant="outline"
                                class="w-12 h-12 p-0 flex items-center justify-center">
                                <ArrowPathIcon class="w-8 h-8" />
                            </AdminButton>
                        </div>
                        <AdminButton @click="openModal('create')">Thêm mới</AdminButton>
                    </div>
                    <AdminTable :columns="columns" :data="filteredItems" actions>
                        <template #cell-appName="{ item }">
                            <div class="flex flex-col">
                                <span class="font-bold text-gray-900">{{ item.appName }}</span>
                                <span class="text-xs text-gray-400">ID: {{ item.appId }}</span>
                            </div>
                        </template>
                        <template #cell-imagePreview="{ item }">
                            <div
                                class="w-20 h-12 rounded-lg bg-gray-50 border border-gray-100 overflow-hidden flex items-center justify-center">
                                <img v-if="item.imagePreview && item.imagePreview.startsWith('http')"
                                    :src="item.imagePreview" class="w-full h-full object-cover" />
                                <PhotoIcon v-else class="w-6 h-6 text-gray-300" />
                            </div>
                        </template>
                        <template #cell-status="{ item }">
                            <AdminBadge :variant="item.status === 'ACTIVE' ? 'success' : 'warning'">
                                {{ item.status === 'ACTIVE' ? 'Hoạt động' : 'Ẩn' }}
                            </AdminBadge>
                        </template>
                        <template #actions="{ item }">
                            <div class="flex gap-2 justify-end">
                                <AdminButton variant="ghost" size="sm" @click="openModal('edit', item)">
                                    <PencilIcon class="w-5 h-5 text-indigo-600" />
                                </AdminButton>
                                <AdminButton variant="ghost" size="sm" @click="confirmDelete(item)">
                                    <TrashIcon class="w-5 h-5 text-red-500" />
                                </AdminButton>
                            </div>
                        </template>
                    </AdminTable>
                </AdminCard>
            </div>

            <FeatureModal :is-open="isModalOpen" :mode="modalMode" :item="selectedItem" @close="isModalOpen = false"
                @refresh="loadData" />
        </div>
    </AdminLayout>
</template>

<script setup lang="ts">
import AdminLayout from '@/layouts/admin/AdminLayout.vue'
import AdminCard from '@/components/admin/AdminCard.vue'
import AdminTable from '@/components/admin/dashboard/AdminTable.vue'
import AdminButton from '@/components/admin/AdminButton.vue'
import AdminInput from '@/components/admin/AdminInput.vue'
import AdminBadge from '@/components/admin/AdminBadge.vue'
import FeatureModal from './FeatureModal.vue'
import { ref, onMounted, computed } from 'vue'
import { getFeatures, deleteFeature, Feature } from '@/services/admin/feature.service'
import { ArrowPathIcon, PencilIcon, TrashIcon, PhotoIcon } from '@heroicons/vue/24/outline'
import { toast } from 'vue3-toastify'

const items = ref<Feature[]>([])
const searchQuery = ref('')
const isModalOpen = ref(false)
const modalMode = ref<'create' | 'edit'>('create')
const selectedItem = ref<Feature | null>(null)

const columns = [
    { key: 'name', label: 'Tên chức năng' },
    { key: 'appName', label: 'Sản phẩm' },
    { key: 'imagePreview', label: 'Minh họa', align: 'center' },
    { key: 'sortOrder', label: 'Thứ tự', align: 'center' },
    { key: 'status', label: 'Trạng thái', align: 'center' },
]

const filteredItems = computed(() => {
    if (!searchQuery.value) return items.value
    const query = searchQuery.value.toLowerCase()
    return items.value.filter(d =>
        d.name.toLowerCase().includes(query) ||
        (d.appName && d.appName.toLowerCase().includes(query))
    )
})

const loadData = async () => {
    try {
        items.value = await getFeatures()
    } catch (e) {
        console.error(e)
        toast.error('Không thể tải danh sách chức năng')
    }
}

const openModal = (mode: 'create' | 'edit', item?: Feature) => {
    modalMode.value = mode
    selectedItem.value = item || null
    isModalOpen.value = true
}

const confirmDelete = async (item: Feature) => {
    if (confirm(`Bạn có chắc chắn muốn xóa chức năng "${item.name}"?`)) {
        try {
            await deleteFeature(item.id)
            toast.success('Xóa chức năng thành công')
            await loadData()
        } catch (e: any) {
            console.error(e)
            toast.error(e.response?.data?.message || 'Xóa thất bại')
        }
    }
}

onMounted(() => {
    loadData()
})
</script>
