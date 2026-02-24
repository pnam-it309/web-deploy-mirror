<script setup lang="ts">
import { reactive, onMounted, computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useSidebarStore } from '@/stores/sidebar.store';
import { DomainService } from '@/services/admin/domain.service';
import { toast } from 'vue3-toastify';
import { decodeId } from '@/utils';


import BaseButton from '@/components/base/BaseButton.vue';
import BaseInput from '@/components/base/BaseInput.vue';
import BaseTextarea from '@/components/base/BaseTextarea.vue';

import BaseIconPicker from '@/components/base/BaseIconPicker.vue';

const route = useRoute();
const router = useRouter();
const sidebarStore = useSidebarStore();
const isEdit = computed(() => !!route.params.id);
const isLoading = ref(false);

const form = reactive({
  id: '',
  name: '',
  description: '',
  icon: '',
  color: '#3b82f6', // Màu mặc định (Blue-500)
  status: 'ACTIVE',
  sortOrder: 1
});

onMounted(async () => {
  if (isEdit.value) {
    isLoading.value = true;
    const all = await DomainService.getAll();
    const domainId = decodeId(route.params.id as string);
    const found = all.find((d: any) => d.id === domainId);
    if (found) Object.assign(form, found);

    // Nếu dữ liệu cũ chưa có color thì set mặc định
    if (!form.color) form.color = '#3b82f6';
    if (!form.status) form.status = 'ACTIVE';
    if (form.sortOrder === undefined || form.sortOrder === null) form.sortOrder = 1;

    isLoading.value = false;
  }
});

const errors = reactive({
  name: '',
  icon: '',
  description: ''
});

const validateForm = (): boolean => {
  errors.name = '';
  errors.icon = '';
  errors.description = '';

  let valid = true;
  if (!form.name || !form.name.trim()) {
    errors.name = 'Tên lĩnh vực không được để trống';
    valid = false;
  }
  if (!form.icon || !form.icon.trim()) {
    errors.icon = 'Biểu tượng không được để trống';
    valid = false;
  }
  if (!form.description || !form.description.trim()) {
    errors.description = 'Mô tả ngắn không được để trống';
    valid = false;
  }
  return valid;
};

const handleSubmit = async () => {
  if (!validateForm()) {
    toast.error('Vui lòng điền đầy đủ các trường bắt buộc!');
    return;
  }
  try {
    if (isEdit.value) await DomainService.update(form.id, form);
    else await DomainService.create(form);
    toast.success(isEdit.value ? 'Cập nhật thành công!' : 'Tạo mới thành công!');
    router.push({ name: 'admin-domains' });
  } catch (e: any) {
    const msg = e.response?.data?.message || e.response?.data?.error;
    toast.error(msg || 'Lỗi khi lưu dữ liệu!');
  }
};
</script>

