<template>
  <AdminLayout>
    <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
      <div class="flex items-center justify-between mb-6">
        <h2 class="text-xl font-bold text-gray-800">
          {{ isEdit ? 'Cập nhật sản phẩm' : 'Tạo sản phẩm mới' }}
        </h2>
        <div class="flex gap-3">
          <button @click="$router.push({ name: 'admin-apps' })"
            class="px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors">
            Hủy
          </button>
          <button @click="submitForm" :disabled="loading"
            class="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors flex items-center gap-2">
            <svg v-if="loading" class="animate-spin h-5 w-5 text-white" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor"
                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
              </path>
            </svg>
            {{ isEdit ? 'Lưu thay đổi' : 'Tạo sản phẩm' }}
          </button>
        </div>
      </div>

      <!-- Tabs -->
      <div class="border-b border-gray-200 mb-6">
        <nav class="-mb-px flex space-x-8" aria-label="Tabs">
          <button v-for="tab in tabs" :key="tab.id" @click="currentTab = tab.id" :class="[
            currentTab === tab.id
              ? 'border-indigo-500 text-indigo-600'
              : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300',
            'whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm'
          ]">
            {{ tab.name }}
          </button>
        </nav>
      </div>

      <!-- Tab Content -->
      <div v-show="currentTab === 'general'" class="space-y-6">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- Basic Info -->
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên sản phẩm <span
                  class="text-red-500">*</span></label>
              <input v-model="form.name" type="text"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500"
                placeholder="Nhập tên sản phẩm..." required />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã SKU</label>
              <input v-model="form.sku" type="text"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500"
                placeholder="SKU-001" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Lĩnh vực <span
                  class="text-red-500">*</span></label>
              <select v-model="form.domainId"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500">
                <option value="" disabled>Chọn lĩnh vực</option>
                <option v-for="domain in domains" :key="domain.id" :value="domain.id">{{ domain.name }}</option>
              </select>
            </div>
          </div>

          <!-- Links & Status -->
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Demo URL</label>
              <input v-model="form.demoUrl" type="url"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500"
                placeholder="https://demo.example.com" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Source Code URL</label>
              <input v-model="form.sourceUrl" type="url"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500"
                placeholder="https://github.com/..." />
            </div>
            <div class="flex items-center gap-4 mt-8">
              <label class="flex items-center gap-2 cursor-pointer">
                <input v-model="form.isFeatured" type="checkbox"
                  class="w-5 h-5 text-indigo-600 border-gray-300 rounded focus:ring-indigo-500" />
                <span class="text-sm font-medium text-gray-700">Sản phẩm nổi bật</span>
              </label>
            </div>

            <!-- Approval Status -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái duyệt</label>
              <select v-model="form.approvalStatus"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500">
                <option value="DRAFT">Nháp (Draft)</option>
                <option value="PENDING">Chờ duyệt (Pending)</option>
                <option value="APPROVED">Đã duyệt (Approved)</option>
                <option value="REJECTED">Từ chối (Rejected)</option>
              </select>
            </div>
          </div>
        </div>

        <!-- Descriptions -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Mô tả ngắn</label>
          <textarea v-model="form.shortDescription" rows="3"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500"
            placeholder="Tóm tắt về sản phẩm..."></textarea>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Mô tả chi tiết</label>
          <textarea v-model="form.longDescription" rows="6"
            class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500"
            placeholder="Chi tiết về tính năng, công nghệ..."></textarea>
        </div>
      </div>

      <div v-show="currentTab === 'technology'" class="space-y-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-3">Công nghệ sử dụng</label>
          <div v-if="technologies.length > 0" class="grid grid-cols-2 md:grid-cols-4 gap-4">
            <label v-for="tech in technologies" :key="tech.id"
              class="flex items-center p-3 border rounded-lg cursor-pointer transition-colors"
              :class="form.technologyIds.includes(tech.id) ? 'border-indigo-500 bg-indigo-50' : 'border-gray-200 hover:border-gray-300'">
              <input type="checkbox" :value="tech.id" v-model="form.technologyIds" class="hidden" />
              <img v-if="tech.image" :src="tech.image" class="w-6 h-6 mr-3 object-contain" />
              <span class="text-sm font-medium"
                :class="form.technologyIds.includes(tech.id) ? 'text-indigo-700' : 'text-gray-700'">{{ tech.name
                }}</span>
            </label>
          </div>
          <p v-else class="text-gray-500 italic">Chưa có công nghệ nào được định nghĩa.</p>
        </div>
      </div>

      <!-- Media Tab -->
      <div v-show="currentTab === 'media'" class="space-y-8">
        <!-- Thumbnail Section -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Ảnh đại diện (Thumbnail)</label>
          <div class="flex items-center gap-6">
            <div
              class="w-32 h-32 border-2 border-dashed border-gray-300 rounded-lg flex items-center justify-center overflow-hidden bg-gray-50 bg-cover bg-center"
              :style="previewThumbnail ? { backgroundImage: `url(${previewThumbnail})` } : {}">
              <span v-if="!previewThumbnail" class="text-gray-400 text-xs text-center px-2">Chưa có ảnh</span>
            </div>
            <div>
              <input type="file" ref="fileInput" @change="handleFileChange" accept="image/*" class="hidden" />
              <button type="button" @click="fileInput?.click()"
                class="px-4 py-2 border border-gray-300 bg-white text-gray-700 rounded-lg hover:bg-gray-50 text-sm font-medium">
                Chọn ảnh
              </button>
              <p class="text-xs text-gray-500 mt-2">Định dạng JPG, PNG. Tối đa 5MB.</p>
            </div>
          </div>
        </div>

        <!-- Gallery Section -->
        <div>
          <div class="flex justify-between items-center mb-4">
            <label class="block text-sm font-medium text-gray-700">Thư viện ảnh (Gallery)</label>
            <button v-if="isEdit" @click="galleryInput?.click()"
              class="px-3 py-1.5 bg-indigo-50 text-indigo-700 rounded text-sm font-medium hover:bg-indigo-100">
              + Thêm ảnh
            </button>
          </div>
          <input type="file" ref="galleryInput" multiple @change="handleGalleryUpload" accept="image/*"
            class="hidden" />

          <div v-if="isEdit" class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-5 gap-4">
            <div v-for="(img, idx) in galleryImages" :key="idx"
              class="relative group aspect-square bg-gray-100 rounded-lg overflow-hidden">
              <img :src="img.url" class="w-full h-full object-cover" />
              <div
                class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-40 transition-all flex items-center justify-center opacity-0 group-hover:opacity-100 gap-2">
                <button @click="deleteGalleryImg(img.id)"
                  class="p-1 bg-white text-red-600 rounded-full shadow hover:bg-red-50">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd"
                      d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                      clip-rule="evenodd" />
                  </svg>
                </button>
              </div>
            </div>
            <div v-if="galleryImages.length === 0"
              class="col-span-full text-center py-8 bg-gray-50 border border-dashed rounded-lg text-gray-400 text-sm">
              Chưa có ảnh thư viện
            </div>
          </div>
          <div v-else class="text-sm text-gray-500 bg-gray-50 p-4 rounded-lg">
            Vui lòng lưu sản phẩm trước khi upload thư viện ảnh.
          </div>
        </div>

      </div>

      <!-- Feature Tab -->
      <div v-show="currentTab === 'features'" class="space-y-6">
        <div v-if="isEdit" class="space-y-4">
          <AdminProductFeatures :app-id="route.params.id as string" />
        </div>
        <div v-else class="text-center py-8 text-gray-500 bg-gray-50 rounded-lg">
          Vui lòng lưu sản phẩm trước khi quản lý chức năng.
        </div>
      </div>

      <!-- Team Tab -->
      <div v-show="currentTab === 'team'" class="space-y-6">
        <div v-if="isEdit" class="space-y-4">
          <AdminProductTeam :app-id="route.params.id as string" />
        </div>
        <div v-else class="text-center py-8 text-gray-500 bg-gray-50 rounded-lg">
          Vui lòng lưu sản phẩm trước khi quản lý nhóm.
        </div>
      </div>

    </div>
  </AdminLayout>
