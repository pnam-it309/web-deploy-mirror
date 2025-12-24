<template>
    <div v-if="isOpen" class="fixed inset-0 z-[60] overflow-y-auto" role="dialog" aria-modal="true">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" @click="close"></div>

        <div class="relative z-10 w-full bg-white shadow-2xl transform transition-all sm:max-w-full">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4 border-b border-gray-100">
                <div class="relative max-w-7xl mx-auto flex items-center">
                    <MagnifyingGlassIcon class="h-6 w-6 text-gray-400 absolute left-3" />
                    <input ref="searchInput" v-model="searchQuery" @keydown.enter="handleSearch" type="text"
                        class="w-full pl-12 pr-12 py-4 text-lg border-none focus:ring-0 text-gray-900 placeholder-gray-400"
                        placeholder="Search for products, ergonomic support...">
                    <button @click="close" class="absolute right-0 p-2 text-gray-400 hover:text-gray-500">
                        <XMarkIcon class="h-8 w-8" />
                    </button>
                </div>
            </div>

            <!-- Content Area -->
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
                <div class="flex flex-col md:flex-row gap-12">
                    <!-- Popular Searches -->
                    <div class="w-full md:w-1/4">
                        <h3 class="text-xl font-serif text-gray-900 mb-6">Popular Searches</h3>
                        <ul class="space-y-3">
                            <li v-for="term in popularSearches" :key="term">
                                <button @click="executeSearch(term)"
                                    class="text-gray-600 hover:text-indigo-600 hover:underline text-lg">
                                    {{ term }}
                                </button>
                            </li>
                        </ul>

                        <div class="mt-10">
                            <h4 class="text-sm font-bold text-gray-900 mb-2">Need help deciding?</h4>
                            <a href="#" class="text-gray-500 text-sm underline hover:text-indigo-600">Take our quiz</a>
                            <span class="text-gray-500 text-sm"> to find the right products for your team's
                                needs.</span>
                        </div>
                    </div>

                    <!-- Popular Categories -->
                    <div class="w-full md:w-3/4">
                        <div class="flex items-center justify-between mb-6 border-l border-gray-200 pl-6">
                            <h3 class="text-xl font-serif text-gray-900">Popular Categories</h3>
                        </div>

                        <div class="grid grid-cols-1 sm:grid-cols-3 gap-6 pl-6 border-l border-gray-200">
                            <div v-for="cat in popularCategories" :key="cat.name" class="group cursor-pointer"
                                @click="goToCategory(cat.id)">
                                <div class="relative overflow-hidden mb-3 bg-gray-100 aspect-square">
                                    <span v-if="cat.badge"
                                        class="absolute top-2 left-2 bg-blue-400 text-white text-xs font-bold px-2 py-1 uppercase tracking-wide z-10">
                                        {{ cat.badge }}
                                    </span>
                                    <img :src="cat.image" :alt="cat.name"
                                        class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500">
                                </div>
                                <h4 class="font-bold text-gray-900 group-hover:text-indigo-600">{{ cat.name }}</h4>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick, watch } from 'vue';
import { MagnifyingGlassIcon, XMarkIcon } from '@heroicons/vue/24/outline';
import { useRouter } from 'vue-router';

const props = defineProps<{
    isOpen: boolean;
}>();

const emit = defineEmits(['close']);
const router = useRouter();
const searchQuery = ref('');
const searchInput = ref<HTMLInputElement | null>(null);

const popularSearches = [
    'Ergonomic Chair',
    'Home Office Chairs',
    'Standing Desk',
    'Accessories',
    'Assembly Guides'
];

const popularCategories = [
    {
        id: 'chairs',
        name: 'Ergonomic Chairs',
        image: 'https://images.unsplash.com/photo-1505843490538-5133c6c7d0e1?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80',
        badge: 'New'
    },
    {
        id: 'desks',
        name: 'Standing Desks',
        image: 'https://images.unsplash.com/photo-1497215728101-856f4ea42174?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80',
        badge: 'Bestseller'
    },
    {
        id: 'accessories',
        name: 'Office Accessories',
        image: 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80'
    }
];

const close = () => {
    emit('close');
};

const handleSearch = () => {
    if (searchQuery.value.trim()) {
        executeSearch(searchQuery.value);
    }
};

const executeSearch = (term: string) => {
    router.push({ path: '/customer/products', query: { search: term } });
    close();
};

const goToCategory = (catId: string) => {
    // In a real app this would map IDs to actual categories
    router.push({ path: '/customer/products' });
    close();
};

watch(() => props.isOpen, (newVal) => {
    if (newVal) {
        nextTick(() => {
            searchInput.value?.focus();
        });
    }
});
</script>
