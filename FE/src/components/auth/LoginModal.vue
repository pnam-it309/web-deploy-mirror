<template>
    <teleport to="body">
        <transition 
            enter-active-class="transition ease-out duration-300" 
            enter-from-class="opacity-0 scale-95"
            enter-to-class="opacity-100 scale-100" 
            leave-active-class="transition ease-in duration-200"
            leave-from-class="opacity-100 scale-100" 
            leave-to-class="opacity-0 scale-95"
        >
            <div v-if="isOpen" class="fixed inset-0 z-[100] overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
            <!-- Container for centering -->
            <div class="flex items-center justify-center min-h-screen p-4 text-center sm:p-0">
                
                <!-- Background overlay with premium blur -->
                <transition
                    enter-active-class="transition ease-out duration-300"
                    enter-from-class="opacity-0"
                    enter-to-class="opacity-100"
                    leave-active-class="transition ease-in duration-200"
                    leave-from-class="opacity-100"
                    leave-to-class="opacity-0"
                >
                    <div class="fixed inset-0 bg-gray-900/60 backdrop-blur-sm transition-opacity" aria-hidden="true" @click="close"></div>
                </transition>

                <!-- This element is to trick the browser into centering the modal contents. -->
                <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

                <!-- Modal panel -->
                <div class="relative inline-block align-middle bg-white dark:bg-gray-800 rounded-2xl text-left overflow-hidden shadow-2xl transform transition-all sm:my-8 sm:max-w-md sm:w-full border border-gray-100 dark:border-gray-700">
                    
                    <!-- Close button in corner -->
                    <button @click="close" class="absolute top-4 right-4 p-2 text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 transition-colors z-10">
                        <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>

                    <div class="px-6 pt-8 pb-8">
                        <div class="flex flex-col items-center">
                            <!-- Logo / Icon -->
                            <div class="w-16 h-16 rounded-2xl bg-blue-50 dark:bg-blue-900/30 flex items-center justify-center mb-6 shadow-inner">
                                <svg class="h-8 w-8 text-blue-600 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                                </svg>
                            </div>

                            <div class="text-center mb-8">
                                <h3 class="text-2xl font-bold text-gray-900 dark:text-white mb-2" id="modal-title">
                                    Chào mừng trở lại!
                                </h3>
                                <p class="text-gray-500 dark:text-gray-400 max-w-[280px] mx-auto">
                                    Đăng nhập để trải nghiệm đầy đủ các sản phẩm và tiện ích của FPL Catalog.
                                </p>
                            </div>

                            <div class="w-full space-y-4">
                                <button @click="handleGoogleLogin"
                                    class="w-full flex items-center justify-center gap-3 px-5 py-3.5 border border-gray-200 dark:border-gray-700 rounded-xl text-gray-700 dark:text-gray-200 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700/50 hover:border-blue-300 dark:hover:border-blue-500/50 shadow-sm hover:shadow-md transition-all duration-200 group">
                                    <svg class="h-5 w-5 group-hover:scale-110 transition-transform" viewBox="0 0 24 24">
                                        <path
                                            d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                                            fill="#4285F4" />
                                        <path
                                            d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                                            fill="#34A853" />
                                        <path
                                            d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.26.81-.58z"
                                            fill="#FBBC05" />
                                        <path
                                            d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                                            fill="#EA4335" />
                                    </svg>
                                    <span class="font-semibold text-sm">Tiếp tục với Google</span>
                                </button>
                                
                                <div class="relative py-4">
                                    <div class="absolute inset-0 flex items-center" aria-hidden="true">
                                        <div class="w-full border-t border-gray-100 dark:border-gray-700"></div>
                                    </div>
                                    <div class="relative flex justify-center text-xs uppercase">
                                        <span class="px-2 bg-white dark:bg-gray-800 text-gray-400">Hoặc</span>
                                    </div>
                                </div>

                                <p class="text-center text-xs text-gray-500 dark:text-gray-400">
                                    Bằng cách tiếp tục, bạn đồng ý với <a href="#" class="text-blue-600 hover:underline">Điều khoản dịch vụ</a> và <a href="#" class="text-blue-600 hover:underline">Chính sách bảo mật</a> của chúng tôi.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </transition>
    </teleport>
</template>

<script setup lang="ts">

const props = defineProps<{
    isOpen: boolean;
}>();

const emit = defineEmits<{
    (e: 'close'): void;
    (e: 'login-google'): void;
}>();

const close = () => {
    emit('close');
};

const handleGoogleLogin = () => {
    emit('login-google');
};
</script>

