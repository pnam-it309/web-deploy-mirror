<template>
  <div class="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
    <div class="bg-white w-[600px] rounded-lg shadow-lg p-6 relative">
      <h2 class="text-xl font-semibold mb-4">
        {{ editItem ? "Chỉnh sửa thương hiệu" : "Thêm thương hiệu mới" }}
      </h2>

      <form @submit.prevent="save">
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium mb-1">Tên thương hiệu</label>
            <input
              v-model="form.name"
              type="text"
              class="border rounded-lg w-full p-2"
              required
            />
          </div>

          <div>
            <label class="block text-sm font-medium mb-1">Mã code</label>
            <input
              v-model="form.code"
              type="text"
              class="border rounded-lg w-full p-2"
              required
            />
          </div>

          <div class="col-span-2">
            <label class="block text-sm font-medium mb-1">Slug</label>
            <input
              v-model="form.slug"
              type="text"
              class="border rounded-lg w-full p-2"
              required
            />
          </div>

          <div class="col-span-2">
            <label class="block text-sm font-medium mb-1">Mô tả</label>
            <textarea
              v-model="form.description"
              rows="3"
              class="border rounded-lg w-full p-2"
              placeholder="Giới thiệu ngắn về thương hiệu"
            ></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium mb-1">Trạng thái</label>
            <select v-model="form.status" class="border rounded-lg w-full p-2">
              <option value="ACTIVE">ACTIVE</option>
              <option value="INACTIVE">INACTIVE</option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium mb-1">Logo URL</label>
            <input
              v-model="form.logoUrl"
              type="url"
              class="border rounded-lg w-full p-2"
              placeholder="https://..."
            />
          </div>
        </div>

        <div class="flex justify-end space-x-2 mt-6">
          <button
            type="button"
            @click="$emit('close')"
            class="px-4 py-2 bg-gray-300 hover:bg-gray-400 rounded-lg"
          >
            Huỷ
          </button>
          <button
            type="submit"
            class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg"
          >
            Lưu
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from "vue";

const props = defineProps({
  editItem: Object,
});
const emit = defineEmits(["close", "save"]);

const form = ref({
  id: null,
  name: "",
  code: "",
  slug: "",
  description: "",
  status: "ACTIVE",
  logoUrl: "",
});

watch(
  () => props.editItem,
  (newVal) => {
    form.value = newVal
      ? { ...newVal }
      : {
          id: null,
          name: "",
          code: "",
          slug: "",
          description: "",
          status: "ACTIVE",
          logoUrl: "",
        };
  },
  { immediate: true }
);

const save = () => {
  emit("save", { ...form.value });
};
</script>
