<template>
    <div class="group bg-white rounded-2xl border border-gray-100 p-4 h-full flex flex-col animate-pulse">
        <!-- Image Skeleton -->
        <div class="relative aspect-[4/3] rounded-xl bg-gray-200 mb-4 overflow-hidden">
        </div>

        <div class="flex-1 flex flex-col">
            <!-- Category Badge Skeleton -->
            <div class="w-20 h-5 bg-gray-200 rounded-full mb-3"></div>

            <!-- Title Skeleton -->
            <div class="h-6 bg-gray-200 rounded mb-2 w-3/4"></div>

            <!-- Description Skeleton -->
            <div class="h-4 bg-gray-200 rounded mb-4 w-full"></div>
            <div class="h-4 bg-gray-200 rounded mb-6 w-2/3"></div>

            <!-- Footer Skeleton -->
            <div class="mt-auto pt-4 border-t border-gray-50 flex items-center justify-between">
                <div class="flex items-center gap-3">
                    <div class="w-16 h-4 bg-gray-200 rounded"></div>
                </div>
                <div class="flex items-center gap-2">
                    <div class="w-8 h-8 bg-gray-200 rounded-lg"></div>
                </div>
            </div>
        </div>
    </div>
</template>