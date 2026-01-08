<template>
    <div class="flex flex-col lg:flex-row gap-4 justify-between items-center mb-8">
        <!-- Left Side: Filter & Search -->
        <div class="flex flex-col sm:flex-row w-full lg:w-auto gap-3">
            <!-- Filter Button -->
            <button
                class="flex items-center justify-center gap-2 px-4 py-2.5 bg-white border border-gray-200 rounded-xl hover:bg-gray-50 hover:border-indigo-600 hover:text-indigo-600 transition-all text-gray-700 font-medium whitespace-nowrap shadow-sm group">
                <svg xmlns="http://www.w3.org/2000/svg"
                    class="h-5 w-5 text-gray-500 group-hover:text-indigo-600 transition-colors" fill="none"
                    viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
                </svg>
                <span>Bộ lọc</span>
                <svg xmlns="http://www.w3.org/2000/svg"
                    class="h-4 w-4 text-gray-400 group-hover:text-indigo-600 transition-colors" fill="none"
                    viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
            </button>

            <!-- Search -->
            <div class="relative w-full sm:w-80">
                <input type="text" :value="modelValue"
                    @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
                    placeholder="Tìm kiếm lĩnh vực..."
                    class="w-full px-4 py-2.5 pl-11 bg-white border border-gray-200 rounded-xl focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/10 transition-all outline-none text-gray-600 placeholder-gray-400 shadow-sm" />
                <svg xmlns="http://www.w3.org/2000/svg"
                    class="h-5 w-5 absolute left-3.5 top-1/2 -translate-y-1/2 text-gray-400" fill="none"
                    viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
            </div>
        </div>

        <!-- Right Side: Sort & Stats -->
        <div class="flex items-center gap-4 w-full lg:w-auto justify-between lg:justify-end">
            <!-- Layout Toggle -->
            <div class="flex items-center bg-gray-100 p-1 rounded-lg">
                <button @click="$emit('update:layout', 'grid')" class="p-1.5 rounded-md transition-all"
                    :class="layout === 'grid' ? 'bg-white shadow-sm text-indigo-600' : 'text-gray-400 hover:text-gray-600'">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z" />
                    </svg>
                </button>
                <button @click="$emit('update:layout', 'list')" class="p-1.5 rounded-md transition-all"
                    :class="layout === 'list' ? 'bg-white shadow-sm text-indigo-600' : 'text-gray-400 hover:text-gray-600'">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M4 6h16M4 12h16M4 18h16" />
                    </svg>
                </button>
            </div>

            <div class="flex items-center gap-3">
                <span class="text-sm text-gray-500 hidden sm:inline-block">Sắp xếp:</span>
                <div class="relative">
                    <select
                        class="appearance-none bg-white border border-gray-200 text-gray-700 py-2.5 pl-4 pr-10 rounded-xl focus:outline-none focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 font-medium cursor-pointer transition-all shadow-sm hover:border-gray-300">
                        <option>Mới nhất</option>
                        <option>Tên A-Z</option>
                        <option>Nhiều sản phẩm nhất</option>
                    </select>
                    <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-3 text-gray-500">
                        <svg class="h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd"
                                d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                                clip-rule="evenodd" />
                        </svg>
                    </div>
                </div>
            </div>

            <div class="h-8 w-[1px] bg-gray-200 hidden sm:block"></div>

            <div class="text-sm text-gray-500 whitespace-nowrap">
                <span class="font-bold text-gray-900">{{ total }}</span> kết quả
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
defineProps<{
    modelValue: string;
    total: number;
    layout: 'grid' | 'list';
}>();

defineEmits<{
    (e: 'update:modelValue', value: string): void;
    (e: 'update:layout', value: 'grid' | 'list'): void;
}>();
</script>