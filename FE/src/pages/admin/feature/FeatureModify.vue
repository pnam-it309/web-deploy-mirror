<script setup lang="ts">
import { reactive, onMounted, computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { FeatureService } from '@/services/admin/feature.service';
import { AppService } from '@/services/admin/app.service';
import { toast } from 'vue3-toastify';

import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import BaseInput from '@/components/base/BaseInput.vue';
import BaseTextarea from '@/components/base/BaseTextarea.vue';
import BaseSelect from '@/components/base/BaseSelect.vue';
import BaseCard from '@/components/base/BaseCard.vue';
import MediaUpload from '@/components/common/MediaUpload.vue';
import YouTubeEmbed from '@/components/common/YouTubeEmbed.vue';

const route = useRoute();
const router = useRouter();
const isEdit = computed(() => !!route.params.id);
const apps = ref<any[]>([]);

const form = reactive({
  id: '',
  name: '',
  description: '',
  appId: '',
  imagePreview: '',
  videoUrl: '',
  sortOrder: 1,
  status: 'ACTIVE'
});

onMounted(async () => {
  try {
    // Load Apps
    const appRes = await AppService.getAll({ size: 100 });
    apps.value = appRes?.content || [];

    if (isEdit.value) {
      const all = await FeatureService.getAll();
      if (Array.isArray(all)) {
        const found = all.find((f: any) => f.id === route.params.id);
        if (found) {
          Object.assign(form, found);
          if (!form.status) form.status = 'ACTIVE';
          if (!form.videoUrl) form.videoUrl = '';
        }
      }
    }
  } catch (error) {
    console.error('Error loading data:', error);
    toast.error('Lỗi khi tải dữ liệu');
  }
});

const handleSubmit = async () => {
  if (!form.appId) return toast.warning('Vui lòng chọn dự án!');
  try {
    if (isEdit.value) await FeatureService.update(form.id, form);
    else await FeatureService.create(form);
    toast.success(isEdit.value ? 'Cập nhật thành công!' : 'Tạo mới thành công!');
    router.push({ name: 'admin-features' });
  } catch (e) {
    toast.error('Lỗi khi lưu dữ liệu');
  }
};

const isYouTubeUrl = (url?: string) => {
  if (!url) return false;
  return /(?:youtube\.com\/watch\?v=|youtu\.be\/|youtube\.com\/embed\/)([^&\n?#]+)/.test(url);
};

const getAppName = computed(() => {
  if (!Array.isArray(apps.value)) return 'Tên Dự Án';
  return apps.value.find((a: any) => a.id === form.appId)?.name || 'Tên Dự Án';
});
</script>

<template>
  <div class="p-4 h-full overflow-y-auto custom-scrollbar bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
    <div class="mb-4 flex justify-between items-start">
      <div>
        <BaseBreadcrumb :items="[
          { label: 'Admin', to: '/admin' },
          { label: 'Chức năng', to: '/admin/features' },
          { label: isEdit ? 'Cập nhật' : 'Thêm mới' }
        ]" />
        <h1 class="text-2xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight mt-1">
          {{ isEdit ? 'Cập nhật Chức năng' : 'Thêm Chức năng' }}
        </h1>
      </div>
      <div class="flex gap-3">
        <BaseButton variant="outline" size="sm" @click="router.back()">Huỷ</BaseButton>
        <BaseButton variant="primary" size="sm" @click="handleSubmit">Lưu lại</BaseButton>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
      <!-- Left Column: Form (7 cols) -->
      <BaseCard class="lg:col-span-7 !p-5 !bg-white dark:!bg-gray-800 !border-gray-200 dark:!border-gray-700 h-fit">
        <h3
          class="text-xs font-bold text-gray-900 dark:text-white uppercase mb-4 border-b border-gray-100 dark:border-gray-700 pb-2">
          Nội dung</h3>
        <div class="grid grid-cols-2 gap-4">
          <div class="col-span-1">
            <BaseSelect v-model="form.appId" :options="apps.map(a => ({ value: a.id, label: a.name }))"
              label="Thuộc dự án (*)" class="dark:text-white" />
          </div>
          <div class="col-span-1">
            <BaseInput v-model="form.name" label="Tên chức năng (*)" required class="dark:text-white" />
          </div>
          <div class="col-span-2">
            <BaseTextarea v-model="form.description" label="Mô tả chi tiết" :rows="2" class="dark:text-white" />
          </div>

          <!-- Media Section: Side by Side -->
          <div class="col-span-1 space-y-2">
            <MediaUpload v-model="form.imagePreview" label="Ảnh minh hoạ" />
            <BaseInput v-model="form.imagePreview" placeholder="URL ảnh..." class="dark:text-white text-xs" />
          </div>

          <div class="col-span-1 space-y-2">
            <div class="flex items-center justify-between">
              <label class="text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Video minh
                hoạ</label>
            </div>
            <MediaUpload v-model="form.videoUrl" label="" accept="video/*" />
            <BaseInput v-model="form.videoUrl" placeholder="YouTube URL..." class="dark:text-white text-xs" />
            <transition name="fade">
              <span v-if="form.videoUrl && !isYouTubeUrl(form.videoUrl) && form.videoUrl.length > 0"
                class="text-[10px] text-orange-500 flex items-center gap-1">
                <svg class="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd"
                    d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z"
                    clip-rule="evenodd" />
                </svg>
                Không phải YouTube
              </span>
            </transition>
          </div>

          <div class="col-span-1">
            <BaseInput v-model="form.sortOrder" type="number" label="Thứ tự" class="dark:text-white" />
          </div>

          <div class="col-span-1 flex flex-col justify-end pb-2">
            <div class="flex items-center gap-4">
              <label class="text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Trạng
                thái:</label>
              <div class="flex items-center gap-4">
                <label class="flex items-center gap-1.5 cursor-pointer group">
                  <input type="radio" v-model="form.status" value="ACTIVE" class="peer sr-only" />
                  <div
                    class="w-4 h-4 border-2 border-gray-300 dark:border-gray-500 rounded-full peer-checked:border-primary peer-checked:bg-primary transition-all">
                  </div>
                  <span
                    class="text-xs font-medium text-gray-700 dark:text-gray-300 group-hover:text-primary transition-colors">Hiện</span>
                </label>
                <label class="flex items-center gap-1.5 cursor-pointer group">
                  <input type="radio" v-model="form.status" value="INACTIVE" class="peer sr-only" />
                  <div
                    class="w-4 h-4 border-2 border-gray-300 dark:border-gray-500 rounded-full peer-checked:border-red-500 peer-checked:bg-red-500 transition-all">
                  </div>
                  <span
                    class="text-xs font-medium text-gray-700 dark:text-gray-300 group-hover:text-red-500 transition-colors">Ẩn</span>
                </label>
              </div>
            </div>
          </div>
        </div>
      </BaseCard>

      <!-- Right Column: Preview (5 cols) -->
      <div class="lg:col-span-5 space-y-4">
        <h3 class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wide">Mô phỏng (Landing Page)
        </h3>

        <div
          class="bg-white dark:bg-gray-800 rounded-xl overflow-hidden shadow-lg border border-gray-100 dark:border-gray-700 transform transition-all hover:shadow-xl">
          <div
            class="bg-gray-50 dark:bg-gray-700/50 px-4 py-2 border-b border-gray-100 dark:border-gray-700 flex items-center justify-between">
            <span class="text-[10px] font-bold text-primary uppercase tracking-wider">FEATURE SPOTLIGHT</span>
            <span class="text-[10px] text-gray-400 dark:text-gray-500 font-bold uppercase truncate max-w-[100px]">{{
              getAppName }}</span>
          </div>

          <div class="p-5">
            <div class="flex flex-col gap-4">
              <div class="flex-1">
                <h4 class="text-lg font-bold text-gray-900 dark:text-white mb-2 leading-tight">
                  {{ form.name || 'Tiêu đề chức năng' }}</h4>
                <p class="text-xs text-gray-500 dark:text-gray-400 leading-relaxed line-clamp-3">
                  {{ form.description || 'Mô tả chi tiết về chức năng này.' }}
                </p>
                <div class="mt-4">
                  <span
                    class="inline-block px-3 py-1.5 bg-gray-900 dark:bg-white text-white dark:text-gray-900 text-[10px] font-bold rounded cursor-pointer hover:bg-primary dark:hover:bg-primary hover:text-white transition-colors uppercase tracking-wide">
                    Trải nghiệm ngay
                  </span>
                </div>
              </div>

              <div
                class="w-full h-32 bg-gray-100 dark:bg-gray-700 rounded-lg overflow-hidden border border-gray-200 dark:border-gray-600 mt-1 shadow-inner relative group">
                <!-- YouTube Embed Preview -->
                <YouTubeEmbed v-if="form.videoUrl && isYouTubeUrl(form.videoUrl)" :url="form.videoUrl"
                  :title="form.name || 'Feature Preview'" class="w-full h-full" />
                <!-- Image Preview -->
                <img v-else-if="form.imagePreview" :src="form.imagePreview" class="w-full h-full object-cover" />
                <!-- Placeholder -->
                <div v-else
                  class="w-full h-full flex flex-col items-center justify-center text-gray-400 dark:text-gray-500">
                  <svg class="w-8 h-8 mb-1 opacity-50 text-gray-400" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  <span class="text-[10px]">Preview</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>