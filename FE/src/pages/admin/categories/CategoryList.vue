<template>
  <div class="category-list">
    <div class="bg-white shadow">
      <div class="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
        <h1 class="text-3xl font-bold text-gray-900">Quản lý danh mục</h1>
        <button
          @click="showCreateModal = true"
          class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          <svg class="-ml-1 mr-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
            <path fill-rule="evenodd" d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" clip-rule="evenodd" />
          </svg>
          Thêm danh mục
        </button>
      </div>
    </div>

    <div class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <!-- Search and Filter -->
      <div class="bg-white shadow rounded-lg p-4 mb-6">
        <div class="flex flex-col md:flex-row md:items-center md:justify-between space-y-4 md:space-y-0 md:space-x-4">
          <div class="flex-1">
            <label for="search" class="sr-only">Tìm kiếm</label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <svg class="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
                </svg>
              </div>
              <input
                id="search"
                v-model="filters.search"
                type="text"
                placeholder="Tìm kiếm danh mục..."
                class="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              />
            </div>
          </div>
          <div class="flex items-center space-x-2">
            <select
              v-model="filters.status"
              class="block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
            >
              <option value="">Tất cả trạng thái</option>
              <option value="active">Đang hoạt động</option>
              <option value="inactive">Ngừng hoạt động</option>
            </select>
            <button
              @click="resetFilters"
              class="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              Đặt lại
            </button>
          </div>
        </div>
      </div>

      <!-- Categories Table -->
      <div class="bg-white shadow overflow-hidden sm:rounded-lg">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Tên danh mục
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Đường dẫn
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Số sản phẩm
                </th>
                <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Trạng thái
                </th>
                <th scope="col" class="relative px-6 py-3">
                  <span class="sr-only">Hành động</span>
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="category in filteredCategories" :key="category.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center">
                    <div v-if="category.image" class="flex-shrink-0 h-10 w-10">
                      <img class="h-10 w-10 rounded-full" :src="category.image" :alt="category.name" />
                    </div>
                    <div class="ml-4">
                      <div class="text-sm font-medium text-gray-900">{{ category.name }}</div>
                      <div class="text-sm text-gray-500">ID: {{ category.id }}</div>
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  /{{ category.slug || category.name.toLowerCase().replace(/\s+/g, '-') }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ category.product_count || 0 }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="getStatusClass(category.status)" class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full">
                    {{ getStatusText(category.status) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <button @click="editCategory(category)" class="text-indigo-600 hover:text-indigo-900 mr-4">Sửa</button>
                  <button @click="confirmDelete(category)" class="text-red-600 hover:text-red-900">Xóa</button>
                </td>
              </tr>
              <tr v-if="filteredCategories.length === 0">
                <td colspan="5" class="px-6 py-4 text-center text-sm text-gray-500">
                  Không tìm thấy danh mục nào
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
          <div class="flex-1 flex justify-between sm:hidden">
            <button
              @click="previousPage"
              :disabled="pagination.currentPage === 1"
              class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
            >
              Trước
            </button>
            <button
              @click="nextPage"
              :disabled="pagination.currentPage === pagination.totalPages"
              class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
            >
              Tiếp
            </button>
          </div>
          <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
            <div>
              <p class="text-sm text-gray-700">
                Hiển thị <span class="font-medium">{{ pagination.from }}</span> đến <span class="font-medium">{{ pagination.to }}</span> trong tổng số <span class="font-medium">{{ pagination.total }}</span> kết quả
              </p>
            </div>
            <div>
              <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                <button
                  @click="previousPage"
                  :disabled="pagination.currentPage === 1"
                  :class="pagination.currentPage === 1 ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-50'"
                  class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500"
                >
                  <span class="sr-only">Trước</span>
                  <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
                <template v-for="page in pagination.totalPages" :key="page">
                  <button
                    @click="goToPage(page)"
                    :class="pagination.currentPage === page ? 'z-10 bg-indigo-50 border-indigo-500 text-indigo-600' : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'"
                    class="relative inline-flex items-center px-4 py-2 border text-sm font-medium"
                  >
                    {{ page }}
                  </button>
                </template>
                <button
                  @click="nextPage"
                  :disabled="pagination.currentPage === pagination.totalPages"
                  :class="pagination.currentPage === pagination.totalPages ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-50'"
                  class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500"
                >
                  <span class="sr-only">Tiếp</span>
                  <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Create/Edit Category Modal -->
    <div v-if="showModal" class="fixed z-10 inset-0 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="closeModal"></div>

        <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

        <div class="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
          <div>
            <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
              <h3 class="text-lg leading-6 font-medium text-gray-900" id="modal-title">
                {{ isEditing ? 'Chỉnh sửa danh mục' : 'Thêm danh mục mới' }}
              </h3>
              <div class="mt-2">
                <form @submit.prevent="saveCategory">
                  <div class="space-y-4">
                    <div>
                      <label for="name" class="block text-sm font-medium text-gray-700">Tên danh mục <span class="text-red-500">*</span></label>
                      <input
                        type="text"
                        id="name"
                        v-model="formData.name"
                        class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                        required
                      />
                    </div>
                    <div>
                      <label for="slug" class="block text-sm font-medium text-gray-700">Đường dẫn</label>
                      <input
                        type="text"
                        id="slug"
                        v-model="formData.slug"
                        class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                        placeholder="tu-dong-tao-neu-de-trong"
                      />
                    </div>
                    <div>
                      <label for="description" class="block text-sm font-medium text-gray-700">Mô tả</label>
                      <textarea
                        id="description"
                        v-model="formData.description"
                        rows="3"
                        class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                      ></textarea>
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700">Hình ảnh</label>
                      <div class="mt-1 flex items-center">
                        <span class="inline-block h-12 w-12 rounded-full overflow-hidden bg-gray-100">
                          <img v-if="formData.image_preview" :src="formData.image_preview" class="h-full w-full object-cover">
                          <svg v-else class="h-full w-full text-gray-300" fill="currentColor" viewBox="0 0 24 24">
                            <path d="M24 20.993V24H0v-2.996A14.977 14.977 0 0112.004 15c4.904 0 9.26 2.354 11.996 5.993zM16.002 8.999a4 4 0 11-8 0 4 4 0 018 0z" />
                          </svg>
                        </span>
                        <button
                          type="button"
                          class="ml-5 bg-white py-2 px-3 border border-gray-300 rounded-md shadow-sm text-sm leading-4 font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        >
                          Chọn ảnh
                        </button>
                      </div>
                    </div>
                    <div>
                      <label class="block text-sm font-medium text-gray-700">Trạng thái</label>
                      <div class="mt-2 space-y-2">
                        <div class="flex items-center">
                          <input
                            id="status-active"
                            v-model="formData.status"
                            type="radio"
                            value="active"
                            class="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300"
                          />
                          <label for="status-active" class="ml-2 block text-sm text-gray-700">
                            Đang hoạt động
                          </label>
                        </div>
                        <div class="flex items-center">
                          <input
                            id="status-inactive"
                            v-model="formData.status"
                            type="radio"
                            value="inactive"
                            class="focus:ring-indigo-500 h-4 w-4 text-indigo-600 border-gray-300"
                          />
                          <label for="status-inactive" class="ml-2 block text-sm text-gray-700">
                            Ngừng hoạt động
                          </label>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="mt-5 sm:mt-6 sm:grid sm:grid-cols-2 sm:gap-3 sm:grid-flow-row-dense">
                    <button
                      type="submit"
                      class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:col-start-2 sm:text-sm"
                      :disabled="saving"
                    >
                      <svg v-if="saving" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                      </svg>
                      {{ saving ? 'Đang lưu...' : 'Lưu thay đổi' }}
                    </button>
                    <button
                      type="button"
                      @click="closeModal"
                      class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:col-start-1 sm:text-sm"
                      :disabled="saving"
                    >
                      Hủy
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="fixed z-10 inset-0 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="showDeleteModal = false"></div>

        <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

        <div class="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
          <div class="sm:flex sm:items-start">
            <div class="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
              <svg class="h-6 w-6 text-red-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
              </svg>
            </div>
            <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
              <h3 class="text-lg leading-6 font-medium text-gray-900" id="modal-title">
                Xác nhận xóa
              </h3>
              <div class="mt-2">
                <p class="text-sm text-gray-500">
                  Bạn có chắc chắn muốn xóa danh mục "{{ categoryToDelete?.name }}"? Hành động này không thể hoàn tác.
                </p>
              </div>
            </div>
          </div>
          <div class="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
            <button
              type="button"
              @click="deleteCategory"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-600 text-base font-medium text-white hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 sm:ml-3 sm:w-auto sm:text-sm"
              :disabled="deleting"
            >
              <svg v-if="deleting" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              {{ deleting ? 'Đang xóa...' : 'Xóa' }}
            </button>
            <button
              type="button"
              @click="showDeleteModal = false"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:w-auto sm:text-sm"
              :disabled="deleting"
            >
              Hủy
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, computed, onMounted } from 'vue';

export default defineComponent({
  name: 'CategoryList',
  setup() {
    // Sample data - replace with actual API calls
    const categories = ref([
      { id: 1, name: 'Áo thun', slug: 'ao-thun', description: 'Các loại áo thun', status: 'active', product_count: 42, image: 'https://via.placeholder.com/40' },
      { id: 2, name: 'Quần jean', slug: 'quan-jean', description: 'Các loại quần jean', status: 'active', product_count: 35, image: 'https://via.placeholder.com/40' },
      { id: 3, name: 'Áo khoác', slug: 'ao-khoac', description: 'Các loại áo khoác', status: 'active', product_count: 28, image: 'https://via.placeholder.com/40' },
      { id: 4, name: 'Giày dép', slug: 'giay-dep', description: 'Các loại giày dép', status: 'active', product_count: 56, image: 'https://via.placeholder.com/40' },
      { id: 5, name: 'Phụ kiện', slug: 'phu-kien', description: 'Các loại phụ kiện', status: 'inactive', product_count: 19, image: 'https://via.placeholder.com/40' },
    ]);

    // Filtering
    const filters = reactive({
      search: '',
      status: ''
    });

    // Pagination
    const pagination = reactive({
      currentPage: 1,
      perPage: 10,
      get total() {
        return filteredCategories.value.length;
      },
      get totalPages() {
        return Math.ceil(this.total / this.perPage);
      },
      get from() {
        return (this.currentPage - 1) * this.perPage + 1;
      },
      get to() {
        const to = this.currentPage * this.perPage;
        return to > this.total ? this.total : to;
      }
    });

    // Filtered and paginated categories
    const filteredCategories = computed(() => {
      return categories.value.filter(category => {
        const matchesSearch = !filters.search || 
          category.name.toLowerCase().includes(filters.search.toLowerCase()) ||
          (category.description && category.description.toLowerCase().includes(filters.search.toLowerCase()));
        
        const matchesStatus = !filters.status || category.status === filters.status;
        
        return matchesSearch && matchesStatus;
      });
    });

    const paginatedCategories = computed(() => {
      const start = (pagination.currentPage - 1) * pagination.perPage;
      const end = start + pagination.perPage;
      return filteredCategories.value.slice(start, end);
    });

    // Modal state
    const showModal = ref(false);
    const showDeleteModal = ref(false);
    const isEditing = ref(false);
    const saving = ref(false);
    const deleting = ref(false);
    const categoryToDelete = ref<any>(null);

    // Form data
    const formData = reactive({
      id: null as number | null,
      name: '',
      slug: '',
      description: '',
      status: 'active',
      image_preview: '' as string | null
    });

    // Reset form
    const resetForm = () => {
      formData.id = null;
      formData.name = '';
      formData.slug = '';
      formData.description = '';
      formData.status = 'active';
      formData.image_preview = null;
    };

    // Open create modal
    const openCreateModal = () => {
      resetForm();
      isEditing.value = false;
      showModal.value = true;
    };

    // Open edit modal
    const editCategory = (category: any) => {
      Object.assign(formData, {
        id: category.id,
        name: category.name,
        slug: category.slug,
        description: category.description || '',
        status: category.status,
        image_preview: category.image || null
      });
      isEditing.value = true;
      showModal.value = true;
    };

    // Close modal
    const closeModal = () => {
      showModal.value = false;
    };

    // Save category
    const saveCategory = async () => {
      saving.value = true;
      
      try {
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 1000));
        
        if (isEditing.value && formData.id) {
          // Update existing category
          const index = categories.value.findIndex(c => c.id === formData.id);
          if (index !== -1) {
            categories.value[index] = {
              ...categories.value[index],
              ...formData
            };
          }
        } else {
          // Add new category
          const newId = Math.max(0, ...categories.value.map(c => c.id)) + 1;
          categories.value.unshift({
            id: newId,
            name: formData.name,
            slug: formData.slug || formData.name.toLowerCase().replace(/\s+/g, '-'),
            description: formData.description,
            status: formData.status,
            product_count: 0,
            image: formData.image_preview || 'https://via.placeholder.com/40'
          });
        }
        
        showModal.value = false;
      } catch (error) {
        console.error('Error saving category:', error);
      } finally {
        saving.value = false;
      }
    };

    // Confirm delete
    const confirmDelete = (category: any) => {
      categoryToDelete.value = category;
      showDeleteModal.value = true;
    };

    // Delete category
    const deleteCategory = async () => {
      if (!categoryToDelete.value) return;
      
      deleting.value = true;
      
      try {
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 800));
        
        const index = categories.value.findIndex(c => c.id === categoryToDelete.value.id);
        if (index !== -1) {
          categories.value.splice(index, 1);
        }
        
        showDeleteModal.value = false;
      } catch (error) {
        console.error('Error deleting category:', error);
      } finally {
        deleting.value = false;
      }
    };

    // Pagination methods
    const goToPage = (page: number) => {
      if (page >= 1 && page <= pagination.totalPages) {
        pagination.currentPage = page;
      }
    };

    const previousPage = () => {
      if (pagination.currentPage > 1) {
        pagination.currentPage--;
      }
    };

    const nextPage = () => {
      if (pagination.currentPage < pagination.totalPages) {
        pagination.currentPage++;
      }
    };

    // Reset filters
    const resetFilters = () => {
      filters.search = '';
      filters.status = '';
      pagination.currentPage = 1;
    };

    // Helper functions
    const getStatusClass = (status: string) => {
      const statusMap: Record<string, string> = {
        'active': 'bg-green-100 text-green-800',
        'inactive': 'bg-yellow-100 text-yellow-800'
      };
      return statusMap[status] || 'bg-gray-100 text-gray-800';
    };

    const getStatusText = (status: string) => {
      const statusMap: Record<string, string> = {
        'active': 'Đang hoạt động',
        'inactive': 'Ngừng hoạt động'
      };
      return statusMap[status] || status;
    };

    // Watch for filter changes to reset to first page
    const watchFilters = () => {
      pagination.currentPage = 1;
    };

    // Watch for filter changes
    watchFilters();

    return {
      // Data
      categories: paginatedCategories,
      filters,
      pagination,
      showModal,
      showDeleteModal,
      isEditing,
      saving,
      deleting,
      categoryToDelete,
      formData,
      
      // Methods
      openCreateModal,
      editCategory,
      closeModal,
      saveCategory,
      confirmDelete,
      deleteCategory,
      goToPage,
      previousPage,
      nextPage,
      resetFilters,
      getStatusClass,
      getStatusText
    };
  }
});
</script>

<style scoped>
.category-list {
  min-height: calc(100vh - 64px);
}
</style>
