<template>
    <Teleport to="body">
        <transition enter-active-class="transition ease-out duration-300" enter-from-class="opacity-0"
            enter-to-class="opacity-100" leave-active-class="transition ease-in duration-200"
            leave-from-class="opacity-100" leave-to-class="opacity-0">
            <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto">
                <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:p-0">
                    <!-- Backdrop -->
                    <div class="fixed inset-0 bg-black/50 backdrop-blur-sm" @click="close"></div>

                    <!-- Modal -->
                    <div
                        class="relative bg-white dark:bg-gray-900 rounded-2xl shadow-2xl max-w-6xl w-full mx-auto transform transition-all">
                        <!-- Header -->
                        <div
                            class="flex items-center justify-between p-6 border-b border-gray-100 dark:border-gray-800">
                            <h2 class="text-2xl font-bold text-gray-900 dark:text-white">
                                So sánh sản phẩm
                            </h2>
                            <button @click="close"
                                class="p-2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-300">
                                <svg class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>
                        </div>

                        <!-- Comparison Table -->
                        <div class="p-6 overflow-x-auto">
                            <table class="w-full">
                                <!-- Product Headers -->
                                <thead>
                                    <tr>
                                        <th
                                            class="w-40 p-4 text-left text-sm font-medium text-gray-500 dark:text-gray-400">
                                        </th>
                                        <th v-for="product in compareList" :key="product.id"
                                            class="p-4 text-center min-w-[200px]">
                                            <div class="relative">
                                                <button @click="removeFromCompare(product.id)"
                                                    class="absolute -top-2 -right-2 p-1 bg-red-500 text-white rounded-full hover:bg-red-600">
                                                    <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24"
                                                        stroke="currentColor">
                                                        <path stroke-linecap="round" stroke-linejoin="round"
                                                            stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                                                    </svg>
                                                </button>
                                                <img :src="product.thumbnail || 'https://placehold.co/200x120'"
                                                    :alt="product.name"
                                                    class="w-full h-24 object-cover rounded-lg mb-3" />
                                                <h3
                                                    class="font-bold text-gray-900 dark:text-white text-sm line-clamp-2">
                                                    {{ product.name }}
                                                </h3>
                                            </div>
                                        </th>
                                    </tr>
                                </thead>

                                <tbody class="divide-y divide-gray-100 dark:divide-gray-800">
                                    <!-- Domain -->
                                    <tr class="bg-gray-50 dark:bg-gray-800/50">
                                        <td class="p-4 text-sm font-medium text-gray-700 dark:text-gray-300">Lĩnh vực
                                        </td>
                                        <td v-for="product in compareList" :key="product.id" class="p-4 text-center">
                                            <span
                                                class="px-3 py-1 bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-400 rounded-full text-sm">
                                                {{ product.domainName || 'N/A' }}
                                            </span>
                                        </td>
                                    </tr>

                                    <!-- Technologies -->
                                    <tr>
                                        <td class="p-4 text-sm font-medium text-gray-700 dark:text-gray-300">Công nghệ
                                        </td>
                                        <td v-for="product in compareList" :key="product.id" class="p-4">
                                            <div class="flex flex-wrap justify-center gap-2">
                                                <span v-for="tech in product.technologies?.slice(0, 5)" :key="tech.name"
                                                    class="flex items-center gap-1 px-2 py-1 bg-gray-100 dark:bg-gray-700 rounded text-xs">
                                                    <img v-if="tech.icon" :src="tech.icon" class="w-3 h-3" />
                                                    {{ tech.name }}
                                                </span>
                                                <span v-if="(product.technologies?.length || 0) > 5"
                                                    class="text-xs text-gray-500">
                                                    +{{ (product.technologies?.length || 0) - 5 }}
                                                </span>
                                            </div>
                                        </td>
                                    </tr>

                                    <!-- Features Count -->
                                    <tr class="bg-gray-50 dark:bg-gray-800/50">
                                        <td class="p-4 text-sm font-medium text-gray-700 dark:text-gray-300">Số tính
                                            năng</td>
                                        <td v-for="product in compareList" :key="product.id"
                                            class="p-4 text-center font-bold text-gray-900 dark:text-white">
                                            {{ product.features?.length || 0 }}
                                        </td>
                                    </tr>

                                    <!-- Demo Link -->
                                    <tr>
                                        <td class="p-4 text-sm font-medium text-gray-700 dark:text-gray-300">Demo</td>
                                        <td v-for="product in compareList" :key="product.id" class="p-4 text-center">
                                            <a v-if="product.demoUrl" :href="product.demoUrl" target="_blank"
                                                class="inline-flex items-center gap-1 text-blue-600 hover:text-blue-700">
                                                <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24"
                                                    stroke="currentColor">
                                                    <path stroke-linecap="round" stroke-linejoin="round"
                                                        stroke-width="2"
                                                        d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                                                </svg>
                                                Xem Demo
                                            </a>
                                            <span v-else class="text-gray-400">-</span>
                                        </td>
                                    </tr>

                                    <!-- Source -->
                                    <tr class="bg-gray-50 dark:bg-gray-800/50">
                                        <td class="p-4 text-sm font-medium text-gray-700 dark:text-gray-300">Source Code
                                        </td>
                                        <td v-for="product in compareList" :key="product.id" class="p-4 text-center">
                                            <a v-if="product.sourceUrl" :href="product.sourceUrl" target="_blank"
                                                class="inline-flex items-center gap-1 text-gray-700 dark:text-gray-300 hover:text-gray-900">
                                                <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
                                                    <path fill-rule="evenodd"
                                                        d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z"
                                                        clip-rule="evenodd" />
                                                </svg>
                                                GitHub
                                            </a>
                                            <span v-else class="text-gray-400">-</span>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <!-- Footer -->
                        <div
                            class="p-6 border-t border-gray-100 dark:border-gray-800 flex justify-between items-center">
                            <p class="text-sm text-gray-500 dark:text-gray-400">
                                {{ compareList.length }}/{{ MAX_COMPARE_ITEMS }} sản phẩm
                            </p>
                            <button @click="clearCompare(); close()"
                                class="px-4 py-2 text-red-600 hover:text-red-700 text-sm font-medium">
                                Xóa tất cả
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </transition>
    </Teleport>
</template>

<script setup lang="ts">
import { useCompare } from '@/composable/data/useCompare'

defineProps<{
    isOpen: boolean
}>()

const emit = defineEmits<{
    (e: 'close'): void
}>()

const { compareList, removeFromCompare, clearCompare, MAX_COMPARE_ITEMS } = useCompare()

const close = () => emit('close')
</script>
