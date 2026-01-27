<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { TechnologyService } from '@/services/admin/technology.service';
import type { Technology } from '@/types/admin.types';


import BaseSpinner from '@/components/base/BaseSpinner.vue';
import TechnologyFilter from '@/components/admin/technology/TechnologyFilter.vue';
import TechnologyTable from '@/components/admin/technology/TechnologyTable.vue';

const router = useRouter();
const items = ref<Technology[]>([]);
const isLoading = ref(false);

const loadData = async () => {
  isLoading.value = true;
  try {
    items.value = await TechnologyService.getAll();
  } catch (error) {
    console.error("Failed to load technologies:", error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(loadData);

const handleCreate = () => router.push({ name: 'AdminTechnologyCreate' });
const handleEdit = (item: Technology) => router.push({ name: 'AdminTechnologyEdit', params: { id: item.id } });
const handleDelete = async (id: string) => {
  if (confirm('Xoá công nghệ này?')) { await TechnologyService.deleteTechnology(id); loadData(); }
};
</script>

<template>
  <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">

    
    <TechnologyFilter @create="handleCreate" @search="loadData" />
    
    <div v-if="isLoading" class="flex-1 flex justify-center items-center">
      <BaseSpinner size="lg" />
    </div>
    
    <div v-else class="flex-1 overflow-auto custom-scrollbar bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm mt-4">
      <TechnologyTable :items="items" @edit="handleEdit" @delete="handleDelete" />
    </div>
  </div>
</template>