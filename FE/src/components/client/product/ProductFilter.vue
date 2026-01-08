<template>
    <div class="bg-white p-4 rounded-xl border border-gray-100 sticky top-24">
        <div class="flex justify-between items-center mb-4">
            <h3 class="font-semibold text-gray-900">Bộ lọc</h3>
            <button @click="resetFilters" class="text-sm text-blue-600 hover:text-blue-700">Đặt lại</button>
        </div>

        <!-- Domain Filter -->
        <div class="mb-6">
            <h4 class="text-sm font-medium text-gray-700 mb-3">Lĩnh vực</h4>
            <div class="space-y-2 max-h-48 overflow-y-auto">
                <template v-for="domain in domains" :key="domain.id">
                    <label class="flex items-center gap-2 cursor-pointer hover:text-gray-900 text-gray-600">
                        <input type="radio" name="domain" :value="domain.id" v-model="selectedDomainId"
                            class="rounded border-gray-300 text-blue-600 focus:ring-blue-500" />
                        <span>{{ domain.name }}</span>
                    </label>
                </template>
            </div>
        </div>

        <!-- Tech Filter -->
        <div>
            <h4 class="text-sm font-medium text-gray-700 mb-3">Công nghệ</h4>
            <div class="flex flex-wrap gap-2">
                <template v-for="tech in technologies" :key="tech.id">
                    <button @click="toggleTech(tech.id)" class="px-3 py-1 rounded-full text-xs transition-colors border"
                        :class="selectedTechIds.includes(tech.id)
                            ? 'bg-blue-100 text-blue-700 border-blue-200'
                            : 'bg-gray-50 text-gray-600 border-gray-200 hover:bg-gray-100'">
                        {{ tech.name }}
                    </button>
                </template>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { getPublicDomains, getTechnologies, type PublicDomain, type PublicTechnology, type ProductFilterParams } from '@/services/client/client.service';

const route = useRoute();
const domains = ref<PublicDomain[]>([]);
const technologies = ref<PublicTechnology[]>([]);

const selectedDomainId = ref<string>('');
const selectedTechIds = ref<string[]>([]);

const emit = defineEmits<{
    (e: 'change', filters: Partial<ProductFilterParams>): void;
}>();

onMounted(async () => {
    try {
        const [domainsRes, techsRes] = await Promise.all([
            getPublicDomains(),
            getTechnologies()
        ]);
        domains.value = domainsRes;
        technologies.value = techsRes;

        // Init from URL usually, but ProductListPage handles initial load. 
        // We can sync if needed.
        if (route.query.domain) {
            selectedDomainId.value = route.query.domain as string;
        }
    } catch (error) {
        console.error("Failed to load filter data", error);
    }
});

const toggleTech = (id: string) => {
    if (selectedTechIds.value.includes(id)) {
        selectedTechIds.value = selectedTechIds.value.filter(tId => tId !== id);
    } else {
        selectedTechIds.value.push(id);
    }
    emitChange();
};

const resetFilters = () => {
    selectedDomainId.value = '';
    selectedTechIds.value = [];
    emitChange();
};

const emitChange = () => {
    emit('change', {
        domainId: selectedDomainId.value,
        technologyIds: selectedTechIds.value
    });
};

watch(selectedDomainId, () => {
    emitChange();
});
</script>