<template>
  <div class="min-h-screen bg-white flex flex-col items-center justify-center px-4">
    <div class="text-center">
      <h1 class="text-9xl font-bold text-indigo-100">404</h1>
      
      <div class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2">
        <h2 class="text-3xl font-bold text-gray-900 mb-4">Trang không tồn tại</h2>
        <p class="text-gray-500 mb-8 max-w-md mx-auto">
          Xin lỗi, trang bạn đang tìm kiếm không tồn tại hoặc đã bị di chuyển.
        </p>
        
        <router-link 
          to="/" 
          class="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-lg text-indigo-700 bg-indigo-100 hover:bg-indigo-200 transition-colors duration-200"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
          Về trang chủ
        </router-link>
      </div>
    </div>
  </div>
</template>