<template>
  <div class="p-6 h-full overflow-y-auto custom-scrollbar bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
    <div class="mb-6 flex justify-between items-center">
      <div></div>
      <div class="flex gap-3">
        <BaseButton variant="outline" @click="router.back()">Huỷ bỏ</BaseButton>
        <BaseButton variant="primary" @click="handleSubmit">Lưu lại</BaseButton>
      </div>
    </div>

    <div class="grid grid-cols-1 gap-8 transition-all duration-300"
      :class="[sidebarStore.isDesktopExpanded ? 'xl:grid-cols-2' : 'lg:grid-cols-2']">
      
      <!-- Input Form -->
      <div class="bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm p-6 space-y-6">
        <h3 class="text-sm font-bold text-gray-900 dark:text-white uppercase mb-6 border-b border-gray-100 dark:border-gray-700 pb-3">Thông tin nhập liệu</h3>
        <div class="space-y-6">
          <BaseInput v-model="form.name" label="Tên lĩnh vực (*)" placeholder="VD: E-commerce" required class="dark:text-white" :error="errors.name" />

          <div class="grid grid-cols-2 gap-5">
            <BaseIconPicker v-model="form.icon" label="Biểu tượng" class="dark:text-white" :error="errors.icon" />

            <div class="flex flex-col gap-2">
              <label class="text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Màu chủ đạo</label>
              <div class="flex items-center gap-3">
                <input type="color" v-model="form.color"
                  class="h-[46px] w-[60px] p-1 bg-white dark:bg-gray-700 border border-gray-200 dark:border-gray-600 rounded-lg cursor-pointer shadow-sm" />
                <input type="text" v-model="form.color"
                  class="flex-1 px-3 h-[46px] border border-gray-200 dark:border-gray-600 dark:bg-gray-700 dark:text-white rounded-lg text-sm uppercase font-mono shadow-sm focus:ring-2 focus:ring-primary/20 transition-all outline-none"
                  maxlength="7" />
              </div>
            </div>

            <BaseInput v-model.number="form.sortOrder" type="number" label="Thứ tự hiển thị" placeholder="1" class="dark:text-white" />

            <div class="flex flex-col gap-2">
              <label class="text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Trạng thái</label>
              <div class="flex items-center gap-6 mt-3">
                <label class="flex items-center gap-2 cursor-pointer group">
                  <div class="relative flex items-center">
                    <input type="radio" v-model="form.status" value="ACTIVE"
                      class="peer sr-only" />
                    <div class="w-5 h-5 border-2 border-gray-300 dark:border-gray-500 rounded-full peer-checked:border-blue-600 peer-checked:bg-blue-600 transition-all"></div>
                  </div>
                  <span class="text-sm font-bold text-gray-700 dark:text-gray-300 group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">Hiển thị</span>
                </label>
                <label class="flex items-center gap-2 cursor-pointer group">
                  <div class="relative flex items-center">
                    <input type="radio" v-model="form.status" value="INACTIVE"
                      class="peer sr-only" />
                    <div class="w-5 h-5 border-2 border-gray-300 dark:border-gray-500 rounded-full peer-checked:border-red-500 peer-checked:bg-red-500 transition-all"></div>
                  </div>
                  <span class="text-sm font-bold text-gray-700 dark:text-gray-300 group-hover:text-red-500 transition-colors">Ẩn</span>
                </label>
              </div>
            </div>
          </div>

          <BaseTextarea v-model="form.description" label="Mô tả ngắn" placeholder="Mô tả về lĩnh vực này..."
            :rows="4" class="dark:text-white" :error="errors.description" />
        </div>
      </div>

      <!-- Preview -->
      <div class="space-y-6">
        <h3 class="text-sm font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wide">Xem trước (Giao diện người dùng)</h3>

        <div
          class="bg-white dark:bg-gray-800 rounded-2xl shadow-lg border border-gray-100 dark:border-gray-700 p-8 flex items-start gap-5 max-w-md mx-auto transform transition-all hover:-translate-y-1 hover:shadow-2xl cursor-default group">
          <div
            class="w-14 h-14 rounded-xl flex items-center justify-center text-3xl shrink-0 transition-all duration-300 shadow-sm"
            :style="{ backgroundColor: form.color + '20', color: form.color }">
            <i v-if="form.icon" :class="form.icon"></i>
            <span v-else class="font-bold text-xs">ICON</span>
          </div>

          <div>
            <h4 class="text-xl font-bold text-gray-900 dark:text-white mb-2 group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">
              {{ form.name || 'Tên Lĩnh Vực' }}</h4>
            <p class="text-sm text-gray-500 dark:text-gray-400 leading-relaxed line-clamp-2">
              <span
                v-text="form.description || 'Mô tả ngắn của lĩnh vực sẽ hiển thị ở đây. Ví dụ: Các giải pháp thương mại điện tử hiện đại...'">
              </span>
            </p>
            <div class="mt-4 text-xs font-bold uppercase tracking-wider flex items-center gap-1 transition-colors group-hover:gap-2"
              :style="{ color: form.color }">
              Xem dự án <span class="text-lg leading-none">&rarr;</span>
            </div>
          </div>
        </div>

        <p class="text-center text-xs text-gray-400 dark:text-gray-500 italic mt-6">
          * Icon và màu sắc sẽ được sử dụng đồng bộ trên toàn bộ website.
        </p>
      </div>
    </div>
  </div>
</template>