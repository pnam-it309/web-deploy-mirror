<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { TechnologyService } from '@/services/admin/technology.service';
import type { Technology } from '@/types/admin.types';


import BaseSpinner from '@/components/base/BaseSpinner.vue';
import TechnologyTable from '@/components/admin/technology/TechnologyTable.vue';
import AdminToolbar from '@/components/admin/common/AdminToolbar.vue';
import { computed } from 'vue';
import { toast } from 'vue3-toastify';
import { ArrowDownTrayIcon, ArrowUpTrayIcon } from '@heroicons/vue/24/outline';
import BaseButton from '@/components/base/BaseButton.vue';
import { encodeId } from '@/utils';

const router = useRouter();
const items = ref<Technology[]>([]);
const isLoading = ref(false);
const keyword = ref('');

const filteredItems = computed(() => {
  if (!keyword.value) return items.value;
  const k = keyword.value.toLowerCase();
  return items.value.filter(i => i.name.toLowerCase().includes(k) || (i.description && i.description.toLowerCase().includes(k)));
});

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
const handleEdit = (item: Technology) => router.push({ name: 'AdminTechnologyEdit', params: { id: encodeId(item.id) } });
const handleDelete = async (id: string) => {
  if (confirm('Xoá công nghệ này?')) { await TechnologyService.deleteTechnology(id); loadData(); }
};

const fileInput = ref<HTMLInputElement | null>(null);

const handleExport = async () => {
  try {
    const data = await TechnologyService.exportExcel();
    const url = window.URL.createObjectURL(new Blob([data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `technologies_export_${new Date().getTime()}.xlsx`);
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
    const data = await TechnologyService.downloadTemplate();
    const url = window.URL.createObjectURL(new Blob([data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `technologies_import_template.xlsx`);
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
    const res = await TechnologyService.importExcel(file);
    toast.success(`Import thành công: ${res.data.importedCount} mục.`);
    if (res.data.errorCount > 0) {
      toast.warning(`Có ${res.data.errorCount} lỗi. Kiểm tra console.`);
      console.warn("Import errors:", res.data.errors);
    }
    loadData();
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
        placeholder="Tìm công nghệ..."
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
      </AdminToolbar>
    </div>
    
    <div v-if="isLoading" class="flex-1 flex justify-center items-center">
      <BaseSpinner size="lg" />
    </div>
    
    <div v-else class="flex-1 overflow-auto custom-scrollbar bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm mt-4">
      <TechnologyTable :items="filteredItems" @edit="handleEdit" @delete="handleDelete" />
    </div>
  </div>
</template>