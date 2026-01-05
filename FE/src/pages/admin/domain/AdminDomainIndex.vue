<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { DomainService } from '@/services/admin/domain.service';
import type { Domain } from '@/types/admin.types';

import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import DomainFilter from '@/components/admin/domain/DomainFilter.vue';
import DomainTable from '@/components/admin/domain/DomainTable.vue';

const router = useRouter();
const items = ref<Domain[]>([]);
const isLoading = ref(false);

const loadData = async () => {
  isLoading.value = true;
  try { items.value = await DomainService.getAll(); } 
  finally { isLoading.value = false; }
};

onMounted(loadData);

const handleCreate = () => router.push({ name: 'AdminDomainCreate' });
const handleEdit = (item: Domain) => router.push({ name: 'AdminDomainEdit', params: { id: item.id } });

const handleDelete = async (id: string) => {
  if (confirm('Bạn có chắc muốn xoá?')) {
    await DomainService.deleteDomain(id);
    loadData();
  }
};
</script>

<template>
  <div class="p-6 h-full flex flex-col">
    <div class="mb-6 shrink-0">
      <BaseBreadcrumb :items="[{ label: 'Admin', to: '/admin' }, { label: 'Quản lý Lĩnh vực' }]" />
      <h1 class="text-2xl font-bold text-dark font-serif uppercase">Lĩnh vực</h1>
    </div>
    
    <DomainFilter @create="handleCreate" @search="loadData" />

    <div v-if="isLoading" class="flex-1 flex justify-center items-center"><BaseSpinner size="lg" /></div>
    <div v-else class="flex-1 overflow-auto custom-scrollbar">
      <DomainTable :items="items" @edit="handleEdit" @delete="handleDelete" />
    </div>
  </div>
</template>