<template>
    <AdminLayout>
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
                        <img v-if="item.icon && item.icon.startsWith('http')" :src="item.icon"
                            class="w-8 h-8 rounded" />
                        <span v-else>{{ item.icon }}</span>
                    </template>
                    <template #actions="{ item }">
                        <div class="flex gap-2 justify-end">
                            <AdminButton variant="ghost" size="sm" @click="openModal('edit', item)">Sửa</AdminButton>
                            <AdminButton variant="ghost" size="sm" class="text-red-600" @click="confirmDelete(item)">Xóa
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
import { getTechnologies, deleteTechnology, Technology } from '@/services/admin/technology.service'
import { ArrowPathIcon } from '@heroicons/vue/24/outline'

const items = ref<Technology[]>([])
const searchQuery = ref('')

const columns = [
    { key: 'name', label: 'Tên công nghệ' },
    { key: 'icon', label: 'Icon' },
]

const filteredItems = computed(() => {
    if (!searchQuery.value) return items.value
    return items.value.filter(d =>
        d.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
})

const loadData = async () => {
    try {
        items.value = await getTechnologies()
    } catch (e) {
        console.error(e)
    }
}

const openModal = (mode: string, item?: any) => {
    alert(`Mở modal ${mode} (Tính năng đang phát triển)`)
}

const confirmDelete = async (item: Technology) => {
    if (confirm(`Bạn có chắc chắn muốn xóa ${item.name}?`)) {
        try {
            await deleteTechnology(item.id)
            await loadData()
        } catch (e) {
            console.error(e)
            alert('Xóa thất bại')
        }
    }
}

onMounted(() => {
    loadData()
})
</script>
