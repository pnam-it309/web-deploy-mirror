<script setup lang="ts">
import { onMounted, reactive, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAppStore } from '@/stores/app.store';
import { DomainService } from '@/services/admin/domain.service';
import { TechnologyService } from '@/services/admin/technology.service';
import type { AdminAppCreateRequest, AdminAppUpdateRequest, AppDetailUpdateRequest, Domain, Technology } from '@/types/admin.types';
import { toast } from 'vue3-toastify';
import { decodeId } from '@/utils';

// Components

import BaseButton from '@/components/base/BaseButton.vue';
import BaseCard from '@/components/base/BaseCard.vue';
import BaseInput from '@/components/base/BaseInput.vue';
import BaseTextarea from '@/components/base/BaseTextarea.vue';
import BaseSelect from '@/components/base/BaseSelect.vue';
import BaseMultiSelect from '@/components/base/BaseMultiSelect.vue';
import AppFormMembers from '@/components/admin/app/AppFormMembers.vue';
import AppFormImages from '@/components/admin/app/AppFormImages.vue';
import MediaUpload from '@/components/common/MediaUpload.vue';
import BaseModal from '@/components/base/BaseModal.vue';
import YouTubeEmbed from '@/components/common/YouTubeEmbed.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';

const route = useRoute();
const router = useRouter();
const appStore = useAppStore();

const appId = decodeId(route.params.id as string);
const isEdit = computed(() => !!appId);
const domains = ref<Domain[]>([]);
const technologies = ref<Technology[]>([]);

