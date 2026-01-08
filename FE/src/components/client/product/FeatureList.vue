<template>
    <div class="mt-8">
        <h3 class="text-xl font-bold text-gray-900 mb-6">Chức năng chính</h3>
        <div class="space-y-4">
            <div v-for="(feature, idx) in features" :key="idx" class="flex gap-4 group">
                <div
                    class="flex-shrink-0 w-8 h-8 flex items-center justify-center rounded-lg bg-blue-50 text-blue-600 font-bold text-sm group-hover:bg-blue-600 group-hover:text-white transition-colors">
                    {{ idx + 1 }}
                </div>
                <div>
                    <h4 class="font-semibold text-gray-900 group-hover:text-blue-600 transition-colors">{{ feature.name
                    }}</h4>
                    <p class="text-gray-500 text-sm mt-1">{{ feature.description }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
export interface Feature {
    name: string;
    description: string;
}

defineProps<{
    features: Feature[]
}>();
</script>