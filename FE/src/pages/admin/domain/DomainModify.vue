<script setup lang="ts">
import { reactive, onMounted, computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { DomainService } from '@/services/admin/domain.service';

import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BaseButton from '@/components/base/BaseButton.vue';
import BaseInput from '@/components/base/BaseInput.vue';
import BaseTextarea from '@/components/base/BaseTextarea.vue';
import BaseCard from '@/components/base/BaseCard.vue';
import BaseIconPicker from '@/components/base/BaseIconPicker.vue'; // Import mới

const route = useRoute();
const router = useRouter();
const isEdit = computed(() => !!route.params.id);
const isLoading = ref(false);

const form = reactive({
  id: '',
  name: '',
  description: '',
  icon: '',
  color: '#3b82f6' // Màu mặc định (Blue-500)
});

onMounted(async () => {
  if (isEdit.value) {
    isLoading.value = true;
    const all = await DomainService.getAll();
    const found = all.find((d: any) => d.id === route.params.id);
    if (found) Object.assign(form, found);
    
    // Nếu dữ liệu cũ chưa có color thì set mặc định
    if (!form.color) form.color = '#3b82f6';
    
    isLoading.value = false;
  }
});

const handleSubmit = async () => {
  try {
    if (isEdit.value) await DomainService.update(form.id, form);
    else await DomainService.create(form);
    router.push({ name: 'admin-domains' }); // Đảm bảo đúng route name
  } catch (e) { alert('Lỗi lưu dữ liệu'); }
};
</script>

<template>
  <div class="p-6 h-full overflow-y-auto custom-scrollbar">
    <div class="mb-6 flex justify-between items-start">
      <div>
        <BaseBreadcrumb :items="[
          { label: 'Admin', to: '/admin' }, 
          { label: 'Lĩnh vực', to: '/admin/domains' },
          { label: isEdit ? 'Cập nhật' : 'Tạo mới' }
        ]" />
        <h1 class="text-2xl font-bold text-dark font-serif uppercase">{{ isEdit ? 'Cập nhật Lĩnh vực' : 'Thêm Lĩnh vực mới' }}</h1>
      </div>
      <div class="flex gap-3">
        <BaseButton variant="outline" @click="router.back()">Huỷ bỏ</BaseButton>
        <BaseButton variant="primary" @click="handleSubmit">Lưu lại</BaseButton>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <BaseCard>
        <h3 class="text-sm font-bold text-dark uppercase mb-4 border-b pb-2">Thông tin nhập liệu</h3>
        <div class="space-y-5">
          <BaseInput v-model="form.name" label="Tên lĩnh vực (*)" placeholder="VD: E-commerce" required />
          
          <div class="grid grid-cols-2 gap-4">
             <BaseIconPicker v-model="form.icon" label="Biểu tượng" />
             
             <div class="flex flex-col gap-1.5">
                <label class="text-[11px] font-bold text-dark uppercase tracking-wider">Màu chủ đạo</label>
                <div class="flex items-center gap-3">
                   <input 
                      type="color" 
                      v-model="form.color" 
                      class="h-[42px] w-[60px] p-1 bg-white border border-gray-200 rounded-sm cursor-pointer"
                   />
                   <input 
                      type="text" 
                      v-model="form.color"
                      class="flex-1 px-3 py-2.5 border border-gray-200 rounded-sm text-sm uppercase font-mono"
                      maxlength="7"
                   />
                </div>
             </div>
          </div>

          <BaseTextarea v-model="form.description" label="Mô tả ngắn" placeholder="Mô tả về lĩnh vực này..." :rows="4" />
        </div>
      </BaseCard>

      <div class="space-y-4">
        <h3 class="text-sm font-bold text-gray-500 uppercase tracking-wide">Xem trước (Giao diện người dùng)</h3>
        
        <div class="bg-white rounded-xl shadow-lg border border-gray-100 p-6 flex items-start gap-4 max-w-md mx-auto transform transition-all hover:-translate-y-1 hover:shadow-xl cursor-default group">
          <div 
             class="w-12 h-12 rounded-lg flex items-center justify-center text-2xl shrink-0 transition-colors duration-300"
             :style="{ backgroundColor: form.color + '20', color: form.color }" 
          >
             <i v-if="form.icon" :class="form.icon"></i>
             <span v-else class="font-bold text-sm">ICON</span>
          </div>
          
          <div>
            <h4 class="text-lg font-bold text-dark mb-1 group-hover:text-primary transition-colors">{{ form.name || 'Tên Lĩnh Vực' }}</h4>
            <p class="text-sm text-gray-500 leading-relaxed line-clamp-2">
              {{ form.description || 'Mô tả ngắn của lĩnh vực sẽ hiển thị ở đây. Ví dụ: Các giải pháp thương mại điện tử hiện đại...' }}
            </p>
            <div 
               class="mt-3 text-xs font-bold uppercase tracking-wider flex items-center gap-1 transition-colors"
               :style="{ color: form.color }"
            >
              Xem dự án <span class="text-lg leading-none">&rarr;</span>
            </div>
          </div>
        </div>

        <p class="text-center text-xs text-gray-400 italic mt-4">
          * Icon và màu sắc sẽ được sử dụng đồng bộ trên toàn bộ website.
        </p>
      </div>
    </div>
  </div>
</template>