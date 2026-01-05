<template>
    <div class="space-y-6">
        <div class="flex justify-between items-center">
            <h3 class="text-lg font-medium text-gray-900">Thành viên dự án</h3>
            <button @click="showModal = true"
                class="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 text-sm font-medium">
                + Thêm thành viên
            </button>
        </div>

        <!-- Member List -->
        <div v-if="members.length > 0" class="overflow-hidden border border-gray-200 rounded-lg">
            <table class="min-w-full divide-y divide-gray-200">
                <tbody class="bg-white divide-y divide-gray-200">
                    <tr v-for="member in members" :key="member.id">
                        <td class="px-6 py-4 whitespace-nowrap">
                            <div class="flex items-center">
                                <div class="flex-shrink-0 h-10 w-10">
                                    <div
                                        class="h-10 w-10 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-700 font-bold">
                                        {{ member.fullName.charAt(0) }}
                                    </div>
                                </div>
                                <div class="ml-4">
                                    <div class="text-sm font-medium text-gray-900">{{ member.fullName }}</div>
                                    <div class="text-sm text-gray-500">{{ member.email }}</div>
                                </div>
                            </div>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap">
                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                                :class="member.role === 'LEADER' ? 'bg-purple-100 text-purple-800' : 'bg-gray-100 text-gray-800'">
                                {{ member.role }}
                            </span>
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                            <button @click="handleRemove(member.id)"
                                class="text-red-600 hover:text-red-900">Xóa</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div v-else class="text-center py-12 bg-gray-50 rounded-lg border border-dashed border-gray-300">
            <p class="text-gray-500">Chưa có thành viên nào</p>
        </div>

        <!-- Modal -->
        <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div class="bg-white rounded-xl shadow-xl w-full max-w-lg mx-4 p-6">
                <h3 class="text-xl font-bold mb-4">Thêm thành viên</h3>

                <div class="space-y-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Tìm kiếm thành viên</label>
                        <input v-model="searchQuery" @input="handleSearch" type="text"
                            placeholder="Nhập tên hoặc email..."
                            class="w-full px-4 py-2 border border-gray-300 rounded-lg" />

                        <!-- Search Results -->
                        <div v-if="searchResults.length > 0" class="mt-2 border rounded-lg max-h-48 overflow-y-auto">
                            <div v-for="user in searchResults" :key="user.id" @click="selectUser(user)"
                                class="p-2 hover:bg-gray-50 cursor-pointer flex justify-between items-center"
                                :class="selectedUser?.id === user.id ? 'bg-indigo-50' : ''">
                                <div>
                                    <div class="text-sm font-medium">{{ user.fullName }}</div>
                                    <div class="text-xs text-gray-500">{{ user.email }}</div>
                                </div>
                                <div v-if="selectedUser?.id === user.id" class="text-indigo-600">✓</div>
                            </div>
                        </div>
                    </div>

                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Vai trò</label>
                        <select v-model="selectedRole" class="w-full px-4 py-2 border border-gray-300 rounded-lg">
                            <option value="MEMBER">Thành viên (Member)</option>
                            <option value="LEADER">Trưởng nhóm (Leader)</option>
                        </select>
                    </div>
                </div>

                <div class="mt-6 flex justify-end gap-3">
                    <button @click="showModal = false"
                        class="px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-lg">Hủy</button>
                    <button @click="handleAdd" :disabled="!selectedUser"
                        class="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 disabled:opacity-50">
                        Thêm
                    </button>
                </div>
            </div>
        </div>

    </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';
import { getMembers, addMember, removeMember, searchUsers } from '@/services/admin/app.service';

const props = defineProps({
    appId: {
        type: String,
        required: true
    }
});

const members = ref<any[]>([]);
const showModal = ref(false);
const searchQuery = ref('');
const searchResults = ref<any[]>([]);
const selectedUser = ref<any>(null);
const selectedRole = ref('MEMBER');
let debounceTimer: any = null;

const loadMembers = async () => {
    if (!props.appId) return;
    try {
        members.value = await getMembers(props.appId);
    } catch (e) {
        console.error(e);
    }
}

const handleSearch = () => {
    if (debounceTimer) clearTimeout(debounceTimer);
    debounceTimer = setTimeout(async () => {
        if (!searchQuery.value) {
            searchResults.value = [];
            return;
        }
        try {
            searchResults.value = await searchUsers(searchQuery.value);
        } catch (e) {
            console.error(e);
        }
    }, 300);
}

const selectUser = (user: any) => {
    selectedUser.value = user;
}

const handleAdd = async () => {
    if (!selectedUser.value) return;
    try {
        await addMember(props.appId, {
            customerId: selectedUser.value.id,
            role: selectedRole.value
        });
        toast.success('Thêm thành viên thành công');
        showModal.value = false;
        loadMembers();
        // Reset
        selectedUser.value = null;
        searchQuery.value = '';
        searchResults.value = [];
    } catch (e) {
        toast.error('Có lỗi xảy ra');
    }
}

const handleRemove = async (memberId: string) => {
    if (!confirm('Bạn có chắc chắn muốn xóa thành viên này?')) return;
    try {
        await removeMember(props.appId, memberId);
        toast.success('Xóa thành viên thành công');
        loadMembers();
    } catch (e) {
        toast.error('Có lỗi xảy ra');
    }
}

watch(() => props.appId, (newId) => {
    if (newId) loadMembers();
}, { immediate: true });

</script>
