<script setup lang="ts">
import { onMounted, reactive, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAppStore } from '@/stores/app.store';
import { DomainService } from '@/services/admin/domain.service';
import { TechnologyService } from '@/services/admin/technology.service';
import type { AppCreateRequest, AppUpdateRequest, AppDetailUpdateRequest, Domain, Technology } from '@/types/admin.types';
import { toast } from 'vue3-toastify';

// Components
import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import BaseCard from '@/components/base/BaseCard.vue';
import BaseInput from '@/components/base/BaseInput.vue';
import BaseTextarea from '@/components/base/BaseTextarea.vue';
import BaseSelect from '@/components/base/BaseSelect.vue';
import BaseMultiSelect from '@/components/base/BaseMultiSelect.vue';
import AppFormMembers from '@/components/admin/app/AppFormMembers.vue';
import AppFormImages from '@/components/admin/app/AppFormImages.vue';
import MediaUpload from '@/components/common/MediaUpload.vue';

const route = useRoute();
const router = useRouter();
const appStore = useAppStore();

const appId = route.params.id as string;
const isEdit = computed(() => !!appId);
const domains = ref<Domain[]>([]);
const technologies = ref<Technology[]>([]);

// Extended Form State
interface ExtendedAppForm extends AppUpdateRequest {
  longDescription: string;
  demoUrl: string;
  sourceUrl: string;
}

const form = reactive<ExtendedAppForm>({
  name: '',
  sku: '',
  shortDescription: '',
  thumbnail: '',
  domainId: '',
  technologyIds: [],
  approvalStatus: 'PENDING',
  members: [],
  images: [],
  longDescription: '',
  demoUrl: '',
  sourceUrl: ''
});

onMounted(async () => {
  try {
    const [domainRes, techRes] = await Promise.all([
      DomainService.getAll(),
      TechnologyService.getAll()
    ]);
    domains.value = domainRes;
    technologies.value = techRes;
    if (isEdit.value) {
      await appStore.getAppById(appId);
      const data = appStore.currentApp;
      const detail = appStore.currentAppDetail;

      if (data) {
        form.name = data.name;
        form.sku = data.sku;
        form.shortDescription = data.shortDescription;
        form.thumbnail = data.thumbnail;
        form.domainId = data.domainId;
        form.approvalStatus = data.approvalStatus;
        form.technologyIds = data.technologies ? data.technologies.map(t => t.id) : [];
        if (data.members && data.members.length > 0) {
          form.members = data.members.map((m: any) => ({
            customerId: m.customerId || m.email,
            role: m.role as 'LEADER' | 'MEMBER',
            _tempName: m.fullName || m.customerName || 'Thành viên',
            _tempEmail: m.email || m.customerEmail,
            _tempAvatar: m.avatar || ''
          }));
        } else {
          form.members = [];
        }
        if (data.images && data.images.length > 0) {
          form.images = data.images.map(i => ({
            url: i.url,
            isMain: i.isMain
          }));
        } else {
          form.images = [];
        }
      }

      if (detail) {
        form.longDescription = detail.longDescription || '';
        form.demoUrl = detail.demoUrl || '';
        form.sourceUrl = detail.sourceUrl || '';
      }
    }
  } catch (e) {
    console.error("Init Error", e);
  }
});

import apiClient from '@/services/api/api';
import _ from 'lodash';
import { watch } from 'vue';

