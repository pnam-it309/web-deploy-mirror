<template>
    <button @click.stop="handleCompare"
        class="compare-btn p-2 rounded-full transition-all duration-300 focus:outline-none" :class="[
            isInCompare
                ? 'bg-blue-100 text-blue-600 dark:bg-blue-900/30 dark:text-blue-400'
                : 'bg-gray-100 text-gray-500 hover:bg-gray-200 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-gray-700'
        ]" :title="isInCompare ? 'Xóa khỏi so sánh' : 'Thêm vào so sánh'">
        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
        </svg>
    </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useCompare, type CompareProduct } from '@/composable/data/useCompare'
import { toast } from 'vue3-toastify'

const props = defineProps<{
    product: CompareProduct
}>()

const { isInCompare: checkInCompare, addToCompare, removeFromCompare } = useCompare()

const isInCompare = computed(() => checkInCompare(props.product.id))

const handleCompare = () => {
    if (isInCompare.value) {
        removeFromCompare(props.product.id)
        toast.info('Đã xóa khỏi danh sách so sánh')
    } else {
        const result = addToCompare(props.product)
        if (result.success) {
            toast.success(result.message)
        } else {
            toast.warning(result.message)
        }
    }
}
</script>
