<script setup lang="ts">
import { ref, onMounted } from 'vue';
import BaseCard from '@/components/base/BaseCard.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import {
  CubeIcon,
  FolderIcon,
  UserGroupIcon,
  EyeIcon,
  SparklesIcon,
  CpuChipIcon
} from '@heroicons/vue/24/outline';
import { getDashboardStats, type DashboardStats } from '@/services/admin/dashboard.service';

const isLoading = ref(false);
const stats = ref<DashboardStats>({
  totalApps: 0,
  totalDomains: 0,
  totalTechnologies: 0,
  totalStudents: 0,
  totalViews: 0,
  totalFeatures: 0
});

onMounted(async () => {
  isLoading.value = true;
  try {
    const res = await getDashboardStats();
    if (res) {
      stats.value = res;
    }
  } catch (e) {
    console.error(e)
  } finally {
    isLoading.value = false;
  }
});
</script>

<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold text-dark mb-6 font-serif uppercase">Tổng quan hệ thống</h1>

    <div v-if="isLoading" class="flex justify-center py-10">
      <BaseSpinner />
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <!-- 1. Projects -->
      <BaseCard class="flex items-center gap-4 !p-4 hover:-translate-y-1 transition-transform">
        <div class="p-3 rounded-full bg-blue-100 text-blue-600">
          <CubeIcon class="w-6 h-6" />
        </div>
        <div>
          <p class="text-xs text-gray-500 uppercase font-bold">Dự án</p>
          <p class="text-2xl font-bold text-dark">{{ stats?.totalApps || 0 }}</p>
        </div>
      </BaseCard>

      <!-- 2. Domains -->
      <BaseCard class="flex items-center gap-4 !p-4 hover:-translate-y-1 transition-transform">
        <div class="p-3 rounded-full bg-yellow-100 text-yellow-600">
          <FolderIcon class="w-6 h-6" />
        </div>
        <div>
          <p class="text-xs text-gray-500 uppercase font-bold">Lĩnh vực</p>
          <p class="text-2xl font-bold text-dark">{{ stats?.totalDomains || 0 }}</p>
        </div>
      </BaseCard>

      <!-- 3. Students -->
      <BaseCard class="flex items-center gap-4 !p-4 hover:-translate-y-1 transition-transform">
        <div class="p-3 rounded-full bg-green-100 text-green-600">
          <UserGroupIcon class="w-6 h-6" />
        </div>
        <div>
          <p class="text-xs text-gray-500 uppercase font-bold">Sinh viên</p>
          <p class="text-2xl font-bold text-dark">{{ stats?.totalStudents || 0 }}</p>
        </div>
      </BaseCard>

      <!-- 4. Technologies -->
      <BaseCard class="flex items-center gap-4 !p-4 hover:-translate-y-1 transition-transform">
        <div class="p-3 rounded-full bg-purple-100 text-purple-600">
          <CpuChipIcon class="w-6 h-6" />
        </div>
        <div>
          <p class="text-xs text-gray-500 uppercase font-bold">Công nghệ</p>
          <p class="text-2xl font-bold text-dark">{{ stats?.totalTechnologies || 0 }}</p>
        </div>
      </BaseCard>

      <!-- 5. Views -->
      <BaseCard class="flex items-center gap-4 !p-4 hover:-translate-y-1 transition-transform">
        <div class="p-3 rounded-full bg-pink-100 text-pink-600">
          <EyeIcon class="w-6 h-6" />
        </div>
        <div>
          <p class="text-xs text-gray-500 uppercase font-bold">Lượt xem</p>
          <p class="text-2xl font-bold text-dark">{{ stats?.totalViews || 0 }}</p>
        </div>
      </BaseCard>

      <!-- 6. Features -->
      <BaseCard class="flex items-center gap-4 !p-4 hover:-translate-y-1 transition-transform">
        <div class="p-3 rounded-full bg-orange-100 text-orange-600">
          <SparklesIcon class="w-6 h-6" />
        </div>
        <div>
          <p class="text-xs text-gray-500 uppercase font-bold">Chức năng</p>
          <p class="text-2xl font-bold text-dark">{{ stats?.totalFeatures || 0 }}</p>
        </div>
      </BaseCard>

    </div>
  </div>
</template>