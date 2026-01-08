<template>
    <div class="container mx-auto px-4 py-8">
        <div class="mb-8">
            <h1 class="text-3xl font-bold text-gray-900 mb-2">Danh sách Ứng dụng</h1>
            <p class="text-gray-500">Khám phá tất cả các dự án và ứng dụng từ sinh viên</p>
        </div>

        <div class="flex flex-col lg:flex-row gap-8">
            <!-- Sidebar Filters -->
            <aside class="w-full lg:w-64 flex-shrink-0 space-y-6">
                <ProductFilter @change="handleFilterChange" />
            </aside>

            <!-- Main Content -->
            <div class="flex-1">
                <!-- Toolbar (Search/Sort) -->
                <div class="flex flex-wrap gap-4 justify-between items-center mb-6">
                    <input type="text" placeholder="Tìm kiếm ứng dụng..." :value="filterParams.query"
                        @input="handleSearch"
                        class="px-4 py-2 border border-gray-200 rounded-lg w-full md:w-auto focus:outline-none focus:ring-2 focus:ring-indigo-500" />
                    <select v-model="filterParams.sort" @change="handleFilterChange({ sort: filterParams.sort })"
                        class="px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-indigo-500">
                        <option value="NEWEST">Mới nhất</option>
                        <option value="FEATURED">Nổi bật</option>
                        <option value="VIEW_COUNT">Xem nhiều nhất</option>
                        <option value="NAME_ASC">Tên (A-Z)</option>
                        <option value="NAME_DESC">Tên (Z-A)</option>
                    </select>
                </div>

                <!-- Grid -->
                <!-- Loading State -->
                <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <ProductCardSkeleton v-for="n in 6" :key="n" />
                </div>

                <!-- Products State -->
                <div v-else-if="products.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <ProductCard v-for="product in products" :key="product.id" :product="product" />
                </div>

                <!-- Empty State -->
                <div v-else class="py-12">
                    <EmptyState title="Không tìm thấy sản phẩm"
                        description="Thử thay đổi bộ lọc hoặc tìm kiếm từ khóa khác" :icon="MagnifyingGlassIcon"
                        action-label="Xóa bộ lọc" @action-click="resetFilters" />
                </div>

                <!-- Pagination -->
                <div v-if="!loading && products.length > 0" class="mt-8">
                    <Pagination :current-page="currentPage" :total-pages="totalPages" @change="handlePageChange" />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { MagnifyingGlassIcon } from '@heroicons/vue/24/outline'; // Import Icon
import ProductCard from '@/components/client/product/ProductCard.vue';
import ProductCardSkeleton from '@/components/client/product/ProductCardSkeleton.vue'; // Import Skeleton
import EmptyState from '@/components/common/EmptyState.vue'; // Import EmptyState
import ProductFilter from '@/components/client/product/ProductFilter.vue';
import Pagination from '@/components/common/Pagination.vue';
import { getPublicFeaturedProducts, type PublicProduct, type ProductFilterParams } from '@/services/client/client.service';

const route = useRoute();
const router = useRouter(); // For clearing query
const products = ref<PublicProduct[]>([]);
const loading = ref(false);
const currentPage = ref(1);
const totalPages = ref(1);

const filterParams = reactive<ProductFilterParams>({
    page: 1,
    size: 10,
    query: '',
    sort: 'NEWEST',
    domainId: route.query.domain as string || undefined,
    technologyIds: []
});

const fetchProducts = async () => {
    loading.value = true;
    try {
        const params = { ...filterParams, page: currentPage.value };
        const res = await getPublicFeaturedProducts(params);
        products.value = res.data || [];
        totalPages.value = res.totalPages || 0;
    } catch (error) {
        console.error("Error fetching products", error);
    } finally {
        setTimeout(() => { // Small delay to show skeleton (optional, for UX feel)
            loading.value = false;
        }, 300)
    }
};

const handlePageChange = (page: number) => {
    currentPage.value = page;
    fetchProducts();
    window.scrollTo({ top: 0, behavior: 'smooth' });
};

const handleFilterChange = (newFilters: Partial<ProductFilterParams>) => {
    Object.assign(filterParams, newFilters);
    currentPage.value = 1; // Reset to page 1 on filter change
    fetchProducts();
};

const handleSearch = (e: Event) => {
    const target = e.target as HTMLInputElement;
    filterParams.query = target.value;
    currentPage.value = 1;
    // Debounce
    clearTimeout((window as any)._searchTimer);
    (window as any)._searchTimer = setTimeout(() => {
        fetchProducts();
    }, 500);
};

const resetFilters = () => {
    filterParams.query = '';
    filterParams.domainId = undefined;
    filterParams.technologyIds = [];
    router.replace({ query: {} }); // Clear URL query
    fetchProducts();
}

onMounted(() => {
    fetchProducts();
});

// Watch query params if they change (e.g. from nav)
watch(() => route.query.domain, (newDomain) => {
    filterParams.domainId = newDomain as string;
    currentPage.value = 1;
    fetchProducts();
});
</script>