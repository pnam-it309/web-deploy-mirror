<template>
    <section class="py-16 bg-gradient-to-r from-blue-600 to-indigo-700">
        <div class="container mx-auto px-4">
            <div class="max-w-2xl mx-auto text-center">
                <h2 class="text-3xl font-bold text-white mb-4">
                    Đăng ký nhận tin mới nhất
                </h2>
                <p class="text-blue-100 mb-8">
                    Nhận thông báo về các sản phẩm mới, cập nhật và tin tức từ FPL Catalog
                </p>

                <!-- Success State -->
                <div v-if="isSubscribed" class="bg-white/20 backdrop-blur-sm rounded-xl p-6">
                    <svg class="w-12 h-12 mx-auto text-green-300 mb-4" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <p class="text-white text-lg">Cảm ơn bạn đã đăng ký!</p>
                    <p class="text-blue-100 text-sm mt-2">Vui lòng kiểm tra email để xác nhận đăng ký</p>
                </div>

                <!-- Form -->
                <form v-else @submit.prevent="subscribe" class="flex flex-col sm:flex-row gap-4">
                    <input v-model="name" type="text" placeholder="Tên của bạn"
                        class="flex-1 px-4 py-3 rounded-lg bg-white/10 backdrop-blur-sm border border-white/20 text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-white/50" />
                    <input v-model="email" type="email" placeholder="Email của bạn" required
                        class="flex-1 px-4 py-3 rounded-lg bg-white/10 backdrop-blur-sm border border-white/20 text-white placeholder-white/60 focus:outline-none focus:ring-2 focus:ring-white/50" />
                    <button type="submit" :disabled="isLoading"
                        class="px-8 py-3 bg-white text-blue-600 font-semibold rounded-lg hover:bg-blue-50 transition-colors disabled:opacity-50">
                        <span v-if="isLoading">Đang xử lý...</span>
                        <span v-else>Đăng ký</span>
                    </button>
                </form>

                <p v-if="error" class="mt-4 text-red-200 text-sm">{{ error }}</p>

                <p class="text-blue-100 text-xs mt-6">
                    Chúng tôi tôn trọng quyền riêng tư của bạn. Huỷ đăng ký bất cứ lúc nào.
                </p>
            </div>
        </div>
    </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import request from '@/services/request'
import { toast } from 'vue3-toastify'

const name = ref('')
const email = ref('')
const isLoading = ref(false)
const isSubscribed = ref(false)
const error = ref('')

const subscribe = async () => {
    if (!email.value) return

    isLoading.value = true
    error.value = ''

    try {
        await request.post('/customer/subscribe', {
            name: name.value,
            email: email.value
        })
        isSubscribed.value = true
        toast.success('Đăng ký thành công!')
    } catch (err: any) {
        error.value = err.response?.data?.message || 'Có lỗi xảy ra'
    } finally {
        isLoading.value = false
    }
}
</script>