</template>

<script setup lang="ts">
import AdminLayout from '@/layouts/admin/AdminLayout.vue';
import { ref, reactive, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import AdminProductFeatures from './AdminProductFeatures.vue'; // Import
import AdminProductTeam from './AdminProductTeam.vue'; // Import
// Services
import {
  getAppDetail,
  createApp,
  updateApp,
  uploadAppImage,
  uploadGallery,
  deleteGalleryImage // Need to export these from service
} from '@/services/admin/app.service';
import { getDomains } from '@/services/admin/domain.service';
import { getTechnologies } from '@/services/admin/technology.service';

const router = useRouter();
const route = useRoute();

const isEdit = computed(() => !!route.params.id);
const loading = ref(false);
const currentTab = ref('general');
const galleryImages = ref<any[]>([]); // { id, url, isMain }
const previewThumbnail = ref('');
const fileInput = ref<HTMLInputElement | null>(null);
const galleryInput = ref<HTMLInputElement | null>(null);

const tabs = [
  { id: 'general', name: 'Thông tin chung' },
  { id: 'technology', name: 'Công nghệ' },
  { id: 'media', name: 'Hình ảnh & Media' },
  { id: 'features', name: 'Chức năng' },
  { id: 'team', name: 'Nhóm phát triển' },
];

const domains = ref<any[]>([]);
const technologies = ref<any[]>([]); // Need to fetch this

const form = reactive({
  name: '',
  sku: '',
  price: 0,
  shortDescription: '',
  longDescription: '',
  thumbnail: '',
  domainId: '',
  brandId: null, // Optional
  technologyIds: [] as string[],
  demoUrl: '',
  sourceUrl: '',
  isFeatured: false,
  homepageSortOrder: 0,
  approvalStatus: 'DRAFT',
});

// Mock Technologies if service not ready, or better, fetch them.
// I'll try to fetch or use a placeholder if api fails.
const fetchDependencies = async () => {
  try {
    const dRes = await getDomains();
    domains.value = dRes || [];

    const tRes = await getTechnologies();
    technologies.value = tRes || [];
  } catch (e) {
    console.error('Failed to load dependencies', e);
  }
};

const fetchDetail = async () => {
  if (!isEdit.value) return;
  loading.value = true;
  try {
    const res = await getAppDetail(route.params.id as string);
    const data = res || {}; // Adapt to response wrapper

    Object.assign(form, {
      name: data.name,
      sku: data.sku,
      price: data.price,
      shortDescription: data.shortDescription,
      longDescription: data.longDescription,
      thumbnail: data.thumbnail,
      domainId: data.domainId,
      brandId: data.brandId,
      technologyIds: data.technologyIds || [], // Ensure backend sends this or we map it
      demoUrl: data.demoUrl,
      sourceUrl: data.sourceUrl,
      isFeatured: data.isFeatured,
      homepageSortOrder: data.homepageSortOrder,
      approvalStatus: data.approvalStatus || 'DRAFT'
    });

    if (data.thumbnail) previewThumbnail.value = data.thumbnail;
    // Map Gallery if Backend provides it
    // Check Backend `toResponse` of AdminAppController. `AdminAppResponse` doesn't seem to have `images` list yet?
    // Let's check AdminAppResponse.java.
    // If not, we have GAP: Admin doesn't see Gallery images on Load.
    if (data.images) {
      galleryImages.value = data.images;
    }

  } catch (error) {
    console.error(error);
  } finally {
    loading.value = false;
  }
}

const pendingFile = ref<File | null>(null);

const handleFileChange = async (e: Event) => {
  const input = e.target as HTMLInputElement;
  if (input.files && input.files[0]) {
    const file = input.files[0];
    previewThumbnail.value = URL.createObjectURL(file);
    pendingFile.value = file;
  }
}

const handleGalleryUpload = async (e: Event) => {
  const input = e.target as HTMLInputElement;
  if (input.files && input.files.length > 0) {
    try {
      const files = Array.from(input.files);
      await uploadGallery(route.params.id as string, files);
      toast.success('Upload thành công');
      await fetchDetail();
    } catch (error) {
      toast.error('Upload thất bại');
    }
  }
}

const deleteGalleryImg = async (imgId: string) => {
  if (!confirm('Xóa ảnh này?')) return;
  try {
    await deleteGalleryImage(imgId, route.params.id as string);
    // Remove from local list
    galleryImages.value = galleryImages.value.filter(i => i.id !== imgId);
    toast.success('Xóa ảnh thành công');
  } catch (err) {
    toast.error('Xóa thất bại');
  }
}

const submitForm = async () => {
  loading.value = true;
  try {
    let appId = route.params.id as string;

    if (!isEdit.value) {
      // Create
      const res = await createApp(form);
      appId = res || ''; // Expecting ID string
    } else {
      // Update
      await updateApp(appId, form);
    }

    // Handle Image Upload if any
    if (pendingFile.value && appId) {
      const urlStr = await uploadAppImage(appId, pendingFile.value);
      // And update the thumbnail field if it wasn't set (or backend updates it automatically? Backend image upload just adds to AppImage table usually? 
      // Wait, backend `uploadImage` adds to `appImageRepository`. It does NOT update `app.thumbnail`.
      // We might need to manually update `thumbnail` field with the returned URL.
      // Or the backend upload should have returned the URL.
      // Let's assume we get URL and then do a quick update or assume the user copy-pastes it? 
      // No, that's bad UX. 
      // Better: Upload -> Get URL -> Update `thumbnail` field in App.

      // Let's do:
      // 1. Upload
      // 2. Update App with new thumbnail URL
      form.thumbnail = urlStr;
      await updateApp(appId, { ...form, thumbnail: urlStr });
    }

    toast.success(isEdit.value ? 'Cập nhật thành công' : 'Tạo mới thành công');
    router.push({ name: 'admin-apps' });

  } catch (error) {
    console.error(error);
    toast.error('Có lỗi xảy ra');
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  fetchDependencies();
  fetchDetail();
});
</script>
