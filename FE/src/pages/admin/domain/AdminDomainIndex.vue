<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { DomainService } from '@/services/admin/domain.service';
import type { Domain } from '@/types/admin.types';


import BaseSpinner from '@/components/base/BaseSpinner.vue';
import DomainFilter from '@/components/admin/domain/DomainFilter.vue';
import DomainTable from '@/components/admin/domain/DomainTable.vue';

import { toast } from 'vue3-toastify';

const router = useRouter();
const items = ref<Domain[]>([]);
const isLoading = ref(false);

const loadData = async () => {
  isLoading.value = true;
  try {
    const res = await DomainService.getAll();
    items.value = res.sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0));
  } catch (error) {
    console.error("Failed to load domains:", error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(loadData);

const handleCreate = () => router.push({ name: 'AdminDomainCreate' });
const handleEdit = (item: Domain) => router.push({ name: 'AdminDomainEdit', params: { id: item.id } });

const handleDelete = async (id: string) => {
  if (confirm('Bạn có chắc muốn xoá?')) {
    try {
      await DomainService.deleteDomain(id);
      toast.success("Xoá thành công!");
      loadData();
    } catch (e: any) {
      // Backend returns error if constraints failed
      const msg = e.response?.data?.message || "Không thể xoá. Có thể danh mục này đang chứa sản phẩm.";
      toast.error(msg);
    }
  }
};

const handleReorder = async () => {
  try {
    const payload = items.value.map((item, index) => ({
      id: item.id,
      sortOrder: index + 1
    }));
    await DomainService.updateOrder(payload);
    // Silent success or optional toast
  } catch (e) {
    toast.error("Lỗi cập nhật thứ tự");
  }
};
</script>

<template>
  <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">


    <DomainFilter @create="handleCreate" @search="loadData" />

    <div v-if="isLoading" class="flex-1 flex justify-center items-center">
      <BaseSpinner size="lg" />
    </div>
    <div v-else class="flex-1 overflow-auto custom-scrollbar bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm mt-4">
      <DomainTable v-model:items="items" @edit="handleEdit" @delete="handleDelete" @reorder="handleReorder" />
    </div>
  </div>
</template>