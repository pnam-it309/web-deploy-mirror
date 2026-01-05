<template>
    <div class="space-y-16 pb-16">
        <!-- Hero Section -->
        <section
            class="relative bg-gradient-to-r from-[#1e293b] to-[#334155] text-white py-20 lg:py-28 overflow-hidden">
            <!-- Background Shapes (Decorative) -->
            <div class="absolute top-0 right-0 w-1/3 h-full bg-white/5 skew-x-12 transform translate-x-1/2"></div>

            <div class="container mx-auto px-4 relative z-10 flex flex-col items-center text-center">
                <h1 class="text-4xl lg:text-5xl font-extrabold mb-6 tracking-tight leading-tight">
                    Khám phá Kho tàng <span class="text-blue-400">Sản phẩm Sinh viên</span>
                </h1>
                <p class="text-lg text-gray-300 max-w-2xl mb-8">
                    Nơi lưu trữ và trưng bày các dự án xuất sắc, đồ án tốt nghiệp và sản phẩm sáng tạo từ cộng đồng sinh
                    viên FPL UDPM.
                </p>

                <div class="flex gap-4">
                    <router-link to="/products"
                        class="px-8 py-3 bg-blue-600 hover:bg-blue-500 text-white font-semibold rounded-full transition-all shadow-lg hover:shadow-blue-500/30">
                        Xem tất cả sản phẩm
                    </router-link>
                    <a href="#featured"
                        class="px-8 py-3 bg-white/10 hover:bg-white/20 text-white font-semibold rounded-full backdrop-blur-sm transition-all">
                        Nổi bật
                    </a>
                </div>
            </div>
        </section>

        <!-- Domains Section -->
        <section class="container mx-auto px-4">
            <div class="text-center mb-12">
                <h2 class="text-3xl font-bold text-gray-900 mb-4">Lĩnh vực hoạt động</h2>
                <div class="w-20 h-1 bg-blue-600 mx-auto rounded"></div>
            </div>

            <div class="grid grid-cols-2 md:grid-cols-4 gap-6">
                <DomainCard v-for="domain in domains" :key="domain.id" :domain="domain" class="h-full" />
            </div>
        </section>

        <!-- Product Technologies Slider -->
        <TechnologySlider />

        <!-- Featured Products Section -->
        <section id="featured" class="container mx-auto px-4">
            <div class="flex items-end justify-between mb-12">
                <div>
                    <h2 class="text-3xl font-bold text-gray-900 mb-2">Sản phẩm Nổi bật</h2>
                    <p class="text-gray-500">Các dự án được đánh giá cao và có tính ứng dụng thực tế</p>
                </div>
                <router-link to="/products"
                    class="hidden md:flex items-center text-blue-600 hover:text-blue-700 font-medium">
                    Xem thêm <span class="ml-1">&rarr;</span>
                </router-link>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                <ProductCard v-for="product in featuredProducts" :key="product.id" :product="product" />
            </div>

            <!-- Mobile View More -->
            <div class="mt-8 text-center md:hidden">
                <router-link to="/products" class="inline-flex items-center text-blue-600 font-medium">
                    Xem tất cả sản phẩm &rarr;
                </router-link>
            </div>
        </section>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import DomainCard from '@/components/client/domain/DomainCard.vue';
import ProductCard from '@/components/client/product/ProductCard.vue';
import TechnologySlider from '@/components/client/home/TechnologySlider.vue';
import { getPublicDomains, getPublicFeaturedProducts, type PublicDomain, type PublicProduct } from '@/services/client/client.service';

const domains = ref<PublicDomain[]>([]);
const featuredProducts = ref<PublicProduct[]>([]);

onMounted(async () => {
    try {
        const [domainsData, productsData] = await Promise.all([
            getPublicDomains(),
            getPublicFeaturedProducts()
        ]);
        domains.value = domainsData || [];
        featuredProducts.value = productsData?.data || productsData || [];
    } catch (error) {
        console.error("Failed to load home data", error);
    }
});
</script>
