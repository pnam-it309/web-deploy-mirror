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
        <CompareFloatButton />
    </div>
</template>
<script setup lang="ts">
import PublicHeader from './PublicHeader.vue';
import PublicFooter from './PublicFooter.vue';
import CompareFloatButton from '@/components/common/CompareFloatButton.vue';
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { localStorageAction } from '@/utils/storage';
import { ACCESS_TOKEN_STORAGE_KEY, REFRESH_TOKEN_STORAGE_KEY } from '@/constants/storagekey';

const route = useRoute();
const router = useRouter();

onMounted(() => {
    const state = route.query.state as string;
    if (state) {
        try {
            const decodedState = JSON.parse(atob(state));
            if (decodedState.accessToken && decodedState.refreshToken) {
                localStorageAction.set(ACCESS_TOKEN_STORAGE_KEY, decodedState.accessToken);
                localStorageAction.set(REFRESH_TOKEN_STORAGE_KEY, decodedState.refreshToken);

                // Redirect to remove query params and refresh state
                window.location.href = '/';
            }
        } catch (e) {
            console.error('Failed to parse OAuth2 state', e);
        }
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