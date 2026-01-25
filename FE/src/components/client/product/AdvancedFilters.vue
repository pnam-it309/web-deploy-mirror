<template>
    <div
        class="advanced-filters bg-white dark:bg-gray-800 rounded-xl shadow-sm border border-gray-100 dark:border-gray-700 p-6">
        <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-semibold text-gray-900 dark:text-white flex items-center gap-2">
                <svg class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
                </svg>
                Bộ lọc nâng cao
            </h3>
            <button v-if="hasActiveFilters" @click="clearAllFilters"
                class="text-sm text-red-500 hover:text-red-600 dark:text-red-400">
                Xóa tất cả
            </button>
        </div>

        <!-- Domains Multi-Select -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Lĩnh vực</label>
            <div class="flex flex-wrap gap-2">
                <button v-for="domain in domains" :key="domain.id" @click="toggleDomain(domain.id)"
                    class="px-3 py-1.5 text-sm rounded-full border transition-all"
                    :class="selectedDomains.includes(domain.id)
                        ? 'bg-blue-600 text-white border-blue-600'
                        : 'bg-white dark:bg-gray-700 text-gray-600 dark:text-gray-300 border-gray-200 dark:border-gray-600 hover:border-blue-400'">
                    {{ domain.name }}
                </button>
            </div>
        </div>

        <!-- Technologies Multi-Select -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Công nghệ</label>
            <div class="flex flex-wrap gap-2 max-h-32 overflow-y-auto">
                <button v-for="tech in technologies" :key="tech.id" @click="toggleTechnology(tech.id)"
                    class="px-3 py-1.5 text-sm rounded-full border transition-all flex items-center gap-1.5"
                    :class="selectedTechnologies.includes(tech.id)
                        ? 'bg-green-600 text-white border-green-600'
                        : 'bg-white dark:bg-gray-700 text-gray-600 dark:text-gray-300 border-gray-200 dark:border-gray-600 hover:border-green-400'">
                    <img v-if="tech.icon" :src="tech.icon" class="w-4 h-4" />
                    {{ tech.name }}
                </button>
            </div>
        </div>

        <!-- Year Filter -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Năm tạo</label>
            <select v-model.number="selectedYear"
                class="w-full px-3 py-2 bg-white dark:bg-gray-700 border border-gray-200 dark:border-gray-600 rounded-lg text-gray-700 dark:text-gray-300 focus:ring-2 focus:ring-blue-500">
                <option :value="null">Tất cả</option>
                <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
            </select>
        </div>

        <!-- Team Size Filter -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Số thành viên</label>
            <div class="flex gap-2">
                <button v-for="size in teamSizes" :key="size.value"
                    @click="selectedTeamSize = selectedTeamSize === size.value ? null : size.value"
                    class="flex-1 px-3 py-2 text-sm rounded-lg border transition-all text-center"
                    :class="selectedTeamSize === size.value
                        ? 'bg-purple-600 text-white border-purple-600'
                        : 'bg-white dark:bg-gray-700 text-gray-600 dark:text-gray-300 border-gray-200 dark:border-gray-600 hover:border-purple-400'">
                    {{ size.label }}
                </button>
            </div>
        </div>

        <!-- Sort -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Sắp xếp</label>
            <select v-model="sortBy"
                class="w-full px-3 py-2 bg-white dark:bg-gray-700 border border-gray-200 dark:border-gray-600 rounded-lg text-gray-700 dark:text-gray-300 focus:ring-2 focus:ring-blue-500">
                <option value="newest">Mới nhất</option>
                <option value="oldest">Cũ nhất</option>
                <option value="popular">Phổ biến nhất</option>
                <option value="name">Tên A-Z</option>
            </select>
        </div>

        <!-- Apply Button -->
        <button @click="applyFilters"
            class="w-full py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium">
            Áp dụng bộ lọc
        </button>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getPublicDomains, getTechnologies } from '@/services/client/client.service'

interface Domain {
    id: string
    name: string
}

interface Technology {
    id: string
    name: string
    icon?: string
}

const emit = defineEmits<{
    (e: 'filter', filters: FilterState): void
}>()

interface FilterState {
    domains: string[]
    technologies: string[]
    year: number | null
    teamSize: string | null
    sortBy: string
}

const domains = ref<Domain[]>([])
const technologies = ref<Technology[]>([])

const selectedDomains = ref<string[]>([])
const selectedTechnologies = ref<string[]>([])
const selectedYear = ref<number | null>(null)
const selectedTeamSize = ref<string | null>(null)
const sortBy = ref('newest')

const years = computed(() => {
    const currentYear = new Date().getFullYear()
    return Array.from({ length: 5 }, (_, i) => currentYear - i)
})

const teamSizes = [
    { value: '1-2', label: '1-2' },
    { value: '3-5', label: '3-5' },
    { value: '6-10', label: '6-10' },
    { value: '10+', label: '10+' }
]

const hasActiveFilters = computed(() => {
    return selectedDomains.value.length > 0
        || selectedTechnologies.value.length > 0
        || selectedYear.value !== null
        || selectedTeamSize.value !== null
})

const toggleDomain = (id: string) => {
    const index = selectedDomains.value.indexOf(id)
    if (index === -1) {
        selectedDomains.value.push(id)
    } else {
        selectedDomains.value.splice(index, 1)
    }
}

const toggleTechnology = (id: string) => {
    const index = selectedTechnologies.value.indexOf(id)
    if (index === -1) {
        selectedTechnologies.value.push(id)
    } else {
        selectedTechnologies.value.splice(index, 1)
    }
}

const clearAllFilters = () => {
    selectedDomains.value = []
    selectedTechnologies.value = []
    selectedYear.value = null
    selectedTeamSize.value = null
    sortBy.value = 'newest'
    applyFilters()
}

const applyFilters = () => {
    emit('filter', {
        domains: selectedDomains.value,
        technologies: selectedTechnologies.value,
        year: selectedYear.value,
        teamSize: selectedTeamSize.value,
        sortBy: sortBy.value
    })
}

onMounted(async () => {
    try {
        const [domainsRes, techRes] = await Promise.all([
            getPublicDomains(),
            getTechnologies()
        ])
        domains.value = domainsRes
        technologies.value = techRes
    } catch (error) {
        console.error('Failed to load filter options:', error)
    }
})
</script>
