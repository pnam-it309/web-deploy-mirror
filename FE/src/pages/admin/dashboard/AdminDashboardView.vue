<script setup lang="ts">
import { ref, onMounted } from 'vue';

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
  <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
    <div class="mb-6 shrink-0">
          <h1 class="text-3xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight">Tổng quan hệ thống</h1>
          <p class="text-gray-500 dark:text-gray-400 text-sm mt-1">Số liệu thống kê thời gian thực của toàn bộ hệ thống</p>
    </div>

    <div v-if="isLoading" class="flex-1 flex justify-center items-center">
      <BaseSpinner size="lg" />
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 animate-fade-in overflow-y-auto custom-scrollbar pb-6">
      
      <!-- 1. Projects -->
      <div class="bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-100 dark:border-gray-700 shadow-sm hover:shadow-xl hover:-translate-y-1 transition-all duration-300 group">
        <div class="flex items-center gap-5">
            <div class="p-4 rounded-xl bg-blue-50 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400 group-hover:scale-110 transition-transform duration-300">
              <CubeIcon class="w-8 h-8" />
            </div>
            <div>
              <p class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1">Dự án</p>
              <p class="text-3xl font-bold text-gray-900 dark:text-white group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">{{ stats?.totalApps || 0 }}</p>
            </div>
        </div>
        <div class="mt-4 pt-4 border-t border-gray-100 dark:border-gray-700 flex justify-between items-center">
            <span class="text-xs text-green-500 font-bold bg-green-50 dark:bg-green-900/20 px-2 py-0.5 rounded flex items-center gap-1">
                <svg class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" /></svg>
                +12%
            </span>
            <span class="text-xs text-gray-400">so với tháng trước</span>
        </div>
      </div>

      <!-- 2. Domains -->
      <div class="bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-100 dark:border-gray-700 shadow-sm hover:shadow-xl hover:-translate-y-1 transition-all duration-300 group">
        <div class="flex items-center gap-5">
            <div class="p-4 rounded-xl bg-yellow-50 dark:bg-yellow-900/20 text-yellow-600 dark:text-yellow-400 group-hover:scale-110 transition-transform duration-300">
              <FolderIcon class="w-8 h-8" />
            </div>
            <div>
              <p class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1">Lĩnh vực</p>
              <p class="text-3xl font-bold text-gray-900 dark:text-white group-hover:text-yellow-600 dark:group-hover:text-yellow-400 transition-colors">{{ stats?.totalDomains || 0 }}</p>
            </div>
        </div>
         <div class="mt-4 pt-4 border-t border-gray-100 dark:border-gray-700 flex justify-between items-center">
             <span class="text-xs text-gray-400">Đang hoạt động</span>
        </div>
      </div>

      <!-- 3. Students -->
       <div class="bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-100 dark:border-gray-700 shadow-sm hover:shadow-xl hover:-translate-y-1 transition-all duration-300 group">
        <div class="flex items-center gap-5">
            <div class="p-4 rounded-xl bg-green-50 dark:bg-green-900/20 text-green-600 dark:text-green-400 group-hover:scale-110 transition-transform duration-300">
              <UserGroupIcon class="w-8 h-8" />
            </div>
            <div>
              <p class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1">Sinh viên</p>
              <p class="text-3xl font-bold text-gray-900 dark:text-white group-hover:text-green-600 dark:group-hover:text-green-400 transition-colors">{{ stats?.totalStudents || 0 }}</p>
            </div>
        </div>
         <div class="mt-4 pt-4 border-t border-gray-100 dark:border-gray-700 flex justify-between items-center">
            <span class="text-xs text-green-500 font-bold bg-green-50 dark:bg-green-900/20 px-2 py-0.5 rounded flex items-center gap-1">
                <svg class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" /></svg>
                +5%
            </span>
            <span class="text-xs text-gray-400">so với tháng trước</span>
        </div>
      </div>

      <!-- 4. Technologies -->
      <div class="bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-100 dark:border-gray-700 shadow-sm hover:shadow-xl hover:-translate-y-1 transition-all duration-300 group">
        <div class="flex items-center gap-5">
            <div class="p-4 rounded-xl bg-purple-50 dark:bg-purple-900/20 text-purple-600 dark:text-purple-400 group-hover:scale-110 transition-transform duration-300">
              <CpuChipIcon class="w-8 h-8" />
            </div>
            <div>
              <p class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1">Công nghệ</p>
              <p class="text-3xl font-bold text-gray-900 dark:text-white group-hover:text-purple-600 dark:group-hover:text-purple-400 transition-colors">{{ stats?.totalTechnologies || 0 }}</p>
            </div>
        </div>
         <div class="mt-4 pt-4 border-t border-gray-100 dark:border-gray-700 flex justify-between items-center">
             <span class="text-xs text-gray-400">Mới cập nhật</span>
        </div>
      </div>

      <!-- 5. Views -->
       <div class="bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-100 dark:border-gray-700 shadow-sm hover:shadow-xl hover:-translate-y-1 transition-all duration-300 group">
        <div class="flex items-center gap-5">
            <div class="p-4 rounded-xl bg-pink-50 dark:bg-pink-900/20 text-pink-600 dark:text-pink-400 group-hover:scale-110 transition-transform duration-300">
              <EyeIcon class="w-8 h-8" />
            </div>
            <div>
              <p class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1">Lượt xem</p>
              <p class="text-3xl font-bold text-gray-900 dark:text-white group-hover:text-pink-600 dark:group-hover:text-pink-400 transition-colors">{{ stats?.totalViews || 0 }}</p>
            </div>
        </div>
         <div class="mt-4 pt-4 border-t border-gray-100 dark:border-gray-700 flex justify-between items-center">
            <span class="text-xs text-green-500 font-bold bg-green-50 dark:bg-green-900/20 px-2 py-0.5 rounded flex items-center gap-1">
                <svg class="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" /></svg>
                +24%
            </span>
             <span class="text-xs text-gray-400">Total views</span>
        </div>
      </div>

      <!-- 6. Features -->
      <div class="bg-white dark:bg-gray-800 rounded-xl p-6 border border-gray-100 dark:border-gray-700 shadow-sm hover:shadow-xl hover:-translate-y-1 transition-all duration-300 group">
        <div class="flex items-center gap-5">
            <div class="p-4 rounded-xl bg-orange-50 dark:bg-orange-900/20 text-orange-600 dark:text-orange-400 group-hover:scale-110 transition-transform duration-300">
              <SparklesIcon class="w-8 h-8" />
            </div>
            <div>
              <p class="text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1">Chức năng</p>
              <p class="text-3xl font-bold text-gray-900 dark:text-white group-hover:text-orange-600 dark:group-hover:text-orange-400 transition-colors">{{ stats?.totalFeatures || 0 }}</p>
            </div>
        </div>
         <div class="mt-4 pt-4 border-t border-gray-100 dark:border-gray-700 flex justify-between items-center">
             <span class="text-xs text-gray-400">Tính năng hệ thống</span>
        </div>
      </div>

    </div>
  </div>
</template>