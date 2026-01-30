<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { FeatureService } from '@/services/admin/feature.service';
import { AppService } from '@/services/admin/app.service';


import BaseSpinner from '@/components/base/BaseSpinner.vue';
import FeatureTable from '@/components/admin/feature/FeatureTable.vue';
import AdminToolbar from '@/components/admin/common/AdminToolbar.vue';
import BaseSelect from '@/components/base/BaseSelect.vue';
import { computed } from 'vue';
import { AppResponse } from '@/types/admin.types';
import { ArrowDownTrayIcon, ArrowUpTrayIcon } from '@heroicons/vue/24/outline';
import BaseButton from '@/components/base/BaseButton.vue';

import { toast } from 'vue3-toastify';
import { encodeId } from '@/utils';

const router = useRouter();
const features = ref<any[]>([]);
const apps = ref<AppResponse[]>([]);
const isLoading = ref(false);
const currentAppId = ref(''); // State lưu filter hiện tại
const keyword = ref('');

const appOptions = computed(() => {
  return apps.value.map(a => ({ label: a.name, value: a.id }));
});

const filteredFeatures = computed(() => {
  if (!keyword.value) return features.value;
  const k = keyword.value.toLowerCase();
  return features.value.filter((f: any) => f.name.toLowerCase().includes(k) || (f.description && f.description.toLowerCase().includes(k)));
});
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
    let res = [];
    if (currentAppId.value) {
      res = await FeatureService.getByAppId(currentAppId.value);
    } else {
      res = await FeatureService.getAll();
    }
    // Sort locally by sortOrder
    features.value = res.sort((a: any, b: any) => (a.sortOrder || 0) - (b.sortOrder || 0));
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
  router.push({ name: 'AdminFeatureEdit', params: { id: encodeId(item.id) } });
};

const handleDelete = async (id: string) => {
  if (confirm('Bạn có chắc chắn muốn xoá chức năng này?')) {
    try {
      await FeatureService.deleteFeature(id);
      toast.success("Xoá thành công!");
      // Xoá xong thì load lại đúng filter hiện tại
      await loadData(currentAppId.value);
    } catch (e) {
      toast.error('Không thể xoá chức năng này. Có thể đang chứa Media');
    }
  }
};

const handleReorder = async () => {
  try {
    const payload = features.value.map((item, index) => ({
      id: item.id,
      sortOrder: index + 1
    }));
    await FeatureService.updateOrder(payload);
  } catch (e) {
    toast.error("Lỗi cập nhật thứ tự");
  }
};

const fileInput = ref<HTMLInputElement | null>(null);

const handleExport = async () => {
  try {
    const data = await FeatureService.exportExcel();
    const url = window.URL.createObjectURL(new Blob([data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `features_export_${new Date().getTime()}.xlsx`);
    document.body.appendChild(link);
    link.click();
    link.remove();
    toast.success("Export thành công!");
  } catch (error) {
    toast.error("Export thất bại");
  }
};

const downloadTemplateData = async () => {
  try {
    const data = await FeatureService.downloadTemplate();
    const url = window.URL.createObjectURL(new Blob([data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `features_import_template.xlsx`);
    document.body.appendChild(link);
    link.click();
    link.remove();
    toast.success("Tải mẫu thành công!");
  } catch (error) {
    toast.error("Tải mẫu thất bại");
  }
};

const triggerImport = () => {
  fileInput.value?.click();
};

const handleImport = async (event: any) => {
  const file = event.target.files[0];
  if (!file) return;
  try {
    const res = await FeatureService.importExcel(file);
    toast.success(`Import thành công: ${res.data.importedCount} mục.`);
    if (res.data.errorCount > 0) {
      toast.warning(`Có ${res.data.errorCount} lỗi. Kiểm tra console.`);
      console.warn("Import errors:", res.data.errors);
    }
    loadData(currentAppId.value);
  } catch (error) {
    toast.error("Import thất bại");
  } finally {
    if (fileInput.value) fileInput.value.value = '';
  }
};
</script>

<template>
  <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">


    <div class="mb-4">
      <AdminToolbar
        v-model="keyword"
        placeholder="Tìm kiếm chức năng..."
        @create="handleCreate"
      >
        <template #actions>
          <input type="file" ref="fileInput" class="hidden" accept=".xlsx, .xls" @change="handleImport" />
          <BaseButton variant="outline" @click="triggerImport" title="Import Excel" class="flex items-center gap-2">
            <ArrowDownTrayIcon class="w-4 h-4" /> Import
          </BaseButton>
          <BaseButton variant="outline" @click="handleExport" title="Export Excel" class="flex items-center gap-2">
            <ArrowUpTrayIcon class="w-4 h-4" /> Export
          </BaseButton>
          <BaseButton variant="outline" @click="downloadTemplateData" title="Tải mẫu Import" class="flex items-center gap-2">
            <ArrowDownTrayIcon class="w-4 h-4" /> Bản mẫu
          </BaseButton>
        </template>
        <div>
          <label class="block text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">
             Dự án
           </label>
           <BaseSelect
             :model-value="currentAppId"
             @update:model-value="loadData"
             :options="appOptions"
             placeholder="-- Tất cả dự án --"
             class="w-full sm:w-64"
           />
        </div>
      </AdminToolbar>
    </div>

    <div v-if="isLoading" class="flex-1 flex justify-center items-center">
      <BaseSpinner size="lg" />
    </div>

    <div v-else class="flex-1 overflow-auto custom-scrollbar bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm mt-4">
      <FeatureTable :items="filteredFeatures" :apps="apps" @edit="handleEdit" @delete="handleDelete"
        @reorder="handleReorder" />
    </div>
  </div>
</template>