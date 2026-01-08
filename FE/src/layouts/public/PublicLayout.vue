<template>
    <div class="min-h-screen flex flex-col bg-white">
        <PublicHeader />

        <main class="flex-1">
            <router-view v-slot="{ Component }">
                <transition name="fade" mode="out-in">
                    <component :is="Component" />
                </transition>
            </router-view>
        </main>

        <PublicFooter />
    </div>
</template>

<script setup lang="ts">
import PublicHeader from './PublicHeader.vue';
import PublicFooter from './PublicFooter.vue';
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