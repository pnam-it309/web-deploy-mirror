<template>
  <div class="about-page">
    <!-- Hero Section -->
    <div class="bg-gradient-to-r from-blue-600 to-blue-800 text-white py-16">
      <div class="container mx-auto px-4 text-center">
        <h1 class="text-4xl md:text-5xl font-bold mb-4">Về Chúng Tôi</h1>
        <p class="text-xl md:text-2xl max-w-3xl mx-auto">Khám phá câu chuyện đằng sau thương hiệu và đội ngũ đam mê của chúng tôi</p>
      </div>
    </div>

    <!-- Our Story -->
    <section class="py-16 bg-white">
      <div class="container mx-auto px-4">
        <div class="flex flex-col md:flex-row items-center">
          <div class="md:w-1/2 mb-8 md:mb-0 md:pr-8">
            <h2 class="text-3xl font-bold text-gray-800 mb-6">Câu Chuyện Của Chúng Tôi</h2>
            <div class="space-y-4 text-gray-600">
              <p>Được thành lập vào năm 2023, chúng tôi bắt đầu với một tầm nhìn đơn giản: Mang đến những sản phẩm chất lượng cao với giá cả phải chăng cho mọi người tiêu dùng Việt Nam.</p>
              <p>Xuất phát từ một cửa hàng nhỏ, đến nay chúng tôi đã phát triển thành một trong những địa chỉ mua sắm trực tuyến đáng tin cậy hàng đầu, phục vụ hàng trăm nghìn khách hàng trên khắp cả nước.</p>
              <p>Chúng tôi tin rằng mua sắm trực tuyến nên là một trải nghiệm dễ dàng, an toàn và thú vị cho tất cả mọi người.</p>
            </div>
          </div>
          <div class="md:w-1/2">
            <img 
              src="https://images.unsplash.com/photo-1600880292203-757bb62b4baf?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80" 
              alt="Our Team" 
              class="rounded-lg shadow-xl w-full h-auto"
            >
          </div>
        </div>
      </div>
    </section>

    <!-- Our Mission -->
    <section class="py-16 bg-gray-50">
      <div class="container mx-auto px-4">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-gray-800">Sứ Mệnh & Giá Trị Cốt Lõi</h2>
          <div class="w-24 h-1 bg-blue-600 mx-auto mt-4"></div>
        </div>
        
        <div class="grid md:grid-cols-3 gap-8">
          <div class="bg-white p-6 rounded-lg shadow-md text-center">
            <div class="text-blue-600 mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mx-auto" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
              </svg>
            </div>
            <h3 class="text-xl font-semibold mb-2">Chất Lượng Hàng Đầu</h3>
            <p class="text-gray-600">Cam kết cung cấp sản phẩm chính hãng, chất lượng cao với nguồn gốc xuất xứ rõ ràng.</p>
          </div>
          
          <div class="bg-white p-6 rounded-lg shadow-md text-center">
            <div class="text-blue-600 mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mx-auto" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 class="text-xl font-semibold mb-2">Giao Hàng Nhanh Chóng</h3>
            <p class="text-gray-600">Đảm bảo giao hàng đúng hẹn với dịch vụ vận chuyển nhanh chóng, đáng tin cậy.</p>
          </div>
          
          <div class="bg-white p-6 rounded-lg shadow-md text-center">
            <div class="text-blue-600 mb-4">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mx-auto" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18.364 5.636l-3.536 3.536m0 5.656l3.536 3.536M9.172 9.172L5.636 5.636m3.536 9.192l-3.536 3.536M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <h3 class="text-xl font-semibold mb-2">Hỗ Trợ 24/7</h3>
            <p class="text-gray-600">Đội ngũ chăm sóc khách hàng luôn sẵn sàng hỗ trợ mọi thắc mắc của bạn.</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Our Team -->
    <section class="py-16 bg-white">
      <div class="container mx-auto px-4">
        <div class="text-center mb-12">
          <h2 class="text-3xl font-bold text-gray-800">Đội Ngũ Của Chúng Tôi</h2>
          <p class="text-gray-600 mt-2 max-w-2xl mx-auto">Gặp gỡ những người đứng đằng sau thành công của chúng tôi</p>
          <div class="w-24 h-1 bg-blue-600 mx-auto mt-4"></div>
        </div>
        
        <div class="grid md:grid-cols-3 gap-8">
          <div v-for="(member, index) in teamMembers" :key="index" class="text-center">
            <div class="bg-gray-100 rounded-full w-48 h-48 mx-auto mb-4 overflow-hidden">
              <img 
                :src="member.image" 
                :alt="member.name"
                class="w-full h-full object-cover"
              >
            </div>
            <h3 class="text-xl font-semibold">{{ member.name }}</h3>
            <p class="text-blue-600 mb-2">{{ member.position }}</p>
            <p class="text-gray-600 text-sm px-4">{{ member.bio }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="bg-blue-700 text-white py-16">
      <div class="container mx-auto px-4 text-center">
        <h2 class="text-3xl md:text-4xl font-bold mb-6">Sẵn sàng trải nghiệm dịch vụ của chúng tôi?</h2>
        <p class="text-xl mb-8 max-w-2xl mx-auto">Tham gia cùng hàng ngàn khách hàng hài lòng khác ngay hôm nay</p>
        <button class="bg-white text-blue-700 font-semibold px-8 py-3 rounded-full hover:bg-gray-100 transition duration-300">
          Mua Sắm Ngay
        </button>
      </div>
    </section>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'AboutPage',
  setup() {
    const teamMembers = [
      {
        name: 'Nguyễn Văn A',
        position: 'Giám Đốc Điều Hành',
        bio: 'Với hơn 10 năm kinh nghiệm trong ngành thương mại điện tử, anh đã dẫn dắt đội ngũ phát triển không ngừng.',
        image: 'https://randomuser.me/api/portraits/men/1.jpg'
      },
      {
        name: 'Trần Thị B',
        position: 'Trưởng Phòng Kinh Doanh',
        bio: 'Chuyên gia trong lĩnh vực phát triển thị trường và xây dựng chiến lược bán hàng hiệu quả.',
        image: 'https://randomuser.me/api/portraits/women/2.jpg'
      },
      {
        name: 'Lê Văn C',
        position: 'Trưởng Phòng Kỹ Thuật',
        bio: 'Chịu trách nhiệm đảm bảo hệ thống vận hành trơn tru và phát triển các tính năng mới.',
        image: 'https://randomuser.me/api/portraits/men/3.jpg'
      }
    ];

    return {
      teamMembers
    };
  }
});
</script>

<style scoped>
.about-page {
  scroll-behavior: smooth;
}

section {
  scroll-margin-top: 80px;
}
</style>
