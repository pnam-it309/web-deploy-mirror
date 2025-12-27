<template>
    <div class="bg-white p-4 rounded-xl border border-gray-100 sticky top-24">
        <div class="flex justify-between items-center mb-4">
            <h3 class="font-semibold text-gray-900">Bộ lọc</h3>
            <button class="text-sm text-blue-600 hover:text-blue-700">Đặt lại</button>
        </div>

        <!-- Domain Filter -->
        <div class="mb-6">
            <h4 class="text-sm font-medium text-gray-700 mb-3">Lĩnh vực</h4>
            <div class="space-y-2">
                <label class="flex items-center gap-2 cursor-pointer hover:text-gray-900 text-gray-600">
                    <input type="checkbox" class="rounded border-gray-300 text-blue-600 focus:ring-blue-500" />
                    <span>Web Development</span>
                </label>
                <label class="flex items-center gap-2 cursor-pointer hover:text-gray-900 text-gray-600">
                    <input type="checkbox" class="rounded border-gray-300 text-blue-600 focus:ring-blue-500" />
                    <span>Mobile App</span>
                </label>
            </div>
        </div>

        <!-- Tech Filter (Mock) -->
        <div>
            <h4 class="text-sm font-medium text-gray-700 mb-3">Công nghệ</h4>
            <div class="flex flex-wrap gap-2">
                <button
                    class="px-3 py-1 bg-gray-100 hover:bg-gray-200 text-gray-600 rounded-full text-xs transition-colors">React</button>
                <button
                    class="px-3 py-1 bg-gray-100 hover:bg-gray-200 text-gray-600 rounded-full text-xs transition-colors">VueJS</button>
                <button
                    class="px-3 py-1 bg-gray-100 hover:bg-gray-200 text-gray-600 rounded-full text-xs transition-colors">Java</button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
// Logic for emitting filter changes
</script>
