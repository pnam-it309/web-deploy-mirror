<script setup lang="ts">
import { onMounted, reactive, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAppStore } from '@/stores/app.store';
import { DomainService } from '@/services/admin/domain.service';
import { TechnologyService } from '@/services/admin/technology.service';
import type { AppCreateRequest, AppUpdateRequest, Domain, Technology } from '@/types/admin.types';

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

const route = useRoute();
const router = useRouter();
const appStore = useAppStore();

const appId = route.params.id as string;
const isEdit = computed(() => !!appId);
const domains = ref<Domain[]>([]);
const technologies = ref<Technology[]>([]);
const form = reactive<AppUpdateRequest>({
  name: '',
  sku: '',
  shortDescription: '',
  thumbnail: '',
  domainId: '',
  technologyIds: [],
  approvalStatus: 'PENDING',
  members: [],
  images: []
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
    }
  } catch (e) {
    console.error("Init Error", e);
  }
});

const handleSubmit = async () => {
  try {
    const cleanMembers = form.members.filter(m => m.customerId && m.customerId.trim() !== '');
    const cleanImages = form.images.filter(i => i.url && i.url.trim() !== '');

    const payload = {
      ...form,
      members: cleanMembers,
      images: cleanImages
    };

    if (isEdit.value) {
      await appStore.updateApp(appId, payload);
      alert("Cập nhật thành công!");
    } else {
      const res = await appStore.createApp(payload as AppCreateRequest);
      if (cleanMembers.length > 0 || cleanImages.length > 0) {
        await appStore.updateApp(res.id, payload);
      }
      alert("Tạo mới thành công!");
    }
    router.push('/admin/apps');
  } catch (e) {
    console.error(e);
    alert("Có lỗi xảy ra. Vui lòng kiểm tra lại console.");
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
            <BaseSelect 
              v-model="form.domainId" 
              :options="domains.map(d => ({ value: d.id, label: d.name }))"
              label="Lĩnh vực (*)"
            />
            
            <BaseMultiSelect 
              v-model="form.technologyIds"
              :options="technologies.map(t => ({ value: t.id, label: t.name }))"
              label="Công nghệ sử dụng"
              placeholder="Chọn các công nghệ..."
            />
          </div>

          <BaseTextarea v-model="form.shortDescription" label="Mô tả ngắn" placeholder="Giới thiệu tóm tắt về dự án..." />
        </BaseCard>

        <BaseCard>
          <AppFormMembers v-model="form.members" />
        </BaseCard>
      </div>

      <div class="space-y-6">
        <BaseCard>
          <h3 class="text-sm font-bold text-dark mb-4 uppercase">Trạng thái</h3>
          <BaseSelect 
            v-model="form.approvalStatus" 
            :options="[{ value: 'PENDING', label: 'Chờ duyệt' }, { value: 'APPROVED', label: 'Đã duyệt' }, { value: 'REJECTED', label: 'Từ chối' }]"
            label="Tình trạng duyệt"
          />
        </BaseCard>

        <BaseCard>
          <h3 class="text-sm font-bold text-dark mb-4 uppercase">Ảnh đại diện</h3>
          <div class="mb-3">
             <div v-if="form.thumbnail" class="aspect-video rounded-sm overflow-hidden bg-gray-100 mb-2 border border-gray-200">
               <img :src="form.thumbnail" class="w-full h-full object-cover" />
             </div>
             <BaseInput v-model="form.thumbnail" placeholder="URL ảnh thumbnail..." />
          </div>
        </BaseCard>

        <BaseCard>
           <AppFormImages v-model="form.images" />
        </BaseCard>
      </div>
    </div>
  </div>
</template>