<template>
    <!-- Compare Floating Button -->
    <Teleport to="body">
        <transition enter-active-class="transition ease-out duration-300 transform"
            enter-from-class="translate-y-full opacity-0" enter-to-class="translate-y-0 opacity-100"
            leave-active-class="transition ease-in duration-200 transform" leave-from-class="translate-y-0 opacity-100"
            leave-to-class="translate-y-full opacity-0">
            <div v-if="compareCount > 0" class="fixed bottom-6 right-6 z-40">
                <button @click="showCompareModal = true"
                    class="flex items-center gap-3 px-6 py-3 bg-blue-600 text-white rounded-full shadow-lg hover:bg-blue-700 transition-all hover:scale-105">
                    <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                    </svg>
                    <span class="font-medium">So sánh ({{ compareCount }})</span>
                </button>
            </div>
        </transition>

        <CompareModal :is-open="showCompareModal" @close="showCompareModal = false" />
    </Teleport>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useCompare } from '@/composable/data/useCompare'
import CompareModal from '@/components/common/CompareModal.vue'

const { compareCount } = useCompare()
const showCompareModal = ref(false)
</script>
