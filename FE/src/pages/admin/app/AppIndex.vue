<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAppStore } from '@/stores/app.store'; // Store bạn đã tạo
import { DomainService } from '@/services/admin/domain.service';

// Import Components Levels 1 & 2
import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BasePagination from '@/components/base/BasePagination.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import BaseModal from '@/components/base/BaseModal.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import AppFilter from '@/components/admin/app/AppFilter.vue';
import AppTable from '@/components/admin/app/AppTable.vue';
import { Domain } from '@/types/admin.types';

const router = useRouter();
const appStore = useAppStore();

const domains = ref<Domain[]>([]); 
const showDeleteModal = ref(false);
const deleteId = ref<string | null>(null);

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
  appStore.setFilter(filter); // Store tự gọi fetchApps
};

const handlePageChange = (page: number) => {
  appStore.setFilter({ page });
};

const navigateToCreate = () => router.push('/admin/apps/create');
const navigateToEdit = (id: string) => router.push(`/admin/apps/${id}`);

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
  <div class="p-6 min-h-screen bg-gray-50/50">
    <div class="mb-6">
      <BaseBreadcrumb :items="[{ label: 'Admin', to: '/admin' }, { label: 'Quản lý Dự án' }]" />
      <h1 class="text-2xl font-bold text-dark font-serif uppercase tracking-wide">Danh sách Dự án</h1>
      <p class="text-gray-500 text-sm mt-1">Quản lý các sản phẩm Showcase của sinh viên</p>
    </div>

    <AppFilter 
      :domains="domains" 
      @filter="handleFilter" 
      @create="navigateToCreate" 
    />

    <div v-if="appStore.isLoading" class="py-12 flex justify-center">
      <BaseSpinner size="lg" />
    </div>

    <div v-else>
      <AppTable 
        :apps="appStore.apps" 
        @edit="navigateToEdit" 
        @delete="confirmDelete" 
      />

      <div class="mt-6 flex justify-end">
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
      <p class="text-gray-600">Bạn có chắc chắn muốn xoá dự án này không? Hành động này sẽ ẩn dự án khỏi trang chủ.</p>
      <template #footer>
        <BaseButton variant="outline" @click="showDeleteModal = false">Huỷ bỏ</BaseButton>
        <BaseButton variant="danger" @click="handleDelete">Đồng ý Xoá</BaseButton>
      </template>
    </BaseModal>
  </div>
</template>