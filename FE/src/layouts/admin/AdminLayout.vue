<template>
  <div class="min-h-screen bg-[#f8fafc] flex">
    <!-- Sidebar -->
    <AdminSidebar />

    <!-- Main Content Wrapper -->
    <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
      <!-- Header -->
      <AdminHeader />

      <!-- Content Area -->
      <main class="flex-1 overflow-x-hidden overflow-y-auto bg-[#f8fafc] p-6 lg:p-8">
        <Transition name="fade" mode="out-in" appear>
          <slot />
        </Transition>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import AdminSidebar from './AdminSidebar.vue';
import AdminHeader from './AdminHeader.vue';
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