const validateURLs = () => {
  const urlPattern = /^(https?:\/\/[^\s$.?#].[^\s]*)$/i;
  if (form.demoUrl && !urlPattern.test(form.demoUrl)) {
    toast.error("Link Demo không hợp lệ (phải bắt đầu bằng http:// hoặc https://)");
    return false;
  }
  if (form.sourceUrl && !urlPattern.test(form.sourceUrl)) {
    toast.error("Link Source Code không hợp lệ");
    return false;
  }
  return true;
};

const fetchGithubContributors = async (url: string) => {
  if (!url || !url.includes('github.com')) return;

  try {
    const res = await apiClient.get('/admin/apps/github-contributors', {
      params: { url }
    });

    if (res.data && res.data.length > 0) {
      const newMembers = res.data.map((m: any) => ({
        customerId: m.fullName + "@github.com", // Unique ID strategy
        role: 'MEMBER',
        _tempName: m.fullName,
        _tempEmail: m.email,
        _tempAvatar: m.avatar
      }));

      // Merge unique members
      const currentIds = new Set(form.members.map(m => m.customerId));
      const toAdd = newMembers.filter((m: any) => !currentIds.has(m.customerId));

      if (toAdd.length > 0) {
        form.members = [...form.members, ...toAdd];
        toast.success(`Đã tự động thêm ${toAdd.length} thành viên từ GitHub!`);
      }
    }
  } catch (e) {
    console.error("Failed to fetch contributors", e);
  }
};

// Debounce the fetch action to avoid spamming while typing
const debouncedFetch = _.debounce(fetchGithubContributors, 1000);

watch(() => form.sourceUrl, (newVal) => {
  if (newVal && newVal.includes('github.com')) {
    debouncedFetch(newVal);
  }
});

const handleSubmit = async () => {
  // ... existing logic ...
  if (!validateURLs()) return;

  try {
    const cleanMembers = form.members.filter(m => m.customerId && m.customerId.trim() !== '');
    const cleanImages = form.images.filter(i => i.url && i.url.trim() !== '');

    const mainPayload: AppUpdateRequest = {
      name: form.name,
      sku: form.sku,
      shortDescription: form.shortDescription,
      thumbnail: form.thumbnail,
      domainId: form.domainId,
      technologyIds: form.technologyIds,
      approvalStatus: form.approvalStatus,
      members: cleanMembers,
      images: cleanImages
    };

    let targetId = appId;
    if (isEdit.value) {
      await appStore.updateApp(appId, mainPayload);
      targetId = appId;
    } else {
      const res = await appStore.createApp(mainPayload as AppCreateRequest);
      targetId = res.id;
      // Update extended fields for new app immediately
      if (cleanMembers.length > 0 || cleanImages.length > 0) {
        await appStore.updateApp(targetId, mainPayload);
      }
    }

    // Update Detail Info (Link, Long Desc)
    const detailPayload: AppDetailUpdateRequest = {
      longDescription: form.longDescription,
      demoUrl: form.demoUrl,
      sourceUrl: form.sourceUrl,
      specifications: null
    };
    await appStore.updateAppDetailInfo(targetId, detailPayload);

    toast.success(isEdit.value ? "Cập nhật thành công!" : "Tạo mới thành công!");
    router.push('/admin/apps');
  } catch (e) {
    console.error(e);
    toast.error("Có lỗi xảy ra. Vui lòng kiểm tra lại console.");
  }
};

const handleStatusChange = async (status: 'APPROVED' | 'REJECTED' | 'DRAFT' | 'PENDING') => {
  if (!appId) return;
  if (!confirm('Bạn có chắc chắn muốn thay đổi trạng thái?')) return;
  try {
    await appStore.changeStatus(appId, status);
    form.approvalStatus = status;
    toast.success("Cập nhật trạng thái thành công!");
  } catch (e) {
    toast.error("Lỗi khi cập nhật trạng thái");
  }
};
</script>

<template>
  <div class="p-6 min-h-screen bg-gray-50/50">
    <div class="mb-6 flex justify-between items-start">
      <div>
        <BaseBreadcrumb :items="[
          { label: 'Admin', to: '/admin' },
          { label: 'Dự án', to: '/admin/apps' },
          { label: isEdit ? 'Cập nhật' : 'Tạo mới' }
        ]" />
        <h1 class="text-2xl font-bold text-dark font-serif uppercase tracking-wide">
          {{ isEdit ? `Cập nhật: ${form.name}` : 'Thêm Dự án mới' }}
        </h1>
      </div>
      <div class="flex gap-3">
        <BaseButton variant="outline" @click="router.back()">Huỷ bỏ</BaseButton>
        <BaseButton variant="primary" @click="handleSubmit" :disabled="appStore.isLoading">
          {{ appStore.isLoading ? 'Đang lưu...' : 'Lưu lại' }}
        </BaseButton>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

      <div class="lg:col-span-2 space-y-6">
        <BaseCard>
          <h3 class="text-lg font-bold text-dark mb-4 border-b border-gray-100 pb-2">Thông tin chung</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <BaseInput v-model="form.name" label="Tên dự án (*)" placeholder="Nhập tên dự án..." />
            <BaseInput v-model="form.sku" label="Mã SKU" placeholder="VD: PROJ-001" />
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <BaseSelect v-model="form.domainId" :options="domains.map(d => ({ value: d.id, label: d.name }))"
              label="Lĩnh vực (*)" />

            <BaseMultiSelect v-model="form.technologyIds"
              :options="technologies.map(t => ({ value: t.id, label: t.name }))" label="Công nghệ sử dụng"
              placeholder="Chọn các công nghệ..." />
          </div>

          <BaseTextarea v-model="form.shortDescription" label="Mô tả ngắn"
            placeholder="Giới thiệu tóm tắt về dự án..." />
        </BaseCard>

        <BaseCard>
          <h3 class="text-lg font-bold text-dark mb-4 border-b border-gray-100 pb-2">Thông tin chi tiết</h3>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <BaseInput v-model="form.demoUrl" label="Link Demo (URL)" placeholder="https://..." />
            <BaseInput v-model="form.sourceUrl" label="Source Code (Git)" placeholder="https://github.com/..." />
          </div>

          <BaseTextarea v-model="form.longDescription" label="Bài viết chi tiết" :rows="6"
            placeholder="Mô tả chi tiết về chức năng, công nghệ, hướng dẫn cài đặt..." />
        </BaseCard>

        <BaseCard>
          <AppFormMembers v-model="form.members" />
        </BaseCard>
      </div>

      <div class="space-y-6">
        <BaseCard>
          <h3 class="text-sm font-bold text-dark mb-4 uppercase">Trạng thái</h3>

          <div class="mb-4">
            <div class="px-3 py-1.5 rounded text-sm font-bold inline-block" :class="{
              'bg-gray-200 text-gray-700': form.approvalStatus === 'DRAFT',
              'bg-yellow-100 text-yellow-700': form.approvalStatus === 'PENDING',
              'bg-green-100 text-green-700': form.approvalStatus === 'APPROVED',
              'bg-red-100 text-red-700': form.approvalStatus === 'REJECTED'
            }">
              {{
                form.approvalStatus === 'PENDING' ? 'Chờ duyệt' :
                  (form.approvalStatus === 'APPROVED' ? 'Đã duyệt' :
                    (form.approvalStatus === 'REJECTED' ? 'Đã từ chối' : 'Bản nháp'))
              }}
            </div>
          </div>

          <div v-if="isEdit" class="space-y-2">
            <div v-if="form.approvalStatus === 'PENDING'" class="grid grid-cols-2 gap-2">
              <BaseButton variant="outline" class="!text-red-600 !border-red-200 hover:!bg-red-50"
                @click="handleStatusChange('REJECTED')">
                Từ chối
              </BaseButton>
              <BaseButton variant="primary" class="!bg-green-600 hover:!bg-green-700"
                @click="handleStatusChange('APPROVED')">
                Phê duyệt
              </BaseButton>
            </div>

            <!-- Allow returning to Draft or Pending from other states if needed, or simple reset -->
            <div v-if="['APPROVED', 'REJECTED'].includes(form.approvalStatus)" class="pt-2 border-t border-gray-100">
              <p class="text-xs text-center text-gray-400 mb-2">Thay đổi trạng thái</p>
              <div class="grid grid-cols-2 gap-2">
                <BaseButton size="sm" variant="outline" @click="handleStatusChange('DRAFT')">Về Nháp</BaseButton>
                <BaseButton size="sm" variant="secondary" @click="handleStatusChange('PENDING')">Gửi duyệt</BaseButton>
              </div>
            </div>
          </div>
        </BaseCard>

        <BaseCard>
          <h3 class="text-sm font-bold text-dark mb-4 uppercase">Ảnh đại diện</h3>
          <div class="mb-3">
            <MediaUpload v-model="form.thumbnail" label="Ảnh thumbnail" />
          </div>
        </BaseCard>

        <BaseCard>
          <AppFormImages v-model="form.images" />
        </BaseCard>
      </div>
    </div>
  </div>
</template>