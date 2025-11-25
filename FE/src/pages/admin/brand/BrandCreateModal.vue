<template>
  <ModalCustom :show="true" @close="$emit('close')">
    <template #title>
      {{ editItem ? 'Chỉnh sửa thương hiệu' : 'Thêm thương hiệu mới' }}
    </template>

    <template #default>
      <div
        v-if="validationError"
        class="bg-red-100 text-red-700 p-3 rounded mb-4 text-sm"
      >
        {{ validationError }}
      </div>
      
      <div class="grid grid-cols-2 gap-4">
        <InputCustom
          v-model="form.name"
          label="Tên thương hiệu"
          required
          :disabled="loading"
        />

        <InputCustom
          v-model="form.code"
          label="Mã code"
          required
          :disabled="loading || !!editItem"
        />
        <div class="col-span-2">
          <InputCustom
            v-model="form.slug"
            label="Slug"
            required
            :disabled="true" 
            class="bg-gray-100" 
          />
        </div>
        
        <div class="col-span-2">
          <TextareaCustom
            v-model="form.description"
            label="Mô tả"
            placeholder="Giới thiệu ngắn về thương hiệu"
            :disabled="loading"
          />
        </div>

        <SelectCustom v-model="form.status" label="Trạng thái" :disabled="loading">
          <option value="ACTIVE">ACTIVE</option>
          <option value="INACTIVE">INACTIVE</option>
        </SelectCustom>

        <InputCustom
          v-model="form.logoUrl"
          label="Logo URL"
          type="url"
          placeholder="https://..."
          :disabled="loading"
        />
      </div>
    </template>

    <template #footer>
      <ButtonCustom color="secondary" @click="$emit('close')" :disabled="loading">
        Huỷ
      </ButtonCustom>
      <ButtonCustom
        color="primary"
        @click="save"
        :loading="loading"
        :disabled="loading"
      >
        Lưu
      </ButtonCustom>
    </template>
  </ModalCustom>
</template>

<script setup>
// 3. THÊM IMPORT "toSlug" VÀ "watch"
import { ref, watch, defineProps, defineEmits } from 'vue';
import { toSlug } from '@/utils/slug'; // <-- THÊM DÒNG NÀY

import ModalCustom from '@/components/custom/Modal/ModalCustom.vue';
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue';
import InputCustom from '@/components/custom/Input/InputCustom.vue';
import SelectCustom from '@/components/custom/Select/SelectCustom.vue';
import TextareaCustom from '@/components/custom/TextArea/TextAreaCustom.vue';

const props = defineProps({
  editItem: Object,
  loading: Boolean,
});

const emit = defineEmits(['close', 'save']);

const form = ref({
  id: null,
  name: '',
  code: '',
  slug: '',
  description: '',
  status: 'ACTIVE',
  logoUrl: '',
});

const validationError = ref(null);

watch(
  () => props.editItem,
  (newVal) => {
    validationError.value = null;
    form.value = newVal
      ? { ...newVal }
      : {
          id: null,
          name: '',
          code: '',
          slug: '',
          description: '',
          status: 'ACTIVE',
          logoUrl: '',
        };
  },
  { immediate: true }
);
watch(
  () => form.value.name,
  (newName) => {
    if (!props.editItem) {
      form.value.slug = toSlug(newName);
    }
  }
);


const save = () => {
  validationError.value = null; 
  if (!form.value.name || !form.value.code) {
    validationError.value = 'Vui lòng điền đầy đủ Tên và Code.';
    return;
  }
  emit('save', { ...form.value });
};
</script>