// Extended Form State
interface ExtendedAppForm extends AdminAppUpdateRequest {
  longDescription: string;
  demoUrl: string;
  sourceUrl: string;
  metaTitle: string;
  metaDescription: string;
  metaKeywords: string;
  publishAt: string;
  isFeatured: boolean;
  isFeaturedVideo: boolean;
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
  sourceUrl: '',
  metaTitle: '',
  metaDescription: '',
  metaKeywords: '',
  publishAt: '',
  isFeatured: false,
  isFeaturedVideo: false
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
        // Map new field
        form.isFeaturedVideo = data.isFeaturedVideo || false;
      }

      if (detail) {
        form.longDescription = detail.longDescription || '';
        form.demoUrl = detail.demoUrl || '';
        form.sourceUrl = detail.sourceUrl || '';
        form.metaTitle = detail.metaTitle || '';
        form.metaDescription = detail.metaDescription || '';
        form.metaKeywords = detail.metaKeywords || '';
        form.publishAt = detail.publishAt ? new Date(detail.publishAt).toISOString().slice(0, 16) : '';
        form.isFeatured = detail.isFeatured || false;
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

const githubToken = ref('');
const githubError = ref('');
const isFetchingGithub = ref(false);

const fetchGithubContributors = async (url: string) => {
  if (!url || !url.includes('github.com')) return;
  isFetchingGithub.value = true;
  githubError.value = '';

  try {
    const params: any = { url };
    if (githubToken.value) {
      params.token = githubToken.value;
    }

    const res = await apiClient.get('/admin/apps/github-contributors', { params });

    if (res.data && res.data.length > 0) {
      const newMembers = res.data.map((m: any) => ({
        customerId: m.fullName + "@github.com",
        role: 'MEMBER',
        _tempName: m.fullName,
        _tempEmail: m.email,
        _tempAvatar: m.avatar
      }));

      const currentIds = new Set(form.members.map(m => m.customerId));
      const toAdd = newMembers.filter((m: any) => !currentIds.has(m.customerId));

      if (toAdd.length > 0) {
        form.members = [...form.members, ...toAdd];
        toast.success(`Đã tự động thêm ${toAdd.length} thành viên từ GitHub!`);
      } else {
         toast.info("Đã tìm thấy repo nhưng không có thành viên mới (hoặc đã có trong danh sách).");
      }
    } else {
        // Empty list usually means empty repo or no contributors found (or private without token but API returned 200 empty?)
        // Actually our backend returns empty list on error.
        githubError.value = "Không tìm thấy thành viên. Nếu repo Private, hãy nhập Token.";
    }
  } catch (e: any) {
    console.error("Failed to fetch contributors", e);
    githubError.value = "Không thể truy cập. Repo Private cần Token cá nhân (nếu Token hệ thống không đủ quyền).";
  } finally {
    isFetchingGithub.value = false;
  }
};

// Debounce the fetch action to avoid spamming while typing
const debouncedFetch = _.debounce((url) => fetchGithubContributors(url), 1000);

watch(() => form.sourceUrl, (newVal) => {
  if (newVal && newVal.includes('github.com')) {
    debouncedFetch(newVal);
  } else {
    githubError.value = '';
  }
});

const handleSubmit = async () => {
  // ... existing logic ...
  if (!validateURLs()) return;

  try {
    const cleanMembers = form.members.filter(m => m.customerId && m.customerId.trim() !== '');
    const cleanImages = form.images.filter(i => i.url && i.url.trim() !== '');

    const mainPayload: AdminAppUpdateRequest = {
      name: form.name,
      sku: form.sku,
      shortDescription: form.shortDescription,
      thumbnail: form.thumbnail,
      domainId: form.domainId,
      technologyIds: form.technologyIds,
      approvalStatus: form.approvalStatus,
      isFeaturedVideo: form.isFeaturedVideo,
      members: cleanMembers,
      images: cleanImages
    };

    let targetId = appId;
    if (isEdit.value) {
      await appStore.updateApp(appId, mainPayload);
      targetId = appId;
    } else {
      const res = await appStore.createApp(mainPayload as AdminAppCreateRequest);
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
      specifications: null as any,
      metaTitle: form.metaTitle,
      metaDescription: form.metaDescription,
      metaKeywords: form.metaKeywords,
      publishAt: form.publishAt ? new Date(form.publishAt).toISOString() : null,
      isFeatured: form.isFeatured
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

const showPreviewModal = ref(false);
const previewUrl = ref('');

const handlePreview = async () => {
  const currentAppId = route.params.id as string;
  if (!currentAppId) {
    toast.error("Chưa lưu dự án. Vui lòng lưu trước khi xem preview.");
    return;
  }
  try {
    const res = await apiClient.post(`/admin/preview/${currentAppId}/generate-token`);
    const token = res.data.token || (res.data.data && res.data.data.token);
    
    if (token) {
        previewUrl.value = `${window.location.origin}/preview/${token}`;
        showPreviewModal.value = true;
    } else {
        toast.error("Không nhận được token preview từ server.");
    }
  } catch (error) {
    console.error(error);
    toast.error("Không thể tạo preview link");
  }
};

const copyPreviewLink = () => {
  navigator.clipboard.writeText(previewUrl.value);
  toast.success("Đã sao chép link preview");
};

// Video Preview Logic
const showVideoModal = ref(false);
const isValidYoutube = (url?: string) => {
  if (!url) return false;
  return /(?:youtube\.com\/watch\?v=|youtu\.be\/|youtube\.com\/embed\/)([^&\n?#]+)/.test(url);
};
</script>

<template>
  <div class="p-6 h-full overflow-y-auto custom-scrollbar bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
    <div class="mb-6 flex justify-between items-center">
      <div></div>
      <div class="flex gap-3">
        <BaseButton variant="outline" @click="router.back()">Huỷ bỏ</BaseButton>
        <BaseButton v-if="isEdit" variant="primary" class="!bg-blue-600 hover:!bg-blue-700" @click="handlePreview">Xem Preview</BaseButton>
        <BaseButton variant="primary" @click="handleSubmit" :disabled="appStore.isLoading">
          {{ appStore.isLoading ? 'Đang lưu...' : 'Lưu lại' }}
        </BaseButton>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">

      <div class="lg:col-span-2 space-y-6">
        <BaseCard>
          <h3 class="text-lg font-bold text-gray-900 dark:text-white mb-4 border-b border-gray-100 dark:border-gray-700 pb-2">Thông tin chung</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <BaseInput v-model="form.name" label="Tên dự án (*)" placeholder="Nhập tên dự án..." class="dark:text-white" />
            <BaseInput v-model="form.sku" label="Mã SKU" placeholder="VD: PROJ-001" class="dark:text-white" />
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <BaseSelect v-model="form.domainId" :options="domains.map(d => ({ value: d.id, label: d.name }))"
              label="Lĩnh vực (*)" class="dark:text-white" />

            <BaseMultiSelect v-model="form.technologyIds"
              :options="technologies.map(t => ({ value: t.id, label: t.name }))" label="Công nghệ sử dụng"
              placeholder="Chọn các công nghệ..." class="dark:text-white" />
          </div>

          <BaseTextarea v-model="form.shortDescription" label="Mô tả ngắn"
            placeholder="Giới thiệu tóm tắt về dự án..." class="dark:text-white" />
        </BaseCard>

        <BaseCard>
          <div class="flex justify-between items-center mb-4 border-b border-gray-100 dark:border-gray-700 pb-2">
            <h3 class="text-lg font-bold text-gray-900 dark:text-white">Thông tin chi tiết</h3>
            <BaseButton size="sm" variant="outline" @click="showVideoModal = true"
              class="!text-blue-600 !border-blue-200 hover:!bg-blue-50 dark:hover:!bg-blue-900/20">
              <span class="flex items-center gap-1">
                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                Xem Video
              </span>
            </BaseButton>
          </div>

          <div class="mb-4">
            <label class="flex items-center space-x-2 cursor-pointer bg-blue-50 dark:bg-blue-900/10 p-2 rounded border border-blue-100 dark:border-blue-800">
              <input type="checkbox" v-model="form.isFeaturedVideo"
                class="form-checkbox h-4 w-4 text-blue-600 rounded border-gray-300 focus:ring-blue-500" />
              <span class="text-sm font-medium text-blue-800 dark:text-blue-300">Hiển thị trong danh sách Video nổi bật (Trang chủ)</span>
            </label>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <BaseInput v-model="form.demoUrl" label="Link Demo (URL)" placeholder="https://..." class="dark:text-white" />
            <div class="space-y-2">
              <BaseInput v-model="form.sourceUrl" label="Source Code (Git)" placeholder="https://github.com/..." class="dark:text-white" />
              
              <!-- GitHub Private Repo Handler -->
              <div v-if="githubError" class="mt-3 p-4 bg-orange-50 dark:bg-orange-900/10 border border-orange-200 dark:border-orange-800 rounded-lg animate-fade-in-down">
                <div class="flex gap-3 mb-3">
                   <div class="flex-shrink-0 text-orange-500 mt-0.5">
                     <i class='bx bx-lock-alt text-xl'></i>
                   </div>
                   <div>
                     <p class="text-sm font-semibold text-orange-800 dark:text-orange-200">
                       Repo này có thể là Riêng tư (Private)
                     </p>
                     <p class="text-xs text-orange-600 dark:text-orange-300 mt-1">
                       Chúng tôi không tìm thấy thành viên. Vui lòng cung cấp Access Token để truy cập.
                     </p>
                   </div>
                </div>

                <div class="flex flex-col gap-2">
                   <input 
                     v-model="githubToken"
                     type="password" 
                     placeholder="Dán GitHub Token..." 
                     class="w-full px-3 py-2 text-sm border border-gray-300 dark:border-gray-600 rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-orange-500/50 focus:border-orange-500 transition-all shadow-sm"
                   />
                   <div class="flex justify-end">
                     <button 
                       @click="() => fetchGithubContributors(form.sourceUrl)"
                       :disabled="isFetchingGithub"
                       class="px-4 py-2 bg-orange-600 hover:bg-orange-700 text-white text-sm font-medium rounded-md shadow-sm transition-colors disabled:opacity-50 disabled:cursor-not-allowed whitespace-nowrap flex items-center justify-center gap-2"
                     >
                       <i v-if="isFetchingGithub" class='bx bx-loader-alt animate-spin'></i>
                       {{ isFetchingGithub ? 'Đang thử...' : 'Thử lại' }}
                     </button>
                   </div>
                </div>
                
                <div class="mt-2 text-right">
                  <a href="https://github.com/settings/tokens" target="_blank" class="text-xs font-medium text-blue-600 hover:text-blue-700 dark:text-blue-400 hover:underline inline-flex items-center gap-1">
                    Cách lấy Token? <i class='bx bx-link-external'></i>
                  </a>
                </div>
              </div>
              <div v-else-if="isFetchingGithub" class="text-xs text-blue-500 flex items-center gap-1 mt-1 pl-1">
                 <BaseSpinner size="sm" class="w-3 h-3" />
                 Đang tải thành viên từ GitHub...
              </div>
            </div>
          </div>

          <BaseTextarea v-model="form.longDescription" label="Bài viết chi tiết" :rows="6"
            placeholder="Mô tả chi tiết về chức năng, công nghệ, hướng dẫn cài đặt..." class="dark:text-white" />
        </BaseCard>

        <BaseCard>
          <AppFormMembers v-model="form.members" />

        </BaseCard>

        <!-- <BaseCard>
          <h3 class="text-lg font-bold text-gray-900 dark:text-white mb-4 border-b border-gray-100 dark:border-gray-700 pb-2">SEO & Tối ưu hoá</h3>
          <BaseInput v-model="form.metaTitle" label="Meta Title" placeholder="Tiêu đề SEO..." class="mb-4 dark:text-white" />
          <BaseTextarea v-model="form.metaDescription" label="Meta Description" placeholder="Mô tả SEO..." class="mb-4 dark:text-white"
            :rows="3" />
          <BaseInput v-model="form.metaKeywords" label="Meta Keywords"
            placeholder="Từ khoá cách nhau bởi dấu phẩy..." class="dark:text-white" />
        </BaseCard> -->
      </div>

      <div class="space-y-6">
        <BaseCard>
          <h3 class="text-sm font-bold text-gray-900 dark:text-white mb-4 uppercase">Trạng thái</h3>

          <div class="mb-4">
            <div class="px-3 py-1.5 rounded text-sm font-bold inline-block" :class="{
              'bg-gray-200 text-gray-700 dark:bg-gray-700 dark:text-gray-300': form.approvalStatus === 'DRAFT',
              'bg-yellow-100 text-yellow-700 dark:bg-yellow-900/30 dark:text-yellow-400': form.approvalStatus === 'PENDING',
              'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400': form.approvalStatus === 'APPROVED',
              'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400': form.approvalStatus === 'REJECTED'
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
              <BaseButton variant="outline" class="!text-red-600 !border-red-200 hover:!bg-red-50 dark:hover:!bg-red-900/20"
                @click="handleStatusChange('REJECTED')">
                Từ chối
              </BaseButton>
              <BaseButton variant="primary" class="!bg-green-600 hover:!bg-green-700"
                @click="handleStatusChange('APPROVED')">
                Phê duyệt
              </BaseButton>
            </div>

            <!-- Allow returning to Draft or Pending from other states if needed, or simple reset -->
            <div v-if="form.approvalStatus && ['APPROVED', 'REJECTED'].includes(form.approvalStatus)"
              class="pt-2 border-t border-gray-100 dark:border-gray-700">
              <p class="text-xs text-center text-gray-400 mb-2">Thay đổi trạng thái</p>
              <div class="grid grid-cols-2 gap-2">
                <BaseButton size="sm" variant="outline" @click="handleStatusChange('DRAFT')">Về Nháp</BaseButton>
                <BaseButton size="sm" variant="secondary" @click="handleStatusChange('PENDING')">Gửi duyệt</BaseButton>
              </div>
            </div>
          </div>
        </BaseCard>

        <BaseCard>
          <h3 class="text-sm font-bold text-gray-900 dark:text-white mb-4 uppercase">Ảnh đại diện</h3>
          <div class="mb-3">
            <MediaUpload v-model="form.thumbnail" label="Ảnh thumbnail" />
          </div>
        </BaseCard>

        <BaseCard>
          <AppFormImages v-model="form.images" />
        </BaseCard>
      </div>
    </div>

    <BaseModal :is-open="showPreviewModal" title="Preview Link" @close="showPreviewModal = false">
      <div class="space-y-4">
        <p class="text-sm text-gray-600 dark:text-gray-300">Chia sẻ link này để xem trước sản phẩm chưa public. Link có hiệu lực trong 24
          giờ.</p>
        <div class="flex gap-2">
          <input type="text" readonly :value="previewUrl"
            class="flex-1 px-3 py-2 border border-gray-200 dark:border-gray-600 bg-gray-50 dark:bg-gray-700 text-gray-900 dark:text-white rounded text-sm outline-none" />
          <BaseButton variant="primary" @click="copyPreviewLink">Sao chép</BaseButton>
        </div>
        <div>
          <a :href="previewUrl" target="_blank" class="text-sm text-blue-600 dark:text-blue-400 hover:underline">Mở preview trong tab mới
            →</a>
        </div>
      </div>
      <template #footer>
        <BaseButton variant="outline" @click="showPreviewModal = false">Đóng</BaseButton>
      </template>
    </BaseModal>

    <!-- Video Preview Modal -->
    <BaseModal :is-open="showVideoModal" title="Preview Video Demo" @close="showVideoModal = false">
      <div class="space-y-4">
        <div v-if="isValidYoutube(form.demoUrl)" class=" aspect-video bg-black rounded overflow-hidden">
          <YouTubeEmbed :url="form.demoUrl" :title="form.name" class="w-full h-full" />
        </div>
        <div v-else class="text-center py-8 bg-gray-50 dark:bg-gray-800 rounded border border-gray-200 dark:border-gray-700 border-dashed">
          <p class="text-gray-500 dark:text-gray-400">Link Demo không phải là định dạng YouTube hợp lệ hoặc đang trống.</p>
          <p class="text-xs text-gray-400 dark:text-gray-500 mt-1">Vui lòng nhập link dạng: https://youtube.com/watch?v=... hoặc
            https://youtu.be/...</p>
        </div>
      </div>
      <template #footer>
        <BaseButton variant="outline" @click="showVideoModal = false">Đóng</BaseButton>
      </template>
    </BaseModal>
  </div>
</template>