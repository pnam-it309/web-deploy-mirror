<template>
    <AdminLayout>
        <div>
            <div class="space-y-4">
                <h1 class="text-2xl font-bold text-gray-900 ml-1">Quản lý Lĩnh vực</h1>
                <AdminCard class="border-2 border-[#1e293b] rounded-3xl shadow-none">
                    <div class="flex justify-between items-center mb-4">
                        <div class="flex gap-2">
                            <div class="w-72">
                                <AdminInput v-model="searchQuery" placeholder="Tìm kiếm lĩnh vực..." />
                            </div>
                            <AdminButton @click="loadData" variant="outline"
                                class="w-12 h-12 p-0 flex items-center justify-center">
                                <ArrowPathIcon class="w-8 h-8" />
                            </AdminButton>
                        </div>
                        <AdminButton @click="openModal('create')">Thêm mới</AdminButton>
                    </div>
                    <AdminTable :columns="columns" :data="filteredDomains" actions>
                        <template #cell-icon="{ item }">
                            <div
                                class="flex items-center justify-center w-10 h-10 rounded-xl bg-gray-50 border border-gray-100">
                                <img v-if="item.icon && item.icon.startsWith('http')" :src="item.icon"
                                    class="w-7 h-7 object-contain" />
                                <span v-else class="text-xl">{{ item.icon || '📂' }}</span>
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

            <DomainModal :is-open="isModalOpen" :mode="modalMode" :item="selectedItem" @close="isModalOpen = false"
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
import DomainModal from './DomainModal.vue'
import { ref, onMounted, computed } from 'vue'
import { getDomains, deleteDomain, Domain } from '@/services/admin/domain.service'
import { ArrowPathIcon, PencilIcon, TrashIcon } from '@heroicons/vue/24/outline'
import { toast } from 'vue3-toastify'

const domains = ref<Domain[]>([])
const searchQuery = ref('')
const isModalOpen = ref(false)
const modalMode = ref<'create' | 'edit'>('create')
const selectedItem = ref<Domain | null>(null)

const columns = [
    { key: 'name', label: 'Tên lĩnh vực' },
    { key: 'slug', label: 'Slug' },
    { key: 'icon', label: 'Icon', align: 'center' },
    { key: 'sortOrder', label: 'Thứ tự', align: 'center' },
    { key: 'status', label: 'Trạng thái', align: 'center' },
]

const filteredDomains = computed(() => {
    if (!searchQuery.value) return domains.value
    const query = searchQuery.value.toLowerCase()
    return domains.value.filter(d =>
        d.name.toLowerCase().includes(query) ||
        (d.slug && d.slug.toLowerCase().includes(query))
    )
})

const loadData = async () => {
    try {
        domains.value = await getDomains()
    } catch (e) {
        console.error(e)
        toast.error('Không thể tải danh sách lĩnh vực')
    }
}

const openModal = (mode: 'create' | 'edit', item?: Domain) => {
    modalMode.value = mode
    selectedItem.value = item || null
    isModalOpen.value = true
}

const confirmDelete = async (item: Domain) => {
    if (confirm(`Bạn có chắc chắn muốn xóa lĩnh vực "${item.name}"?`)) {
        try {
            await deleteDomain(item.id)
            toast.success('Xóa lĩnh vực thành công')
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
