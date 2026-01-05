<template>
    <AdminLayout>
        <div>
            <div class="space-y-4">
                <h1 class="text-2xl font-bold text-gray-900 ml-1">Quản lý Công nghệ</h1>
                <AdminCard class="border-2 border-[#1e293b] rounded-3xl shadow-none">
                    <div class="flex justify-between items-center mb-4">
                        <div class="flex gap-2">
                            <div class="w-72">
                                <AdminInput v-model="searchQuery" placeholder="Tìm kiếm công nghệ..." />
                            </div>
                            <AdminButton @click="loadData" variant="outline"
                                class="w-12 h-12 p-0 flex items-center justify-center">
                                <ArrowPathIcon class="w-8 h-8" />
                            </AdminButton>
                        </div>
                        <AdminButton @click="openModal('create')">Thêm mới</AdminButton>
                    </div>
                    <AdminTable :columns="columns" :data="filteredItems" actions>
                        <template #cell-icon="{ item }">
                            <div
                                class="flex items-center justify-center w-10 h-10 rounded-xl bg-gray-50 border border-gray-100 p-1">
                                <img v-if="item.icon && item.icon.startsWith('http')" :src="item.icon"
                                    class="w-full h-full object-contain" />
                                <span v-else class="text-xs text-gray-400">N/A</span>
                            </div>
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

            <TechnologyModal :is-open="isModalOpen" :mode="modalMode" :item="selectedItem" @close="isModalOpen = false"
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
import TechnologyModal from './TechnologyModal.vue'
import { ref, onMounted, computed } from 'vue'
import { getTechnologies, deleteTechnology, Technology } from '@/services/admin/technology.service'
import { ArrowPathIcon, PencilIcon, TrashIcon } from '@heroicons/vue/24/outline'
import { toast } from 'vue3-toastify'

const items = ref<Technology[]>([])
const searchQuery = ref('')
const isModalOpen = ref(false)
const modalMode = ref<'create' | 'edit'>('create')
const selectedItem = ref<Technology | null>(null)

const columns = [
    { key: 'name', label: 'Tên công nghệ' },
    { key: 'icon', label: 'Icon', align: 'center' },
]

const filteredItems = computed(() => {
    if (!searchQuery.value) return items.value
    const query = searchQuery.value.toLowerCase()
    return items.value.filter(d =>
        d.name.toLowerCase().includes(query)
    )
})

const loadData = async () => {
    try {
        items.value = await getTechnologies()
    } catch (e) {
        console.error(e)
        toast.error('Không thể tải danh sách công nghệ')
    }
}

const openModal = (mode: 'create' | 'edit', item?: Technology) => {
    modalMode.value = mode
    selectedItem.value = item || null
    isModalOpen.value = true
}

const confirmDelete = async (item: Technology) => {
    if (confirm(`Bạn có chắc chắn muốn xóa công nghệ "${item.name}"?`)) {
        try {
            await deleteTechnology(item.id)
            toast.success('Xóa công nghệ thành công')
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
