<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { DomainService } from '@/services/admin/domain.service';
import type { Domain } from '@/types/admin.types';


import BaseSpinner from '@/components/base/BaseSpinner.vue';
import DomainTable from '@/components/admin/domain/DomainTable.vue';
import AdminToolbar from '@/components/admin/common/AdminToolbar.vue';
import { computed } from 'vue';
import { ArrowDownTrayIcon, ArrowUpTrayIcon } from '@heroicons/vue/24/outline';
import BaseButton from '@/components/base/BaseButton.vue';

import { toast } from 'vue3-toastify';
import { encodeId } from '@/utils';

const router = useRouter();
const items = ref<Domain[]>([]);
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
const handleEdit = (item: Domain) => router.push({ name: 'AdminDomainEdit', params: { id: encodeId(item.id) } });

const handleDelete = async (id: string) => {
  if (confirm('Bạn có chắc muốn xoá?')) {
    try {
      await DomainService.deleteDomain(id);
      toast.success("Xoá thành công!");
      await loadData();
    } catch (e: any) {
      // Nếu backend trả về 409 (đang có sản phẩm liên kết)
      const status = e.response?.status;
      const msg = e.response?.data?.message || e.response?.data?.error;
      if (status === 409) {
        toast.error(msg || "Không thể xoá. Lĩnh vực này đang được sử dụng.");
      } else if (status === 404) {
        toast.error("Không tìm thấy lĩnh vực này.");
        await loadData(); // Refresh để cập nhật danh sách
      } else {
        toast.error(msg || "Không thể xoá. Có thể lĩnh vực này đang chứa sản phẩm.");
      }
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

const fileInput = ref<HTMLInputElement | null>(null);

const handleExport = async () => {
  try {
    const data = await DomainService.exportExcel();
    const url = window.URL.createObjectURL(new Blob([data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `domains_export_${new Date().getTime()}.xlsx`);
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
    const data = await DomainService.downloadTemplate();
    const url = window.URL.createObjectURL(new Blob([data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `domains_import_template.xlsx`);
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
    const res = await DomainService.importExcel(file);
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
        placeholder="Tìm kiếm lĩnh vực..."
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
      <DomainTable v-model:items="filteredItems" @edit="handleEdit" @delete="handleDelete" @reorder="handleReorder" />
    </div>
  </div>
</template>