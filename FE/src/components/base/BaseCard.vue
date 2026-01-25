<template>
  <div class="bg-white dark:bg-gray-800 rounded-xl shadow-sm hover:shadow-md transition-all duration-300 overflow-hidden border border-gray-100 dark:border-gray-700 relative">
    <div class="absolute top-0 left-0 w-full h-[2px] bg-blue-600 dark:bg-blue-500"></div>
    
    <div class="p-6">
      <slot />
    </div>
  </div>
</template>