<script setup lang="ts">
import { ref } from 'vue';
import apiClient from '@/services/api/api';
import type { AppMemberRequest } from '@/types/admin.types';

interface ExtendedAppMember extends AppMemberRequest {
  _tempName?: string;
  _tempEmail?: string;
  _tempAvatar?: string;
}

const props = defineProps<{ modelValue: ExtendedAppMember[] }>();
const emit = defineEmits(['update:modelValue']);

const searchQuery = ref('');
const searchResults = ref<any[]>([]);
const showDropdown = ref(false);
const isSearching = ref(false);

let timeout: any;
const handleSearch = () => {
  if (!searchQuery.value) {
    searchResults.value = [];
    showDropdown.value = false;
    return;
  }
  clearTimeout(timeout);
  timeout = setTimeout(async () => {
    isSearching.value = true;
    try {
      const res = await apiClient.get('/admin/customers/search', { params: { keyword: searchQuery.value } });
      searchResults.value = res.data;
      showDropdown.value = true;
    } catch (e) { console.error(e); } 
    finally { isSearching.value = false; }
  }, 300);
};

// 2. Chọn User từ Dropdown (User cũ)
const selectUser = (user: any) => {
  addMemberToModel(user.id, user.fullName, user.email, user.avatar);
};

// 3. XỬ LÝ ENTER (Thêm Email mới chưa có trong DB)
const handleEnterKey = () => {
  const email = searchQuery.value.trim();
  
  // Validate Email cơ bản
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    // Nếu không phải email, có thể alert hoặc bỏ qua
    return;
  }

  // Nếu email này trùng với một user trong dropdown -> Chọn user đó luôn cho chuẩn
  const existingUser = searchResults.value.find(u => u.email === email);
  if (existingUser) {
    selectUser(existingUser);
    return;
  }

  // Nếu là email mới tinh -> Thêm vào với ID là chính cái Email đó (Backend sẽ xử lý sau)
  addMemberToModel(email, 'Khách mời', email, ''); 
};

// Hàm chung để push vào mảng
const addMemberToModel = (idOrEmail: string, name: string, email: string, avatar: string) => {
  // Check trùng
  if (props.modelValue.some(m => m.customerId === idOrEmail || m._tempEmail === email)) {
    alert('Email này đã có trong danh sách!');
    searchQuery.value = '';
    showDropdown.value = false;
    return;
  }

  const newMember: ExtendedAppMember = {
    customerId: idOrEmail, // Có thể là UUID hoặc là Email (nếu mới)
    role: 'MEMBER',
    _tempName: name,
    _tempEmail: email,
    _tempAvatar: avatar
  };

  emit('update:modelValue', [...props.modelValue, newMember]);
  searchQuery.value = '';
  showDropdown.value = false;
};

const removeMember = (index: number) => {
  const newList = [...props.modelValue];
  newList.splice(index, 1);
  emit('update:modelValue', newList);
};

const updateRole = (index: number, role: any) => {
  const newList = [...props.modelValue];
  newList[index].role = role;
  emit('update:modelValue', newList);
};
</script>

<template>
  <div class="space-y-4">
    <h4 class="text-sm font-bold text-dark uppercase tracking-wide">Thành viên nhóm</h4>

    <div class="relative">
      <div class="flex items-center border border-gray-200 rounded-sm bg-white px-3 py-2 focus-within:border-primary">
        <input 
          v-model="searchQuery"
          @input="handleSearch"
          @keydown.enter.prevent="handleEnterKey"
          type="text" 
          placeholder="Nhập email và nhấn Enter..." 
          class="w-full text-sm outline-none text-dark"
        />
        <span v-if="searchQuery" class="text-[10px] text-gray-400 border border-gray-200 px-1 rounded bg-gray-50">Enter để thêm</span>
      </div>

      <div v-if="showDropdown && searchResults.length > 0" class="absolute top-full left-0 w-full bg-white border border-gray-100 shadow-xl z-50 max-h-60 overflow-y-auto">
        <div v-for="user in searchResults" :key="user.id" @click="selectUser(user)" class="flex items-center gap-3 p-3 hover:bg-yellow-50 cursor-pointer border-b border-gray-50">
          <div class="w-8 h-8 rounded-full bg-gray-200 flex items-center justify-center overflow-hidden font-bold text-xs text-gray-500">
             <img v-if="user.avatar" :src="user.avatar" class="w-full h-full object-cover" />
             <span v-else>{{ user.fullName?.charAt(0) }}</span>
          </div>
          <div class="flex flex-col">
            <span class="text-sm font-bold">{{ user.fullName }}</span>
            <span class="text-xs text-gray-500">{{ user.email }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="space-y-2">
      <div v-for="(member, idx) in modelValue" :key="idx" class="flex items-center gap-3 bg-gray-50 p-2 rounded-sm border border-gray-100">
        <div class="w-8 h-8 rounded-full bg-white border border-gray-200 flex items-center justify-center font-bold text-xs text-gray-400 overflow-hidden">
           <img v-if="member._tempAvatar" :src="member._tempAvatar" class="w-full h-full object-cover" />
           <span v-else>{{ member._tempName?.charAt(0) || '@' }}</span>
        </div>
        
        <div class="flex-1 min-w-0">
          <p class="text-sm font-bold text-dark truncate">{{ member._tempName }}</p>
          <p class="text-xs text-gray-500 truncate">{{ member._tempEmail || member.customerId }}</p>
        </div>

        <select :value="member.role" @change="updateRole(idx, ($event.target as HTMLSelectElement).value)" class="text-xs bg-transparent font-medium cursor-pointer text-right outline-none">
           <option value="MEMBER">Thành viên</option>
           <option value="LEADER">Trưởng nhóm</option>
        </select>
        
        <button @click="removeMember(idx)" class="text-gray-400 hover:text-red-500">
          <svg class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
        </button>
      </div>
    </div>
  </div>
</template>