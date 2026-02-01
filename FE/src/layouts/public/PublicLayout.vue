<template>
    <div class="min-h-screen flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <PublicHeader />

        <main class="flex-1">
            <router-view v-slot="{ Component }">
                <transition name="fade" mode="out-in">
                    <component :is="Component" />
                </transition>
            </router-view>
        </main>

        <PublicFooter />
        <CompareFloatButton />
    </div>
</template>
<script setup lang="ts">
import PublicHeader from './PublicHeader.vue';
import PublicFooter from './PublicFooter.vue';
import CompareFloatButton from '@/components/common/CompareFloatButton.vue';
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

onMounted(() => {
    const state = route.query.state as string;
    if (state) {
        // If state exists on root path, redirect to /redirect to handle it properly
        router.push({ path: '/redirect', query: { state } });
    }
});
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