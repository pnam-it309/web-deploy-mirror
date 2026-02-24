<template>
    <div class="group bg-white/90 dark:bg-gray-800/90 backdrop-blur-xl rounded-2xl border border-white/20 dark:border-gray-700/20 p-4 h-full flex flex-col transition-colors relative overflow-hidden">
        <!-- Shimmer overlay -->
        <div class="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent -translate-x-full animate-shimmer"></div>
        
        <!-- Image Skeleton -->
        <div class="relative aspect-[4/3] rounded-xl bg-gray-200/50 dark:bg-gray-700/50 mb-4 overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-gray-200/50 to-gray-300/50 dark:from-gray-700/50 dark:to-gray-600/50"></div>
        </div>

        <div class="flex-1 flex flex-col relative z-10">
            <!-- Category Badge Skeleton -->
            <div class="w-20 h-5 bg-gray-200/50 dark:bg-gray-700/50 rounded-full mb-3 backdrop-blur-sm"></div>

            <!-- Title Skeleton -->
            <div class="h-6 bg-gray-200/50 dark:bg-gray-700/50 rounded mb-2 w-3/4 backdrop-blur-sm"></div>

            <!-- Description Skeleton -->
            <div class="h-4 bg-gray-200/50 dark:bg-gray-700/50 rounded mb-4 w-full backdrop-blur-sm"></div>
            <div class="h-4 bg-gray-200/50 dark:bg-gray-700/50 rounded mb-6 w-2/3 backdrop-blur-sm"></div>

            <!-- Footer Skeleton -->
            <div class="mt-auto pt-4 border-t border-gray-100/50 dark:border-gray-700/50 flex items-center justify-between">
                <div class="flex items-center gap-3">
                    <div class="w-16 h-4 bg-gray-200/50 dark:bg-gray-700/50 rounded backdrop-blur-sm"></div>
                </div>
                <div class="flex items-center gap-2">
                    <div class="w-8 h-8 bg-gray-200/50 dark:bg-gray-700/50 rounded-lg backdrop-blur-sm"></div>
                </div>
            </div>
        </div>
    </div>
</template>