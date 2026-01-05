<script setup lang="ts">
import AdminSidebar from '@/layouts/admin/AdminSidebar.vue';
</script>

<template>
  <div class="flex h-screen w-full bg-gray-50 overflow-hidden font-sans text-dark">
    <AdminSidebar />

    <main class="flex-1 flex flex-col h-full overflow-hidden relative transition-all duration-300">
      
      <div class="flex-1 overflow-y-auto overflow-x-hidden p-0 custom-scrollbar">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
  </div>
</template>

<style scoped>
/* Hiệu ứng chuyển trang nhẹ nhàng */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Scrollbar đẹp cho nội dung chính */
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #e2e8f0;
  border-radius: 3px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: #cbd5e1;
}
</style>