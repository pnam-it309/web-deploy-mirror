<template>
    <AdminLayout>
        <div class="space-y-4">
            <h1 class="text-2xl font-bold text-gray-900 ml-1">Quản lý Ứng dụng</h1>
            <AdminCard class="border-2 border-[#1e293b] rounded-3xl shadow-none">
                <div class="flex justify-between items-center mb-4">
                    <div class="flex gap-2">
                        <div class="w-72">
                            <AdminInput v-model="searchQuery" placeholder="Tìm kiếm ứng dụng..." />
                        </div>
                        <AdminButton @click="loadApps" variant="outline"
                            class="w-12 h-12 p-0 flex items-center justify-center">
                            <ArrowPathIcon class="w-8 h-8" />
                        </AdminButton>
                    </div>
                    <div class="flex gap-2">
                        <AdminButton @click="openModal('create')">Thêm mới</AdminButton>
                    </div>
                </div>
                <AdminTable :columns="columns" :data="filteredApps" actions>
                    <template #cell-thumbnail="{ item }">
                        <img :src="item.thumbnail" class="w-10 h-10 rounded object-cover" />
                    </template>
                    <template #cell-status="{ item }">
                        <AdminBadge :variant="item.status === 'ACTIVE' ? 'success' : 'warning'" class="cursor-pointer"
                            @click="handleToggleStatus(item)">
                            {{ item.status === 'ACTIVE' ? 'Hoạt động' : 'Ẩn' }}
                        </AdminBadge>
                    </template>
                    <template #actions="{ item }">
                        <div class="flex gap-2 justify-end">
                            <AdminButton variant="ghost" size="sm" @click="handleEdit(item)">
                                <PencilIcon class="w-5 h-5 text-indigo-600" />
                            </AdminButton>
                            <AdminButton variant="ghost" size="sm" @click="handleDelete(item)">
                                <TrashIcon class="w-5 h-5 text-red-500" />
                            </AdminButton>
                        </div>
                    </template>
                </AdminTable>
            </AdminCard>
        </div>
    </AdminLayout>
</template>

<script setup lang="ts">
import AdminLayout from '@/layouts/admin/AdminLayout.vue'
import AdminCard from '@/components/admin/AdminCard.vue'
import AdminTable from '@/components/admin/dashboard/AdminTable.vue'
import AdminButton from '@/components/admin/AdminButton.vue'
import AdminInput from '@/components/admin/AdminInput.vue'
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getApps, deleteApp, toggleAppStatus } from '@/services/admin/app.service'
import { ArrowPathIcon, PencilIcon, TrashIcon } from '@heroicons/vue/24/outline'
import AdminBadge from '@/components/admin/AdminBadge.vue'
import { toast } from 'vue3-toastify'

const router = useRouter()
const apps = ref<any[]>([])
const searchQuery = ref('')

const filteredApps = computed(() => {
    if (!searchQuery.value) return apps.value
    const lowerQuery = searchQuery.value.toLowerCase()
    return apps.value.filter((app: any) =>
        (app.name && app.name.toLowerCase().includes(lowerQuery)) ||
        (app.sku && app.sku.toLowerCase().includes(lowerQuery))
    )
})

const columns = [
    { key: 'thumbnail', label: 'Hình ảnh' },
    { key: 'name', label: 'Tên ứng dụng' },
    { key: 'sku', label: 'SKU' },
    { key: 'price', label: 'Giá' },
    { key: 'viewCount', label: 'Lượt xem', align: 'center' },
    { key: 'status', label: 'Trạng thái', align: 'center' },
    { key: 'actions', label: 'Thao tác', align: 'right' }
]

const loadApps = async () => {
    try {
        apps.value = await getApps()
    } catch (e) {
        console.error(e)
        toast.error('Không thể tải danh sách ứng dụng')
    }
}

const handleEdit = (item: any) => {
    router.push({ name: 'admin-apps-edit', params: { id: item.id } })
}

const handleDelete = async (item: any) => {
    if (!confirm(`Bạn có chắc muốn xóa ứng dụng "${item.name}"?`)) return
    try {
        await deleteApp(item.id)
        toast.success('Xóa thành công')
        await loadApps()
    } catch (e) {
        toast.error('Lỗi khi xóa')
    }
}

const handleToggleStatus = async (item: any) => {
    try {
        await toggleAppStatus(item.id)
        toast.success('Thay đổi trạng thái thành công')
        await loadApps()
    } catch (e) {
        toast.error('Lỗi khi thay đổi trạng thái')
    }
}

const openModal = (mode: string) => {
    if (mode === 'create') {
        router.push({ name: 'admin-apps-create' })
    }
}

onMounted(() => {
    loadApps()
})
</script>
