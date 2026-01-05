<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { FeatureService } from '@/services/admin/feature.service';
import { AppService } from '@/services/admin/app.service';

import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import FeatureFilter from '@/components/admin/feature/FeatureFilter.vue';
import FeatureTable from '@/components/admin/feature/FeatureTable.vue';
import { AppResponse } from '@/types/admin.types';

const router = useRouter();
const features = ref([]);
const apps = ref<AppResponse[]>([]);
const isLoading = ref(false);
const currentAppId = ref(''); // State lưu filter hiện tại
const loadApps = async () => {
  try {
    const res = await AppService.getAll({ size: 100 });
    apps.value = res.content || [];
  } catch (e) {
    console.error("Lỗi load Apps:", e);
  }
};

// 2. Load danh sách Feature (Có support filter)
const loadData = async (appIdFilter?: string) => {
  isLoading.value = true;
  // Cập nhật state currentAppId nếu có truyền vào
  if (appIdFilter !== undefined) {
    currentAppId.value = appIdFilter;
  }

  try {
    if (currentAppId.value) {
      features.value = await FeatureService.getByAppId(currentAppId.value);
    } else {
      features.value = await FeatureService.getAll();
    }
  } catch (e) {
    console.error("Lỗi load Features:", e);
    features.value = [];
  } finally {
    isLoading.value = false;
  }
};
onMounted(async () => {
  await loadApps();
  await loadData();
});
const handleCreate = () => router.push({ name: 'AdminFeatureCreate' });

const handleEdit = (item: any) => {
  router.push({ name: 'AdminFeatureEdit', params: { id: item.id } });
};

const handleDelete = async (id: string) => {
  if (confirm('Bạn có chắc chắn muốn xoá chức năng này?')) {
    try {
      await FeatureService.deleteFeature(id);
      // Xoá xong thì load lại đúng filter hiện tại
      await loadData(currentAppId.value);
    } catch (e) {
      alert('Không thể xoá chức năng này.');
    }
  }
};
</script>

<template>
  <div class="p-6 h-full flex flex-col">
    <div class="mb-6 shrink-0">
      <BaseBreadcrumb :items="[{ label: 'Admin', to: '/admin' }, { label: 'Quản lý Chức năng' }]" />
      <h1 class="text-2xl font-bold text-dark font-serif uppercase">Chức năng dự án</h1>
    </div>

    <FeatureFilter 
      :apps="apps" 
      @filter="loadData" 
      @create="handleCreate" 
    />

    <div v-if="isLoading" class="flex-1 flex justify-center items-center">
      <BaseSpinner size="lg" />
    </div>

    <div v-else class="flex-1 overflow-auto custom-scrollbar">
      <FeatureTable 
        :items="features" 
        :apps="apps" 
        @edit="handleEdit" 
        @delete="handleDelete" 
      />
    </div>
  </div>
</template>