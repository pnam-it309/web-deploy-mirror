<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import roleService, { type RoleResponse, type PermissionResponse } from '@/services/admin/role.service';
import BaseBreadcrumb from '@/components/base/BaseBreadcrumb.vue';
import BaseSpinner from '@/components/base/BaseSpinner.vue';
import { toast } from 'vue3-toastify';
import { PlusIcon, PencilIcon, TrashIcon, ShieldCheckIcon } from '@heroicons/vue/24/outline';

const roles = ref<RoleResponse[]>([]);
const permissions = ref<PermissionResponse[]>([]);
const isLoading = ref(false);
const showModal = ref(false);
const showPermissionModal = ref(false);
const selectedRole = ref<RoleResponse | null>(null);

// Form data
const form = ref({
    name: '',
    description: '',
    permissionIds: [] as string[],
});

const loadRoles = async () => {
    isLoading.value = true;
    try {
        roles.value = await roleService.getAllRoles();
    } catch (error: any) {
        toast.error('Không thể tải danh sách vai trò');
        console.error(error);
    } finally {
        isLoading.value = false;
    }
};

const loadPermissions = async () => {
    try {
        permissions.value = await roleService.getAllPermissions();
    } catch (error: any) {
        toast.error('Không thể tải danh sách quyền');
        console.error(error);
    }
};

onMounted(() => {
    loadRoles();
    loadPermissions();
});

// Group permissions by category
const permissionsByCategory = computed(() => {
    const grouped: Record<string, PermissionResponse[]> = {};
    permissions.value.forEach(perm => {
        if (!grouped[perm.category]) {
            grouped[perm.category] = [];
        }
        grouped[perm.category].push(perm);
    });
    return grouped;
});

const handleCreate = () => {
    selectedRole.value = null;
    form.value = {
        name: '',
        description: '',
        permissionIds: [],
    };
    showModal.value = true;
};

const handleEdit = (role: RoleResponse) => {
    selectedRole.value = role;
    form.value = {
        name: role.name,
        description: role.description || '',
        permissionIds: role.permissions.map(p => p.id),
    };
    showModal.value = true;
};

const handleSave = async () => {
    if (!form.value.name.trim()) {
        toast.error('Tên vai trò không được để trống');
        return;
    }

    try {
        if (selectedRole.value) {
            await roleService.updateRole(selectedRole.value.id, form.value);
            toast.success('Cập nhật vai trò thành công');
        } else {
            await roleService.createRole(form.value);
            toast.success('Tạo vai trò thành công');
        }
        showModal.value = false;
        loadRoles();
    } catch (error: any) {
        const msg = error.response?.data?.message || 'Có lỗi xảy ra';
        toast.error(msg);
    }
};

const handleDelete = async (role: RoleResponse) => {
    if (!confirm(`Bạn có chắc muốn xoá vai trò "${role.name}"?`)) {
        return;
    }

    try {
        await roleService.deleteRole(role.id);
        toast.success('Xoá vai trò thành công');
        loadRoles();
    } catch (error: any) {
        const msg = error.response?.data?.message || 'Không thể xoá vai trò';
        toast.error(msg);
    }
};

const handleManagePermissions = (role: RoleResponse) => {
    selectedRole.value = role;
    form.value.permissionIds = role.permissions.map(p => p.id);
    showPermissionModal.value = true;
};

const handleSavePermissions = async () => {
    if (!selectedRole.value) return;

    try {
        await roleService.assignPermissions(selectedRole.value.id, form.value.permissionIds);
        toast.success('Cập nhật quyền thành công');
        showPermissionModal.value = false;
        loadRoles();
    } catch (error: any) {
        toast.error('Có lỗi xảy ra');
    }
};

const togglePermission = (permissionId: string) => {
    const index = form.value.permissionIds.indexOf(permissionId);
    if (index > -1) {
        form.value.permissionIds.splice(index, 1);
    } else {
        form.value.permissionIds.push(permissionId);
    }
};

