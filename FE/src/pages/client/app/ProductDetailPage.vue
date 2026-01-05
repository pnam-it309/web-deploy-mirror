<template>
    <div v-if="product" class="container mx-auto px-4 py-8">
        <!-- Breadcrumbs -->
        <nav class="flex items-center gap-2 text-sm text-gray-500 mb-6">
            <router-link to="/" class="hover:text-blue-600">Trang chủ</router-link>
            <span>/</span>
            <router-link to="/products" class="hover:text-blue-600">Sản phẩm</router-link>
            <span>/</span>
            <span class="text-gray-900 font-medium truncate max-w-[200px]">{{ product.name }}</span>
        </nav>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <!-- Left Column: Media & Info -->
            <div class="lg:col-span-2 space-y-8">
                <MediaSlider :media="product.media" />

                <div>
                    <div class="flex flex-wrap items-center gap-3 mb-4">
                        <span class="px-3 py-1 bg-blue-50 text-blue-600 rounded-full text-sm font-semibold">
                            {{ product.domainName }}
                        </span>
                        <div class="flex gap-2">
                            <img v-for="(tech, idx) in product.technologies" :key="idx" :src="tech.icon"
                                :title="tech.name" class="w-6 h-6 object-contain" />
                        </div>
                    </div>

                    <h1 class="text-3xl lg:text-4xl font-bold text-gray-900 mb-4">{{ product.name }}</h1>

                    <div class="prose max-w-none text-gray-600">
                        <p>{{ product.detailDescription }}</p>
                    </div>

                    <div class="flex flex-wrap gap-4 mt-8">
                        <a v-if="product.website" :href="product.website" target="_blank"
                            class="px-6 py-3 bg-blue-600 text-white font-semibold rounded-xl hover:bg-blue-700 transition-colors flex items-center gap-2">
                            <span>Xem Demo</span>
                            <span class="text-lg">↗</span>
                        </a>
                        <a v-if="product.sourceCode" :href="product.sourceCode" target="_blank"
                            class="px-6 py-3 bg-gray-100 text-gray-700 font-semibold rounded-xl hover:bg-gray-200 transition-colors flex items-center gap-2">
                            <span>GitHub</span>
                        </a>
                    </div>
                </div>

                <FeatureList :features="product.features" />
            </div>

            <!-- Right Column: Team & Related -->
            <div class="space-y-8">
                <TeamList :team="product.team" />

                <!-- Can add "Related Products" here later -->
            </div>
        </div>
    </div>
    <div v-else class="container mx-auto px-4 py-20 text-center">
        <div class="text-gray-500">Đang tải thông tin sản phẩm...</div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getProductDetail, type ProductDetail } from '@/services/client/client.service';
import MediaSlider from '@/components/client/product/MediaSlider.vue';
import TeamList from '@/components/client/product/TeamList.vue';
import FeatureList from '@/components/client/product/FeatureList.vue';

const route = useRoute();
const product = ref<ProductDetail | null>(null);

onMounted(async () => {
    const id = route.params.id as string;
    if (id) {
        product.value = await getProductDetail(id);
    }
});
</script>
