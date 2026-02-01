<template>
    <div class="flex flex-col items-center justify-center py-12 px-4 text-center">
        <div v-if="icon || $slots.icon" class="mb-4 bg-gray-50 dark:bg-gray-800 p-4 rounded-full transition-colors">
            <slot name="icon">
                <component :is="icon" class="w-12 h-12 text-gray-400 dark:text-gray-500" />
            </slot>
        </div>

        <h3 class="text-lg font-medium text-gray-900 dark:text-white mb-1">{{ title }}</h3>

        <p v-if="description" class="text-gray-500 dark:text-gray-400 max-w-sm mb-6">
            {{ description }}
        </p>

        <template v-if="actionLabel">
            <button @click="$emit('action-click')"
                class="inline-flex items-center px-4 py-2 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors">
                {{ actionLabel }}
            </button>
        </template>
    </div>
</template>

<script setup lang="ts">
defineProps({
    title: {
        type: String,
        required: true
    },
    description: {
        type: String,
        default: ''
    },
    icon: {
        type: [Object, Function],
        default: null
    },
    actionLabel: {
        type: String,
        default: ''
    }
})

defineEmits(['action-click'])
</script>