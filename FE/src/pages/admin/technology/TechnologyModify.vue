<script setup lang="ts">
import { reactive, onMounted, computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { TechnologyService } from '@/services/admin/technology.service';

import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import BaseInput from '@/components/base/BaseInput.vue';
import BaseCard from '@/components/base/BaseCard.vue';

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
    router.push({ name: 'admin-technologies' });
  } catch (e) { alert('Lỗi lưu'); }
};
</script>

<template>
  <div class="p-6 h-full overflow-y-auto custom-scrollbar">
    <div class="mb-6 flex justify-between items-start">
      <div>
        <BaseBreadcrumb :items="[
          { label: 'Admin', to: '/admin' }, 
          { label: 'Công nghệ', to: '/admin/technologies' },
          { label: isEdit ? 'Cập nhật' : 'Thêm mới' }
        ]" />
        <h1 class="text-2xl font-bold text-dark font-serif uppercase">{{ isEdit ? 'Cập nhật Công nghệ' : 'Thêm Công nghệ' }}</h1>
      </div>
      <div class="flex gap-3">
        <BaseButton variant="outline" @click="router.back()">Huỷ</BaseButton>
        <BaseButton variant="primary" @click="handleSubmit">Lưu lại</BaseButton>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <BaseCard>
        <h3 class="text-sm font-bold text-dark uppercase mb-4 border-b pb-2">Thông tin</h3>
        <div class="space-y-5">
          <BaseInput v-model="form.name" label="Tên công nghệ (*)" placeholder="VD: Java, React..." required />
          <BaseInput v-model="form.icon" label="URL Logo / Icon" placeholder="Link ảnh logo..." />
        </div>
      </BaseCard>

      <div class="space-y-6">
        <h3 class="text-sm font-bold text-gray-500 uppercase tracking-wide">Live Preview</h3>
        
        <div class="bg-white p-6 rounded-lg border border-gray-100 shadow-md text-center">
          <div class="w-20 h-20 mx-auto bg-gray-50 rounded-full flex items-center justify-center overflow-hidden mb-3 border border-gray-200">
             <img v-if="form.icon" :src="form.icon" class="w-14 h-14 object-contain" />
             <span v-else class="text-2xl font-bold text-gray-300">{{ form.name.charAt(0) || '?' }}</span>
          </div>
          <p class="font-bold text-lg text-dark">{{ form.name || 'Tên Công Nghệ' }}</p>
          <p class="text-xs text-gray-400 mt-1">Hiển thị dạng lưới</p>
        </div>

        <div class="bg-white p-6 rounded-lg border border-gray-100 shadow-md">
           <p class="text-xs text-gray-400 mb-2">Hiển thị trong thẻ dự án:</p>
           <div class="flex gap-2">
              <div class="flex items-center gap-1 bg-gray-100 px-2 py-1 rounded text-xs font-medium text-dark border border-gray-200">
                <img v-if="form.icon" :src="form.icon" class="w-3 h-3 object-contain" />
                {{ form.name || 'Tech Name' }}
              </div>
              <div class="flex items-center gap-1 bg-gray-100 px-2 py-1 rounded text-xs font-medium text-dark border border-gray-200 opacity-50">
                Spring Boot
              </div>
           </div>
        </div>
      </div>
    </div>
  </div>
</template>