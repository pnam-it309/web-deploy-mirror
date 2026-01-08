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
  // Load Apps
  const appRes = await AppService.getAll({ size: 100 });
  apps.value = appRes.content;

  if (isEdit.value) {
    const all = await FeatureService.getAll();
    const found = all.find((f: any) => f.id === route.params.id);
    if (found) {
      Object.assign(form, found);
      if (!form.status) form.status = 'ACTIVE';
      if (!form.videoUrl) form.videoUrl = '';
    }
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

const getAppName = computed(() => apps.value.find(a => a.id === form.appId)?.name || 'Tên Dự Án');
</script>

<template>
  <div class="p-6 h-full overflow-y-auto custom-scrollbar">
    <div class="mb-6 flex justify-between items-start">
      <div>
        <BaseBreadcrumb :items="[
          { label: 'Admin', to: '/admin' },
          { label: 'Chức năng', to: '/admin/features' },
          { label: isEdit ? 'Cập nhật' : 'Thêm mới' }
        ]" />
        <h1 class="text-2xl font-bold text-dark font-serif uppercase">
          {{ isEdit ? 'Cập nhật Chức năng' : 'Thêm Chức năng' }}
        </h1>
      </div>
      <div class="flex gap-3">
        <BaseButton variant="outline" @click="router.back()">Huỷ</BaseButton>
        <BaseButton variant="primary" @click="handleSubmit">Lưu lại</BaseButton>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <BaseCard>
        <h3 class="text-sm font-bold text-dark uppercase mb-4 border-b pb-2">Nội dung</h3>
        <div class="grid grid-cols-2 gap-4">
          <div class="col-span-2">
            <BaseSelect v-model="form.appId" :options="apps.map(a => ({ value: a.id, label: a.name }))"
              label="Thuộc dự án (*)" />
          </div>
          <div class="col-span-2">
            <BaseInput v-model="form.name" label="Tên chức năng (*)" required />
          </div>
          <div class="col-span-2">
            <BaseTextarea v-model="form.description" label="Mô tả chi tiết" :rows="3" />
          </div>

          <!-- Media Section -->
          <div class="space-y-2">
            <MediaUpload v-model="form.imagePreview" label="Ảnh minh hoạ" />
            <BaseInput v-model="form.imagePreview" placeholder="URL ảnh..." />
          </div>

          <div class="space-y-2">
            <MediaUpload v-model="form.videoUrl" label="Video minh hoạ" accept="video/*" />
            <BaseInput v-model="form.videoUrl" placeholder="URL video..." />
          </div>

          <BaseInput v-model="form.sortOrder" type="number" label="Thứ tự hiển thị" />

          <div class="flex flex-col gap-2">
            <label class="text-[11px] font-bold text-dark uppercase tracking-wider">Trạng thái</label>
            <div class="flex items-center gap-6 mt-2">
              <label class="flex items-center gap-2 cursor-pointer">
                <input type="radio" v-model="form.status" value="ACTIVE"
                  class="w-4 h-4 text-primary focus:ring-primary border-gray-300" />
                <span class="text-sm font-medium text-gray-700">Hiển thị</span>
              </label>
              <label class="flex items-center gap-2 cursor-pointer">
                <input type="radio" v-model="form.status" value="INACTIVE"
                  class="w-4 h-4 text-red-500 focus:ring-red-500 border-gray-300" />
                <span class="text-sm font-medium text-gray-700">Ẩn</span>
              </label>
            </div>
          </div>
        </div>
      </BaseCard>

      <div class="space-y-4">
        <h3 class="text-sm font-bold text-gray-500 uppercase tracking-wide">Mô phỏng hiển thị (Landing Page)</h3>

        <div
          class="bg-white rounded-xl overflow-hidden shadow-lg border border-gray-100 transform transition-all hover:shadow-xl">
          <div class="bg-gray-50 px-4 py-2 border-b border-gray-100 flex items-center justify-between">
            <span class="text-[10px] font-bold text-primary uppercase tracking-wider">FEATURE SPOTLIGHT</span>
            <span class="text-[10px] text-gray-400 font-bold uppercase">{{ getAppName }}</span>
          </div>

          <div class="p-6">
            <div class="flex flex-col gap-4">
              <div class="flex-1">
                <h4 class="text-xl font-bold text-dark mb-2">{{ form.name || 'Tiêu đề chức năng' }}</h4>
                <p class="text-sm text-gray-500 leading-relaxed">
                  {{ form.description || 'Mô tả chi tiết về chức năng này.' }}
                </p>
                <div class="mt-4">
                  <span
                    class="inline-block px-3 py-1 bg-dark text-white text-xs font-bold rounded-sm cursor-pointer hover:bg-primary hover:text-dark transition-colors">
                    Trải nghiệm ngay
                  </span>
                </div>
              </div>

              <div class="w-full h-48 bg-gray-100 rounded-lg overflow-hidden border border-gray-200 mt-2">
                <img v-if="form.imagePreview" :src="form.imagePreview" class="w-full h-full object-cover" />
                <div v-else class="w-full h-full flex flex-col items-center justify-center text-gray-400">
                  <svg class="w-10 h-10 mb-2 opacity-50" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  <span class="text-xs">Ảnh minh hoạ</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>