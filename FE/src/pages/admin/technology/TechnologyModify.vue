<script setup lang="ts">
import { reactive, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { TechnologyService } from '@/services/admin/technology.service';
import { toast } from 'vue3-toastify';

import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import BaseInput from '@/components/base/BaseInput.vue';


const route = useRoute();
const router = useRouter();
const isEdit = computed(() => !!route.params.id);
const form = reactive({ id: '', name: '', icon: '' });

onMounted(async () => {
  if (isEdit.value) {
    const all = await TechnologyService.getAll();
    const found = all.find((d: any) => d.id === route.params.id);
    if (found) Object.assign(form, found);
  }
});

const handleSubmit = async () => {
  try {
    if (isEdit.value) await TechnologyService.update(form.id, form);
    else await TechnologyService.create(form);
    toast.success(isEdit.value ? 'Cập nhật thành công!' : 'Tạo mới thành công!');
    router.push({ name: 'admin-technologies' });
  } catch (e) {
    toast.error('Lỗi khi lưu dữ liệu!');
  }
};
</script>

<template>
  <div class="p-6 h-full overflow-y-auto custom-scrollbar bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
    <div class="mb-6 flex justify-between items-start">
      <div>
        <BaseBreadcrumb :items="[
          { label: 'Admin', to: '/admin' },
          { label: 'Công nghệ', to: '/admin/technologies' },
          { label: isEdit ? 'Cập nhật' : 'Thêm mới' }
        ]" />
        <h1 class="text-3xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight mt-2">
          {{ isEdit ? 'Cập nhật Công nghệ' : 'Thêm Công nghệ' }}
        </h1>
      </div>
      <div class="flex gap-3">
        <BaseButton variant="outline" @click="router.back()">Huỷ</BaseButton>
        <BaseButton variant="primary" @click="handleSubmit">Lưu lại</BaseButton>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <div class="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm p-6 space-y-6">
        <h3 class="text-sm font-bold text-gray-900 dark:text-white uppercase mb-6 border-b border-gray-100 dark:border-gray-700 pb-3">Thông tin</h3>
        <div class="space-y-6">
          <BaseInput v-model="form.name" label="Tên công nghệ (*)" placeholder="VD: Java, React..." required class="dark:text-white" />
          <BaseInput v-model="form.icon" label="URL Logo / Icon" placeholder="Link ảnh logo..." class="dark:text-white" />
        </div>
      </div>

      <div class="space-y-6">
        <h3 class="text-sm font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wide">Live Preview</h3>

        <div class="bg-white dark:bg-gray-800 p-8 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm text-center transition-all hover:shadow-lg">
          <div
            class="w-24 h-24 mx-auto bg-gray-50 dark:bg-gray-700 rounded-full flex items-center justify-center overflow-hidden mb-4 border border-gray-100 dark:border-gray-600 shadow-inner">
            <img v-if="form.icon" :src="form.icon" class="w-16 h-16 object-contain" />
            <span v-else class="text-3xl font-bold text-gray-300 dark:text-gray-500">{{ form.name.charAt(0) || '?' }}</span>
          </div>
          <p class="font-bold text-xl text-gray-900 dark:text-white">{{ form.name || 'Tên Công Nghệ' }}</p>
          <p class="text-sm text-gray-500 dark:text-gray-400 mt-2">Hiển thị dạng lưới</p>
        </div>

        <div class="bg-white dark:bg-gray-800 p-6 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm">
          <p class="text-xs text-gray-400 dark:text-gray-500 mb-3 uppercase font-bold tracking-wider">Hiển thị trong thẻ dự án:</p>
          <div class="flex gap-3">
            <div
              class="flex items-center gap-2 bg-gray-50 dark:bg-gray-700 px-3 py-1.5 rounded-lg text-sm font-medium text-gray-900 dark:text-white border border-gray-200 dark:border-gray-600 shadow-sm border-b-2 border-b-gray-300 dark:border-b-gray-500">
              <img v-if="form.icon" :src="form.icon" class="w-4 h-4 object-contain" />
              {{ form.name || 'Tech Name' }}
            </div>
            <div
              class="flex items-center gap-2 bg-gray-50 dark:bg-gray-700 px-3 py-1.5 rounded-lg text-sm font-medium text-gray-900 dark:text-white border border-gray-200 dark:border-gray-600 opacity-50 border-b-2 border-b-gray-300 dark:border-b-gray-500">
              Spring Boot
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>