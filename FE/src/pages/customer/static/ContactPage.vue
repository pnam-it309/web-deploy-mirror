<template>
  <div class="contact-page">
    <!-- Hero Section -->
    <div class="bg-gradient-to-r from-blue-600 to-blue-800 text-white py-16">
      <div class="container mx-auto px-4 text-center">
        <h1 class="text-4xl md:text-5xl font-bold mb-4">Liên Hệ Với Chúng Tôi</h1>
        <p class="text-xl md:text-2xl max-w-3xl mx-auto">Chúng tôi luôn sẵn lòng lắng nghe và hỗ trợ bạn</p>
      </div>
    </div>

    <!-- Contact Information -->
    <section class="py-16 bg-white">
      <div class="container mx-auto px-4">
        <div class="grid md:grid-cols-3 gap-8">
          <!-- Contact Card 1 -->
          <div class="bg-gray-50 p-6 rounded-lg text-center hover:shadow-lg transition-shadow duration-300">
            <div class="bg-blue-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
              </svg>
            </div>
            <h3 class="text-xl font-semibold mb-2">Email</h3>
            <p class="text-gray-600">info@example.com</p>
            <p class="text-gray-600">support@example.com</p>
          </div>
          
          <!-- Contact Card 2 -->
          <div class="bg-gray-50 p-6 rounded-lg text-center hover:shadow-lg transition-shadow duration-300">
            <div class="bg-blue-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
              </svg>
            </div>
            <h3 class="text-xl font-semibold mb-2">Điện Thoại</h3>
            <p class="text-gray-600">+84 123 456 789</p>
            <p class="text-gray-600">+84 987 654 321</p>
          </div>
          
          <!-- Contact Card 3 -->
          <div class="bg-gray-50 p-6 rounded-lg text-center hover:shadow-lg transition-shadow duration-300">
            <div class="bg-blue-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            </div>
            <h3 class="text-xl font-semibold mb-2">Địa Chỉ</h3>
            <p class="text-gray-600">123 Đường ABC</p>
            <p class="text-gray-600">Quận 1, TP. Hồ Chí Minh</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Contact Form -->
    <section class="py-16 bg-gray-50">
      <div class="container mx-auto px-4 max-w-4xl">
        <div class="bg-white rounded-lg shadow-md p-8">
          <h2 class="text-2xl font-bold text-gray-800 mb-6 text-center">Gửi Tin Nhắn Cho Chúng Tôi</h2>
          
          <form @submit.prevent="handleSubmit" class="space-y-6">
            <div class="grid md:grid-cols-2 gap-6">
              <div class="form-group">
                <label for="name" class="block text-sm font-medium text-gray-700 mb-1">Họ và tên <span class="text-red-500">*</span></label>
                <input
                  id="name"
                  v-model="formData.name"
                  type="text"
                  required
                  class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="Nhập họ và tên của bạn"
                >
              </div>
              
              <div class="form-group">
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email <span class="text-red-500">*</span></label>
                <input
                  id="email"
                  v-model="formData.email"
                  type="email"
                  required
                  class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="email@example.com"
                >
              </div>
            </div>
            
            <div class="form-group">
              <label for="subject" class="block text-sm font-medium text-gray-700 mb-1">Tiêu đề <span class="text-red-500">*</span></label>
              <input
                id="subject"
                v-model="formData.subject"
                type="text"
                required
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Tiêu đề tin nhắn"
              >
            </div>
            
            <div class="form-group">
              <label for="message" class="block text-sm font-medium text-gray-700 mb-1">Nội dung <span class="text-red-500">*</span></label>
              <textarea
                id="message"
                v-model="formData.message"
                rows="5"
                required
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nội dung chi tiết..."
              ></textarea>
            </div>
            
            <div class="flex items-center">
              <input
                id="consent"
                v-model="formData.consent"
                type="checkbox"
                required
                class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
              >
              <label for="consent" class="ml-2 block text-sm text-gray-700">
                Tôi đồng ý với <a href="#" class="text-blue-600 hover:underline">chính sách bảo mật</a> và <a href="#" class="text-blue-600 hover:underline">điều khoản dịch vụ</a>.
              </label>
            </div>
            
            <div class="pt-2">
              <button
                type="submit"
                :disabled="isSubmitting"
                class="w-full md:w-auto px-6 py-3 bg-blue-600 text-white font-medium rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <span v-if="!isSubmitting">Gửi Tin Nhắn</span>
                <span v-else class="flex items-center justify-center">
                  <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  Đang gửi...
                </span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </section>

    <!-- Map Section -->
    <section class="h-96 w-full">
      <iframe
        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.319437703202!2d106.66063741526072!3d10.78684099231395!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752ed2392c44df%3A0x460eaf9612cf0f1b!2sLandmark%2081!5e0!3m2!1sen!2s!4v1620000000000!5m2!1sen!2s"
        width="100%"
        height="100%"
        style="border:0;"
        allowfullscreen=""
        loading="lazy"
      ></iframe>
    </section>

    <!-- Business Hours -->
    <section class="py-16 bg-white">
      <div class="container mx-auto px-4 text-center">
        <h2 class="text-2xl font-bold text-gray-800 mb-8">Giờ Làm Việc</h2>
        <div class="max-w-md mx-auto bg-gray-50 rounded-lg p-6">
          <ul class="space-y-2">
            <li v-for="(hours, day) in businessHours" :key="day" class="flex justify-between">
              <span class="font-medium text-gray-700">{{ day }}:</span>
              <span class="text-gray-600">{{ hours }}</span>
            </li>
          </ul>
          <p class="mt-4 text-sm text-gray-500">* Chúng tôi có thể đóng cửa vào các ngày lễ</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'ContactPage',
  setup() {
    const formData = ref({
      name: '',
      email: '',
      subject: '',
      message: '',
      consent: false
    });

    const isSubmitting = ref(false);

    const businessHours = {
      'Thứ Hai - Thứ Sáu': '8:00 - 18:00',
      'Thứ Bảy': '9:00 - 16:00',
      'Chủ Nhật': 'Nghỉ',
    };

    const handleSubmit = () => {
      isSubmitting.value = true;
      
      // Simulate API call
      setTimeout(() => {
        console.log('Form submitted:', formData.value);
        // Reset form
        formData.value = {
          name: '',
          email: '',
          subject: '',
          message: '',
          consent: false
        };
        
        // Show success message
        alert('Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi sớm nhất có thể.');
        isSubmitting.value = false;
      }, 1500);
    };

    return {
      formData,
      isSubmitting,
      businessHours,
      handleSubmit
    };
  }
});
</script>

<style scoped>
.contact-page {
  scroll-behavior: smooth;
}

.form-group {
  margin-bottom: 1rem;
}

/* Custom checkbox styling */
input[type="checkbox"]:focus {
  --tw-ring-color: rgba(37, 99, 235, 0.2);
  border-color: #3b82f6;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .contact-page {
    padding-top: 1rem;
  }
  
  section {
    padding: 2rem 0;
  }
}
</style>