const toggleAllInCategory = (category: string) => {
    const categoryPerms = permissionsByCategory.value[category];
    const allSelected = categoryPerms.every(p => form.value.permissionIds.includes(p.id));

    if (allSelected) {
        // Deselect all
        form.value.permissionIds = form.value.permissionIds.filter(
            id => !categoryPerms.some(p => p.id === id)
        );
    } else {
        // Select all
        categoryPerms.forEach(p => {
            if (!form.value.permissionIds.includes(p.id)) {
                form.value.permissionIds.push(p.id);
            }
        });
    }
};
</script>

<template>
    <div class="p-6 h-full flex flex-col bg-gray-50 dark:bg-gray-900 transition-colors duration-300">
        <div class="mb-6 shrink-0">
            <BaseBreadcrumb :items="[{ label: 'Admin', to: '/admin' }, { label: 'Quản lý Vai trò & Quyền' }]" />
            <h1 class="text-3xl font-bold text-gray-900 dark:text-white font-serif uppercase tracking-tight mt-2">Vai trò & Quyền hạn</h1>
        </div>

        <!-- Action Bar -->
        <div class="mb-6 flex justify-between items-center bg-white dark:bg-gray-800 p-4 rounded-xl border border-gray-100 dark:border-gray-700 shadow-sm relative overflow-hidden">
            <div class="absolute inset-0 bg-blue-50/50 dark:bg-blue-900/10 opacity-0 hover:opacity-100 transition-opacity pointer-events-none"></div>
            <div class="relative z-10 text-sm text-gray-600 dark:text-gray-400 font-medium">
                Tổng số vai trò: <span class="font-bold text-blue-600 dark:text-blue-400 text-lg">{{ roles.length }}</span>
            </div>
            <button @click="handleCreate" class="relative z-10 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-lg transition-all shadow-lg shadow-blue-600/20 text-sm uppercase tracking-wide flex items-center gap-2">
                <PlusIcon class="w-5 h-5" />
                Tạo vai trò mới
            </button>
        </div>

        <!-- Roles Table -->
        <div v-if="isLoading" class="flex-1 flex justify-center items-center">
            <BaseSpinner size="lg" />
        </div>
        <div v-else class="flex-1 overflow-auto custom-scrollbar bg-white dark:bg-gray-800 rounded-xl border border-gray-200 dark:border-gray-700 shadow-sm">
            <table class="w-full min-w-max border-collapse">
                <thead class="bg-gray-50 dark:bg-gray-900/50 sticky top-0 z-10 backdrop-blur-sm">
                    <tr>
                        <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Tên vai trò</th>
                        <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Mô tả</th>
                        <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Số quyền</th>
                        <th class="px-6 py-4 text-left text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Ngày tạo</th>
                        <th class="px-6 py-4 text-center text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider">Thao tác</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-gray-100 dark:divide-gray-700/50">
                    <tr v-for="role in roles" :key="role.id" class="hover:bg-blue-50 dark:hover:bg-blue-900/10 transition-colors group">
                        <td class="px-6 py-4">
                            <span class="font-bold text-gray-900 dark:text-white group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">{{ role.name }}</span>
                        </td>
                        <td class="px-6 py-4 text-gray-600 dark:text-gray-400 text-sm italic">{{ role.description || '-' }}</td>
                        <td class="px-6 py-4">
                            <span
                                class="inline-flex items-center px-2.5 py-0.5 rounded-full text-[11px] font-bold bg-blue-50 dark:bg-blue-900/30 text-blue-700 dark:text-blue-300 border border-blue-100 dark:border-blue-800">
                                {{ role.permissions.length }} quyền
                            </span>
                        </td>
                        <td class="px-6 py-4 text-xs font-mono text-gray-500 dark:text-gray-400">
                            {{ new Date(role.createdAt).toLocaleDateString('vi-VN') }}
                        </td>
                        <td class="px-6 py-4">
                            <div class="flex justify-center gap-2 opacity-60 group-hover:opacity-100 transition-opacity">
                                <button @click="handleManagePermissions(role)"
                                    class="p-2 text-indigo-600 dark:text-indigo-400 hover:bg-indigo-50 dark:hover:bg-indigo-900/30 rounded-lg transition-colors border border-transparent hover:border-indigo-100 dark:hover:border-indigo-800" title="Phân quyền">
                                    <ShieldCheckIcon class="w-5 h-5" />
                                </button>
                                <button @click="handleEdit(role)"
                                    class="p-2 text-blue-600 dark:text-blue-400 hover:bg-blue-50 dark:hover:bg-blue-900/30 rounded-lg transition-colors border border-transparent hover:border-blue-100 dark:hover:border-blue-800" title="Chỉnh sửa">
                                    <PencilIcon class="w-5 h-5" />
                                </button>
                                <button @click="handleDelete(role)"
                                    class="p-2 text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/30 rounded-lg transition-colors border border-transparent hover:border-red-100 dark:hover:border-red-800" title="Xoá">
                                    <TrashIcon class="w-5 h-5" />
                                </button>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Create/Edit Modal -->
        <div v-if="showModal" class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
            <div class="bg-white dark:bg-gray-800 rounded-2xl p-6 w-full max-w-2xl shadow-2xl border border-gray-100 dark:border-gray-700 transform transition-all scale-100">
                <h2 class="text-xl font-bold mb-6 text-gray-900 dark:text-white flex items-center gap-2">
                    <span class="w-1.5 h-6 bg-blue-600 rounded-full"></span>
                    {{ selectedRole ? 'Chỉnh sửa vai trò' : 'Tạo vai trò mới' }}
                </h2>

                <div class="space-y-4">
                    <div>
                        <label class="block text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">Tên vai trò <span
                                class="text-red-500">*</span></label>
                        <input v-model="form.name" type="text" class="w-full bg-gray-50 dark:bg-gray-900 border border-gray-200 dark:border-gray-700 rounded-lg px-4 py-2.5 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 outline-none transition-all placeholder:text-gray-400 dark:placeholder:text-gray-500"
                            placeholder="VD: Editor" />
                    </div>

                    <div>
                        <label class="block text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-1.5">Mô tả</label>
                        <textarea v-model="form.description" class="w-full bg-gray-50 dark:bg-gray-900 border border-gray-200 dark:border-gray-700 rounded-lg px-4 py-2.5 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 outline-none transition-all placeholder:text-gray-400 dark:placeholder:text-gray-500" rows="3"
                            placeholder="Mô tả vai trò..."></textarea>
                    </div>

                    <div>
                        <label class="block text-xs font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-2">Quyền hạn</label>
                        <div class="border border-gray-200 dark:border-gray-700 rounded-xl p-4 max-h-64 overflow-y-auto custom-scrollbar bg-gray-50/50 dark:bg-gray-900/50">
                            <div v-for="(perms, category) in permissionsByCategory" :key="category"
                                class="mb-4 last:mb-0">
                                <div class="flex items-center mb-2 px-2 py-1 bg-white dark:bg-gray-800 rounded-lg border border-gray-100 dark:border-gray-700 sticky top-0 z-10 shadow-sm">
                                    <input type="checkbox" :id="`cat-${category}`"
                                        @change="toggleAllInCategory(category)" class="w-4 h-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500 mr-2" />
                                    <label :for="`cat-${category}`" class="font-bold text-xs text-gray-700 dark:text-gray-300 uppercase tracking-wide">{{ category }}</label>
                                </div>
                                <div class="pl-2 grid grid-cols-2 gap-2">
                                    <div v-for="perm in perms" :key="perm.id" class="flex items-center p-2 rounded hover:bg-white dark:hover:bg-gray-800 transition-colors">
                                        <input type="checkbox" :id="`perm-${perm.id}`"
                                            :checked="form.permissionIds.includes(perm.id)"
                                            @change="togglePermission(perm.id)" class="w-4 h-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500 mr-2" />
                                        <label :for="`perm-${perm.id}`" class="text-sm cursor-pointer text-gray-600 dark:text-gray-300 font-medium">{{ perm.name
                                            }}</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="flex justify-end gap-3 mt-8 pt-4 border-t border-gray-100 dark:border-gray-700">
                    <button @click="showModal = false" class="px-4 py-2 bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-200 rounded-lg font-bold text-sm hover:bg-gray-200 dark:hover:bg-gray-600 transition-colors">Huỷ bỏ</button>
                    <button @click="handleSave"
                        class="px-6 py-2 bg-blue-600 text-white rounded-lg font-bold text-sm hover:bg-blue-700 shadow-lg shadow-blue-600/20 transition-all">
                        {{ selectedRole ? 'Cập nhật' : 'Tạo mới' }}
                    </button>
                </div>
            </div>
        </div>

        <!-- Permission Management Modal -->
        <div v-if="showPermissionModal"
            class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
            <div class="bg-white dark:bg-gray-800 rounded-2xl p-6 w-full max-w-4xl mx-4 shadow-2xl border border-gray-100 dark:border-gray-700 max-h-[90vh] flex flex-col">
                <h2 class="text-xl font-bold mb-6 text-gray-900 dark:text-white flex items-center gap-2 shrink-0">
                     <span class="w-1.5 h-6 bg-indigo-600 rounded-full"></span>
                    Quản lý quyền hạn - <span class="text-indigo-600 dark:text-indigo-400">{{ selectedRole?.name }}</span>
                </h2>

                <div class="flex-1 overflow-y-auto custom-scrollbar border border-gray-200 dark:border-gray-700 rounded-xl bg-gray-50/50 dark:bg-gray-900/50 p-4">
                    <div v-for="(perms, category) in permissionsByCategory" :key="category" class="mb-6 last:mb-0 bg-white dark:bg-gray-800 rounded-xl border border-gray-100 dark:border-gray-700 overflow-hidden shadow-sm">
                        <div class="bg-gray-50 dark:bg-gray-700/50 p-3 border-b border-gray-100 dark:border-gray-700/50 flex items-center justify-between cursor-pointer hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
                             @click="toggleAllInCategory(category)">
                            <div class="flex items-center">
                                <input type="checkbox" :id="`modal-cat-${category}`"
                                    :checked="perms.every(p => form.permissionIds.includes(p.id))"
                                    @click.stop="toggleAllInCategory(category)" class="w-5 h-5 rounded border-gray-300 text-blue-600 focus:ring-blue-500 mr-3" />
                                <label :for="`modal-cat-${category}`" class="font-bold text-sm uppercase tracking-wide text-gray-800 dark:text-gray-200 cursor-pointer pointer-events-none">{{ category }}</label>
                            </div>
                            <span class="text-xs font-bold px-2 py-1 rounded bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600" :class="perms.filter(p => form.permissionIds.includes(p.id)).length === perms.length ? 'text-green-600 dark:text-green-400' : 'text-gray-500 dark:text-gray-400'">
                                {{perms.filter(p => form.permissionIds.includes(p.id)).length}} / {{ perms.length }}
                            </span>
                        </div>
                        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-1 p-2">
                             <div v-for="perm in perms" :key="perm.id" 
                                class="flex items-start p-2 rounded-lg hover:bg-blue-50 dark:hover:bg-blue-900/10 transition-colors cursor-pointer group"
                                @click="togglePermission(perm.id)">
                                <input type="checkbox" :id="`modal-perm-${perm.id}`"
                                    :checked="form.permissionIds.includes(perm.id)" @click.stop="togglePermission(perm.id)"
                                    class="w-4 h-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500 mr-3 mt-1" />
                                <div class="flex-1 min-w-0">
                                    <div class="text-sm font-bold text-gray-700 dark:text-gray-300 group-hover:text-blue-700 dark:group-hover:text-blue-400 transition-colors">{{ perm.name }}</div>
                                    <div class="text-[11px] text-gray-500 dark:text-gray-400 mt-0.5 leading-snug">{{ perm.description }}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="flex justify-end gap-3 mt-6 shrink-0 pt-4 border-t border-gray-100 dark:border-gray-700">
                    <button @click="showPermissionModal = false"
                        class="px-4 py-2 bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-200 rounded-lg font-bold text-sm hover:bg-gray-200 dark:hover:bg-gray-600 transition-colors">Huỷ bỏ</button>
                    <button @click="handleSavePermissions"
                        class="px-6 py-2 bg-indigo-600 text-white rounded-lg font-bold text-sm hover:bg-indigo-700 shadow-lg shadow-indigo-600/20 transition-all">
                        Lưu thay đổi
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.btn-primary {
    @apply px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors;
}

.custom-scrollbar::-webkit-scrollbar {
    width: 6px;
    height: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
    background: #f1f1f1;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 3px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
    background: #555;
}
</style>
