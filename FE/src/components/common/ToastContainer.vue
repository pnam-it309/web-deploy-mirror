<template>
    <div class="fixed top-4 right-4 z-[9999] flex flex-col gap-3 pointer-events-none">
        <TransitionGroup name="toast">
            <div v-for="item in notifyStore.notifications" :key="item.id"
                class="pointer-events-auto min-w-[300px] max-w-sm w-full bg-white dark:bg-gray-800 shadow-lg rounded-lg pointer-events-auto flex overflow-hidden ring-1 ring-black ring-opacity-5"
                :class="{
                    'border-l-4 border-green-500': item.type === 'success',
                    'border-l-4 border-red-500': item.type === 'error',
                    'border-l-4 border-yellow-500': item.type === 'warning',
                    'border-l-4 border-blue-500': item.type === 'info',
                }">
                <div class="p-4 flex-1">
                    <div class="flex items-start">
                        <div class="flex-shrink-0 pt-0.5">
                            <!-- Icons -->
                            <svg v-if="item.type === 'success'" class="h-6 w-6 text-green-500" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            <svg v-if="item.type === 'error'" class="h-6 w-6 text-red-500" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            <svg v-if="item.type === 'warning'" class="h-6 w-6 text-yellow-500" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                            </svg>
                            <svg v-if="item.type === 'info'" class="h-6 w-6 text-blue-500" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                        </div>
                        <div class="ml-3 flex-1 w-0">
                            <p v-if="item.title" class="text-sm font-medium text-gray-900 dark:text-gray-100">{{
                                item.title }}</p>
                            <p class="text-sm text-gray-500 dark:text-gray-400 mt-1">{{ item.message }}</p>
                        </div>
                        <div class="ml-4 flex-shrink-0 flex">
                            <button @click="notifyStore.remove(item.id)"
                                class="bg-transparent rounded-md inline-flex text-gray-400 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                <span class="sr-only">Close</span>
                                <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                                    fill="currentColor" aria-hidden="true">
                                    <path fill-rule="evenodd"
                                        d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                        clip-rule="evenodd" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </TransitionGroup>
    </div>
</template>

<script setup lang="ts">
import { useNotifyStore } from '@/stores/notify';
const notifyStore = useNotifyStore();
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
    transition: all 0.3s ease;
}

.toast-enter-from {
    opacity: 0;
    transform: translateX(100%);
}

.toast-leave-to {
    opacity: 0;
    transform: translateX(100%);
}
</style>