<template>
    <div class="container mx-auto px-4 py-8">
        <div class="mb-8">
            <h1 class="text-3xl font-bold text-gray-900 mb-2">Danh sách Ứng dụng</h1>
            <p class="text-gray-500">Khám phá tất cả các dự án và ứng dụng từ sinh viên</p>
        </div>

        <div class="flex flex-col lg:flex-row gap-8">
            <!-- Sidebar Filters -->
            <aside class="w-full lg:w-64 flex-shrink-0 space-y-6">
                <ProductFilter />
            </aside>

            <!-- Main Content -->
            <div class="flex-1">
                <!-- Toolbar (Search/Sort) -->
                <div class="flex flex-wrap gap-4 justify-between items-center mb-6">
                    <input type="text" placeholder="Tìm kiếm ứng dụng..."
                        class="px-4 py-2 border border-gray-200 rounded-lg w-full md:w-auto" />
                    <select class="px-4 py-2 border border-gray-200 rounded-lg">
                        <option>Mới nhất</option>
                        <option>Phổ biến nhất</option>
                    </select>
                </div>

                <!-- Grid -->
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <!-- Skeleton or Products -->
                    <ProductCard v-for="product in products" :key="product.id" :product="product" />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import ProductCard from '@/components/client/product/ProductCard.vue';
import ProductFilter from '@/components/client/product/ProductFilter.vue';
import { getPublicFeaturedProducts, type PublicProduct } from '@/services/client/client.service';

const products = ref<PublicProduct[]>([]);

onMounted(async () => {
    // Reuse featured for now, later will use full list API
    products.value = await getPublicFeaturedProducts();
});
</script>
