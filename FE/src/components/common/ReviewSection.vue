<template>
    <div class="review-section bg-gray-50 dark:bg-gray-800/50 rounded-xl p-6">
        <div class="flex items-center justify-between mb-6">
            <h3 class="text-xl font-bold text-gray-900 dark:text-white">
                Đánh giá sản phẩm
            </h3>
            <div class="flex items-center gap-2">
                <StarRating :rating="averageRating" :show-count="true" :review-count="reviews.length" />
            </div>
        </div>

        <!-- Write Review Form -->
        <div v-if="!hasReviewed && !showForm" class="mb-6">
            <button @click="showForm = true"
                class="w-full py-3 px-4 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium">
                Viết đánh giá
            </button>
        </div>

        <div v-if="showForm && !hasReviewed"
            class="mb-6 p-4 bg-white dark:bg-gray-800 rounded-lg border border-gray-200 dark:border-gray-700">
            <h4 class="font-semibold text-gray-900 dark:text-white mb-3">Đánh giá của bạn</h4>

            <!-- Rating Selection -->
            <div class="mb-4">
                <label class="block text-sm text-gray-600 dark:text-gray-400 mb-2">Số sao</label>
                <div class="flex gap-2">
                    <button v-for="star in 5" :key="star" @click="newReview.rating = star"
                        class="p-1 focus:outline-none transition-transform hover:scale-110">
                        <svg class="w-8 h-8 transition-colors"
                            :class="star <= newReview.rating ? 'text-yellow-400' : 'text-gray-300 dark:text-gray-600'"
                            fill="currentColor" viewBox="0 0 20 20">
                            <path
                                d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                        </svg>
                    </button>
                </div>
            </div>

            <!-- Comment -->
            <div class="mb-4">
                <label class="block text-sm text-gray-600 dark:text-gray-400 mb-2">Nhận xét (tùy chọn)</label>
                <textarea v-model="newReview.comment" rows="3"
                    class="w-full px-3 py-2 border border-gray-200 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="Chia sẻ trải nghiệm của bạn về sản phẩm này..."></textarea>
            </div>

            <!-- User Info -->
            <div class="grid grid-cols-2 gap-4 mb-4">
                <div>
                    <label class="block text-sm text-gray-600 dark:text-gray-400 mb-2">Email *</label>
                    <input v-model="newReview.userEmail" type="email"
                        class="w-full px-3 py-2 border border-gray-200 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500"
                        placeholder="your@email.com" />
                </div>
                <div>
                    <label class="block text-sm text-gray-600 dark:text-gray-400 mb-2">Tên hiển thị</label>
                    <input v-model="newReview.userName" type="text"
                        class="w-full px-3 py-2 border border-gray-200 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500"
                        placeholder="Tên của bạn" />
                </div>
            </div>

            <!-- Submit Buttons -->
            <div class="flex gap-3">
                <button @click="submitReview" :disabled="!canSubmit || isSubmitting"
                    class="flex-1 py-2 px-4 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors">
                    {{ isSubmitting ? 'Đang gửi...' : 'Gửi đánh giá' }}
                </button>
                <button @click="showForm = false"
                    class="py-2 px-4 text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white transition-colors">
                    Hủy
                </button>
            </div>
        </div>

        <div v-if="hasReviewed"
            class="mb-6 p-4 bg-green-50 dark:bg-green-900/20 rounded-lg border border-green-200 dark:border-green-800">
            <p class="text-green-700 dark:text-green-400 text-sm flex items-center gap-2">
                <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd"
                        d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                        clip-rule="evenodd" />
                </svg>
                Cảm ơn bạn đã đánh giá sản phẩm này!
            </p>
        </div>

        <!-- Reviews List -->
        <div class="space-y-4">
            <div v-for="review in reviews" :key="review.id"
                class="p-4 bg-white dark:bg-gray-800 rounded-lg border border-gray-100 dark:border-gray-700">
                <div class="flex items-start justify-between mb-2">
                    <div>
                        <span class="font-medium text-gray-900 dark:text-white">{{ review.userName || 'Ẩn danh'
                            }}</span>
                        <span class="text-sm text-gray-500 dark:text-gray-400 ml-2">{{ review.userEmail }}</span>
                    </div>
                    <StarRating :rating="review.rating" />
                </div>
                <p v-if="review.comment" class="text-gray-600 dark:text-gray-300 text-sm">
                    {{ review.comment }}
                </p>
                <p class="text-xs text-gray-400 dark:text-gray-500 mt-2">
                    {{ formatDateNVV(review.createdAt) }}
                </p>
            </div>

            <p v-if="reviews.length === 0" class="text-center text-gray-500 dark:text-gray-400 py-8">
                Chưa có đánh giá nào. Hãy là người đầu tiên!
            </p>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import StarRating from '@/components/common/StarRating.vue'
import request from '@/services/request'
import { toast } from 'vue3-toastify'
import { formatDateNVV } from '@/utils'

const props = defineProps<{
    appId: string
}>()

interface Review {
    id: string
    rating: number
    comment: string
    userEmail: string
    userName: string
    createdAt: number
}

const reviews = ref<Review[]>([])
const averageRating = ref(0)
const showForm = ref(false)
const isSubmitting = ref(false)
const hasReviewed = ref(false)

const newReview = ref({
    rating: 0,
    comment: '',
    userEmail: '',
    userName: ''
})

const canSubmit = computed(() => {
    return newReview.value.rating > 0 && newReview.value.userEmail.includes('@')
})

const fetchReviews = async () => {
    try {
        const res = await request.get(`/customer/reviews/${props.appId}`)
        if (res.data.data) {
            reviews.value = res.data.data.reviews || []
            averageRating.value = res.data.data.averageRating || 0
        }
    } catch (error) {
        console.error('Failed to fetch reviews:', error)
    }
}

const submitReview = async () => {
    if (!canSubmit.value) return
    isSubmitting.value = true

    try {
        await request.post('/customer/reviews', {
            appId: props.appId,
            rating: newReview.value.rating,
            comment: newReview.value.comment,
            userEmail: newReview.value.userEmail,
            userName: newReview.value.userName
        })

        toast.success('Đánh giá thành công!')
        hasReviewed.value = true
        showForm.value = false

        // Store in localStorage to persist review status
        localStorage.setItem(`reviewed-${props.appId}`, newReview.value.userEmail)

        // Refresh reviews
        await fetchReviews()
    } catch (error: any) {
        toast.error(error.response?.data?.message || 'Có lỗi xảy ra')
    } finally {
        isSubmitting.value = false
    }
}



onMounted(() => {
    fetchReviews()
    // Check if user already reviewed (from localStorage)
    const reviewedEmail = localStorage.getItem(`reviewed-${props.appId}`)
    if (reviewedEmail) {
        hasReviewed.value = true
    }
})
</script>
