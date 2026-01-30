<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAppStore } from '@/stores/app.store';
import { DomainService } from '@/services/admin/domain.service';


import BasePagination from '@/components/base/BasePagination.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import BaseModal from '@/components/base/BaseModal.vue';
import BaseButton from '@/components/base/BaseButton.vue';
// import AppFilter from '@/components/admin/app/AppFilter.vue'; // Removed
import AppTable from '@/components/admin/app/AppTable.vue';
import type { Domain } from '@/types/admin.types';
import { toast } from 'vue3-toastify';
import { encodeId } from '@/utils';
import { ArrowDownTrayIcon, ArrowUpTrayIcon } from '@heroicons/vue/24/outline';
import AdminToolbar from '@/components/admin/common/AdminToolbar.vue';
import BaseSelect from '@/components/base/BaseSelect.vue';
import { reactive, computed } from 'vue'; // Ensure computed/reactive are imported

const router = useRouter();
const appStore = useAppStore();

// State local
const domains = ref<Domain[]>([]);
const showDeleteModal = ref(false);
const showBatchDeleteModal = ref(false);
const deleteId = ref<string | null>(null);
const selectedIds = ref<string[]>([]);
const fileInput = ref<HTMLInputElement | null>(null);

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

const domainOptions = computed(() => domains.value.map(d => ({ label: d.name, value: d.id })));

const statusOptions = [
  { label: 'Đã duyệt (Approved)', value: 'APPROVED' },
  { label: 'Chờ duyệt (Pending)', value: 'PENDING' },
  { label: 'Nháp (Draft)', value: 'DRAFT' },
  { label: 'Từ chối (Rejected)', value: 'REJECTED' },
  { label: 'Lưu trữ (Archived)', value: 'ARCHIVED' }
];

const filter = reactive({
  keyword: '',
  domainId: '',
  status: ''
});

const handleSearch = () => {
  appStore.setFilter({ 
    query: filter.keyword,
    domainId: filter.domainId || undefined,
    status: filter.status || undefined,
    page: 1 // Reset page
  });
};

const handleResetFilter = () => {
  filter.keyword = '';
  filter.domainId = '';
  filter.status = '';
  handleSearch();
};


const handleDuplicate = async (id: string) => {
  try {
    if (confirm('Bạn có chắc chắn muốn nhân bản dự án này?')) {
      await appStore.duplicateApp(id);
      toast.success("Nhân bản thành công!");
    }
  } catch (error) {
    toast.error("Nhân bản thất bại");
  }
};

const handleExport = async () => {
  try {
    await appStore.exportApps();
    toast.success("Export thành công!");
  } catch (error) {
    toast.error("Export thất bại");
  }
};

const downloadTemplateData = async () => {
  try {
    await appStore.downloadTemplate();
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
    await appStore.importApps(file);
    toast.success("Import thành công!");
  } catch (error) {
    toast.error("Import thất bại");
  } finally {
    if (fileInput.value) fileInput.value.value = '';
  }
};

const handleSelectionChange = (ids: string[]) => {
  selectedIds.value = ids;
};

const handleBatchDelete = async () => {
  try {
    await Promise.all(selectedIds.value.map(id => appStore.deleteApp(id)));
    toast.success(`Đã xoá ${selectedIds.value.length} dự án`);
    selectedIds.value = [];
    showBatchDeleteModal.value = false;
    await appStore.fetchApps();
  } catch (error) {
    toast.error("Có lỗi xảy ra khi xoá hàng loạt");
  }
};

const handleBatchStatus = async (status: string) => {
  try {
    await Promise.all(selectedIds.value.map(id => appStore.changeStatus(id, status)));
    toast.success(`Đã cập nhật trạng thái cho ${selectedIds.value.length} dự án`);
    selectedIds.value = [];
    await appStore.fetchApps();
  } catch (error) {
    toast.error("Có lỗi xảy ra khi cập nhật trạng thái");
  }
};


// Handlers
const handlePageChange = (page: number) => {
  appStore.setFilter({ page });
};

// Điều hướng sang trang Create (AppModify)
const navigateToCreate = () => router.push({ name: 'AdminAppCreate' });

// Điều hướng sang trang Edit (AppModify)
const navigateToEdit = (id: string) => router.push({ name: 'AdminAppEdit', params: { id: encodeId(id) } });

const confirmDelete = (id: string) => {
  deleteId.value = id;
  showDeleteModal.value = true;
};

