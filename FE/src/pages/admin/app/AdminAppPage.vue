<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAppStore } from '@/stores/app.store';
import { DomainService } from '@/services/admin/domain.service';

import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BasePagination from '@/components/base/BasePagination.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import BaseModal from '@/components/base/BaseModal.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import AppFilter from '@/components/admin/app/AppFilter.vue';
import AppTable from '@/components/admin/app/AppTable.vue';
import type { Domain } from '@/types/admin.types';

const router = useRouter();
const appStore = useAppStore();

// State local
const domains = ref<Domain[]>([]); 
const showDeleteModal = ref(false);
const deleteId = ref<string | null>(null);

// Init Data: Gọi API lấy List App và List Domain (cho filter)
onMounted(async () => {
  await Promise.all([
    appStore.fetchApps(),
    loadDomains()
  ]);
});

const loadDomains = async () => {
  try {
    domains.value = await DomainService.getAll();
  } catch (e) { console.error(e); }
};

// Handlers
const handleFilter = (filter: any) => {
  appStore.setFilter(filter);
};

const handlePageChange = (page: number) => {
  appStore.setFilter({ page });
};

// Điều hướng sang trang Create (AppModify)
const navigateToCreate = () => router.push({ name: 'AdminAppCreate' });

// Điều hướng sang trang Edit (AppModify)
const navigateToEdit = (id: string) => router.push({ name: 'AdminAppEdit', params: { id } });

const confirmDelete = (id: string) => {
  deleteId.value = id;
  showDeleteModal.value = true;
};

const handleDelete = async () => {
  if (deleteId.value) {
    await appStore.deleteApp(deleteId.value);
    showDeleteModal.value = false;
    deleteId.value = null;
  }
};
</script>

<template>
  <div class="p-6 h-full flex flex-col">
    <div class="mb-6 shrink-0">
      <BaseBreadcrumb :items="[{ label: 'Admin', to: '/admin' }, { label: 'Quản lý Dự án' }]" />
      <h1 class="text-2xl font-bold text-dark font-serif uppercase tracking-wide">Danh sách Dự án</h1>
    </div>

    <div class="shrink-0">
      <AppFilter 
        :domains="domains" 
        @filter="handleFilter" 
        @create="navigateToCreate" 
      />
    </div>

    <div v-if="appStore.isLoading" class="flex-1 flex justify-center items-center">
      <BaseSpinner size="lg" />
    </div>

    <div v-else class="flex-1 flex flex-col min-h-0">
      <div class="flex-1 overflow-auto custom-scrollbar bg-white rounded-sm border border-gray-100">
        <AppTable 
          :apps="appStore.apps" 
          @edit="navigateToEdit" 
          @delete="confirmDelete" 
        />
      </div>

      <div class="mt-4 shrink-0 flex justify-end">
        <BasePagination 
          :current-page="appStore.filterParams.page" 
          :total-pages="appStore.totalPages" 
          @change="handlePageChange" 
        />
      </div>
    </div>

    <BaseModal 
      :is-open="showDeleteModal" 
      title="Xác nhận xoá" 
      @close="showDeleteModal = false"
    >
      <p class="text-gray-600">Bạn có chắc chắn muốn xoá dự án này không?</p>
      <template #footer>
        <BaseButton variant="outline" @click="showDeleteModal = false">Huỷ bỏ</BaseButton>
        <BaseButton variant="danger" @click="handleDelete">Đồng ý Xoá</BaseButton>
      </template>
    </BaseModal>
  </div>
</template>