<script setup lang="ts">
import { ref, onMounted } from 'vue';
import BaseCard from '@/components/base/BaseCard.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import { Squares2X2Icon, CubeIcon, FolderIcon, UserGroupIcon } from '@heroicons/vue/24/outline';
// import { useDashboardService } from '@/services/admin/dashboard.service'; // Nếu đã có service

const isLoading = ref(false);
const stats = ref({
  totalApps: 0,
  totalDomains: 0,
  totalTechnologies: 0,
  totalStudents: 0
});

onMounted(async () => {
  // isLoading.value = true;
  // try {
  //   const res = await DashboardService.getStats();
  //   stats.value = res;
  // } catch (e) { console.error(e) } finally { isLoading.value = false; }
  
  // Fake data demo cho lên giao diện trước
  stats.value = { totalApps: 12, totalDomains: 5, totalTechnologies: 8, totalStudents: 45 };
});
</script>

<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold text-dark mb-6 font-serif uppercase">Tổng quan hệ thống</h1>
    
    <div v-if="isLoading" class="flex justify-center py-10"><BaseSpinner /></div>
    
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <BaseCard class="flex items-center gap-4 !p-4 hover:-translate-y-1 transition-transform">
        <div class="p-3 rounded-full bg-blue-100 text-blue-600"><CubeIcon class="w-6 h-6"/></div>
        <div>
          <p class="text-xs text-gray-500 uppercase font-bold">Dự án</p>
          <p class="text-2xl font-bold text-dark">{{ stats.totalApps }}</p>
        </div>
      </BaseCard>

      <BaseCard class="flex items-center gap-4 !p-4 hover:-translate-y-1 transition-transform">
        <div class="p-3 rounded-full bg-yellow-100 text-yellow-600"><FolderIcon class="w-6 h-6"/></div>
        <div>
          <p class="text-xs text-gray-500 uppercase font-bold">Lĩnh vực</p>
          <p class="text-2xl font-bold text-dark">{{ stats.totalDomains }}</p>
        </div>
      </BaseCard>

      <BaseCard class="flex items-center gap-4 !p-4 hover:-translate-y-1 transition-transform">
        <div class="p-3 rounded-full bg-green-100 text-green-600"><UserGroupIcon class="w-6 h-6"/></div>
        <div>
          <p class="text-xs text-gray-500 uppercase font-bold">Sinh viên</p>
          <p class="text-2xl font-bold text-dark">{{ stats.totalStudents }}</p>
        </div>
      </BaseCard>
    </div>
  </div>
</template>