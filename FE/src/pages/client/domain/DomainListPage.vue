<template>
    <div class="container mx-auto px-4 py-8">
        <div class="mb-8 text-center">
            <h1 class="text-3xl font-bold text-[#1e293b] mb-2">Lĩnh vực hoạt động</h1>
            <p class="text-gray-500 max-w-2xl mx-auto">
                Khám phá các lĩnh vực công nghệ đa dạng mà sinh viên FPL đang nghiên cứu và phát triển sản phẩm.
            </p>
        </div>

        <!-- Toolbar -->
        <DomainToolbar v-model="searchQuery" :total="filteredDomains.length" v-model:layout="layout" />

        <!-- Grid/List -->
        <div v-if="filteredDomains.length > 0" :class="[
            layout === 'grid'
                ? 'grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6'
                : 'flex flex-col gap-4'
        ]">
            <template v-for="domain in filteredDomains" :key="domain.id">
                <router-link :to="{ name: ROUTES_CONSTANTS.CUSTOMER.children.APPS.name, query: { domain: domain.id } }">
                    <DomainCard :domain="domain" :layout="layout" />
                </router-link>
            </template>
        </div>

        <!-- Empty State -->
        <div v-else class="text-center py-12">
            <div class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-gray-100 mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-gray-400" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
            </div>
            <h3 class="text-lg font-medium text-gray-900">Không tìm thấy kết quả</h3>
            <p class="text-gray-500 mt-1">Vui lòng thử từ khóa khác</p>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { getPublicDomains, type PublicDomain } from '@/services/client/client.service';
import DomainCard from '@/components/client/domain/DomainCard.vue';
import DomainToolbar from '@/components/client/domain/DomainToolbar.vue';
import { ROUTES_CONSTANTS } from '@/constants/path';

const domains = ref<PublicDomain[]>([]);
const searchQuery = ref('');
const loading = ref(true);
const layout = ref<'grid' | 'list'>('grid'); // Default grid

onMounted(async () => {
    try {
        domains.value = await getPublicDomains();
    } catch (error) {
        console.error('Error fetching domains:', error);
    } finally {
        loading.value = false;
    }
});

const filteredDomains = computed(() => {
    if (!searchQuery.value) return domains.value;
    const query = searchQuery.value.toLowerCase();
    return domains.value.filter(d =>
        d.name.toLowerCase().includes(query)
    );
});
</script>