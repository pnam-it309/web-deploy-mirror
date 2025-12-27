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
                    <template #cell-isFeatured="{ item }">
                        <input type="checkbox" :checked="item.isFeatured"
                            @change="onConfigChange(item, 'isFeatured', $event)"
                            class="rounded border-gray-300 text-indigo-600 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" />
                    </template>
                    <template #cell-homepageSortOrder="{ item }">
                        <input type="number" :value="item.homepageSortOrder"
                            @change="onConfigChange(item, 'homepageSortOrder', $event)"
                            class="w-20 rounded border-gray-300 text-sm shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" />
                    </template>
                    <template #actions="{ item }">
                        <AdminButton variant="ghost" size="sm">Sửa</AdminButton>
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
import apiClient from '@/services/api/api'
import { PREFIX_API_ADMIN } from '@/constants/url'
import { updateHomepageConfig } from '@/services/admin/app.service'
import { ArrowPathIcon } from '@heroicons/vue/24/outline'

const apps = ref([])
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
    { key: 'viewCount', label: 'Lượt xem' },
    { key: 'isFeatured', label: 'Nổi bật' },
    { key: 'homepageSortOrder', label: 'Thứ tự' },
    { key: 'actions', label: 'Thao tác' }
]

const loadApps = async () => {
    try {
        const response = await apiClient.get(`${PREFIX_API_ADMIN}/apps`)
        apps.value = response.data.data
    } catch (e) {
        console.error(e)
    }
}

const openModal = (mode: string, item?: any) => {
    alert(`Mở modal ${mode} (Tính năng đang phát triển)`)
}



const onConfigChange = (item: any, field: string, event: Event) => {
    const target = event.target as HTMLInputElement
    if (!target) return

    // For sort order, we might get string "123", we need to parse it if calling API expects number,
    // but updateConfig handles payload construction.
    // However, for checkboxes, 'value' property is usually "on", we need 'checked'.
    // For number inputs, 'value' property is the number string.

    let val: any
    if (field === 'isFeatured') {
        val = target.checked
    } else {
        val = target.value
    }

    updateConfig(item, field, val)
}

const updateConfig = async (row: any, field: string, value: any) => {
    try {
        const payload: any = {}
        if (field === 'isFeatured') payload.isFeatured = value
        if (field === 'homepageSortOrder') payload.homepageSortOrder = parseInt(value)

        await updateHomepageConfig(row.id, payload)
        // Optimistic update or reload
        // Since 'row' is a reactive object from 'products', updating it updates the UI
        row[field] = field === 'homepageSortOrder' ? parseInt(value) : value
    } catch (e) {
        console.error(e)
        alert('Cập nhật thất bại')
        // Revert change if needed (complex without original value, but loadProducts refreshes)
    }
}


onMounted(() => {
    loadApps()
})
</script>
