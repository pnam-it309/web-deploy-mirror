<template>
    <div class="mt-8">
        <h3 class="text-xl font-bold text-gray-900 mb-6">Nhóm thực hiện</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div v-for="(member, idx) in team" :key="idx"
                class="flex items-center gap-4 p-4 rounded-xl border border-gray-100 bg-gray-50 hover:bg-white hover:shadow-md transition-all">
                <div class="relative">
                    <img :src="member.avatar || 'https://ui-avatars.com/api/?name=' + member.name" :alt="member.name"
                        class="w-12 h-12 rounded-full object-cover border-2 border-white shadow-sm" />
                    <span v-if="member.isLeader" title="Nhóm trưởng"
                        class="absolute -top-1 -right-1 w-5 h-5 bg-yellow-400 rounded-full flex items-center justify-center text-[10px] text-yellow-900 border border-white">
                        ★
                    </span>
                </div>
                <div>
                    <p class="font-semibold text-gray-900">{{ member.name }}</p>
                    <p class="text-sm text-gray-500">{{ member.studentCode }} - {{ member.role }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
export interface TeamMember {
    name: string;
    studentCode: string;
    role: string;
    avatar?: string;
    isLeader?: boolean;
}

defineProps<{
    team: TeamMember[]
}>();
</script>