const handleDelete = async () => {
  if (deleteId.value) {
    await appStore.deleteApp(deleteId.value);
    toast.success("Xoá thành công!");
    showDeleteModal.value = false;
    deleteId.value = null;
  }
};

</script>

<template>
  <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
    <div class="shrink-0 mb-4">
      <AdminToolbar
        v-model="filter.keyword"
        placeholder="Tìm theo tên sản phẩm, mã SKU..."
        @update:model-value="handleSearch"
        @create="navigateToCreate"
        @reset="handleResetFilter"
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
            Lĩnh vực
          </label>
          <BaseSelect
            v-model="filter.domainId"
            :options="domainOptions"
            placeholder="Tất cả lĩnh vực"
            @update:model-value="handleSearch"
            class="w-full"
          />
        </div>
        <div>
          <label class="block text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">
            Trạng thái
          </label>
          <BaseSelect
            v-model="filter.status"
            :options="statusOptions"
            placeholder="Tất cả trạng thái"
            @update:model-value="handleSearch"
            class="w-full"
          />
        </div>
      </AdminToolbar>
    </div>

    <!-- Batch Actions Bar -->
    <div v-if="selectedIds.length > 0"
      class="shrink-0 mb-6 bg-blue-50 dark:bg-blue-900/20 border border-blue-100 dark:border-blue-800 p-4 rounded-lg flex items-center justify-between shadow-sm animate-fade-in">
      <span class="text-sm text-blue-800 dark:text-blue-300 font-bold flex items-center gap-2">
        <span class="w-2 h-2 rounded-full bg-blue-500"></span>
        Đã chọn {{ selectedIds.length }} dự án
      </span>
      <div class="flex gap-2">
        <BaseButton size="sm" variant="danger" icon="trash" @click="showBatchDeleteModal = true">Xoá</BaseButton>
        <BaseButton size="sm" class="bg-green-600 text-white hover:bg-green-700 border-green-600 shadow-sm"
          @click="handleBatchStatus('APPROVED')">Duyệt</BaseButton>
        <BaseButton size="sm" class="bg-yellow-500 text-white hover:bg-yellow-600 border-yellow-500 shadow-sm"
          @click="handleBatchStatus('PENDING')">Gửi duyệt</BaseButton>
        <BaseButton size="sm" class="bg-gray-500 text-white hover:bg-gray-600 border-gray-500 shadow-sm"
          @click="handleBatchStatus('DRAFT')">Về Nháp</BaseButton>
      </div>
    </div>

    <div v-if="appStore.isLoading" class="flex-1 flex justify-center items-center">
      <BaseSpinner size="lg" />
    </div>

    <div v-else class="flex-1 flex flex-col min-h-0 bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm overflow-hidden">
      <div class="flex-1 overflow-auto custom-scrollbar">
        <AppTable :apps="appStore.apps" @edit="navigateToEdit" @delete="confirmDelete" @duplicate="handleDuplicate"
          @selection-change="handleSelectionChange" />
      </div>

      <div class="p-4 border-t border-gray-100 dark:border-gray-700 bg-gray-50 dark:bg-gray-900/50">
        <BasePagination :current-page="appStore.filterParams.page" :total-pages="appStore.totalPages"
          @change="handlePageChange" />
      </div>
    </div>

    <BaseModal :is-open="showDeleteModal" title="Xác nhận xoá" @close="showDeleteModal = false">
      <p class="text-gray-600 dark:text-gray-300">Bạn có chắc chắn muốn xoá dự án này không?</p>
      <template #footer>
        <BaseButton variant="outline" @click="showDeleteModal = false">Huỷ bỏ</BaseButton>
        <BaseButton variant="danger" @click="handleDelete">Đồng ý Xoá</BaseButton>
      </template>
    </BaseModal>

    <BaseModal :is-open="showBatchDeleteModal" title="Xác nhận xoá hàng loạt" @close="showBatchDeleteModal = false">
      <p class="text-gray-600 dark:text-gray-300">Bạn có chắc chắn muốn xoá {{ selectedIds.length }} dự án đã chọn không? Hành động này
        không
        thể hoàn tác.</p>
      <template #footer>
        <BaseButton variant="outline" @click="showBatchDeleteModal = false">Huỷ bỏ</BaseButton>
        <BaseButton variant="danger" @click="handleBatchDelete">Đồng ý Xoá</BaseButton>
      </template>
    </BaseModal>
  </div>
</template>