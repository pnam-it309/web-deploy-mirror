<template>
  <ModalCustom :show="true" @close="onClose">
    <template #title>
      {{ isEdit ? 'Chỉnh sửa danh mục' : 'Thêm danh mục mới' }}
    </template>

    <div
      v-if="validationError"
      class="bg-red-100 text-red-700 p-3 rounded mb-4 text-sm"
    >
      {{ validationError }}
    </div>
    
    <div class="space-y-4">
      <InputCustom
        v-model.trim="form.name"
        label="Tên danh mục"
        required
        :disabled="loading"
      />
      
      <div>
        <!-- SỬA: Áp dụng "Template Chuẩn" (Tự tạo Slug) -->
        <InputCustom
          v-model.trim="form.slug"
          label="Slug (Tự động tạo)"
          :disabled="true"
          class="bg-gray-100"
        />
      </div>

      <TextareaCustom v-model.trim="form.description" label="Mô tả" :disabled="loading" />

      <div>
        <SelectCustom v-model="form.parentId" label="Danh mục cha" :disabled="loading">
          <option :value="null">— Không có —</option>
          <option
            v-for="opt in flattenedCategories"
            :key="opt.id"
            :value="opt.id"
            :disabled="isOptionDisabled(opt.id)"
          >
            <span v-html="indent(opt.level)"></span>{{ opt.name }}
          </option>
        </SelectCustom>
        <AlertCustom
          type="error"
          :show="!!parentError"
          @close="parentError = null"
          class="mt-2"
        >
          {{ parentError }}
        </AlertCustom>
      </div>
    </div>

    <template #footer>
      <ButtonCustom color="secondary" @click="onClose" :disabled="loading"> Hủy </ButtonCustom>
      <ButtonCustom color="primary" @click="onSave" :loading="loading" :disabled="loading">
        {{ isEdit ? 'Lưu thay đổi' : 'Thêm mới' }}
      </ButtonCustom>
    </template>
  </ModalCustom>
</template>

<script setup lang="ts">
import { reactive, computed, watch, ref } from 'vue'
import { toSlug } from '@/utils/slug'; // <-- THÊM IMPORT
import { useCategoryStore } from '@/stores/category.store'; // <-- DÙNG STORE
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import InputCustom from '@/components/custom/Input/InputCustom.vue'
import SelectCustom from '@/components/custom/Select/SelectCustom.vue'
import TextareaCustom from '@/components/custom/TextArea/TextAreaCustom.vue'
import AlertCustom from '@/components/custom/Alert/AlertCustom.vue'

// (Sửa: Bỏ 'categories' khỏi props)
const props = defineProps({
  editItem: { type: Object, default: null },
  loading: Boolean,
})
const emit = defineEmits(['close', 'saved'])

// LẤY CATEGORIES TỪ STORE
const categoryStore = useCategoryStore();

/** Reactive form (Sửa: Dùng string | null cho ID) */
const form = reactive({
  id: undefined as string | undefined,
  name: '',
  slug: '',
  description: '',
  parentId: null as string | null
})

/** Errors */
const validationError = ref<string | null>(null) // (Sửa: Đổi tên slugError -> validationError)
const parentError = ref<string | null>(null)

/** isEdit */
const isEdit = computed(() => !!props.editItem)

/** When editItem changes, populate form (Sửa: Dùng string | null) */
watch(
  () => props.editItem,
  (val) => {
    if (val) {
      form.id = val.id
      form.name = val.name || ''
      form.slug = val.slug || ''
      form.description = val.description || ''
      form.parentId = val.parentId ?? null
    } else {
      // reset
      form.id = undefined
      form.name = ''
      form.slug = ''
      form.description = ''
      form.parentId = null
    }
    validationError.value = null
    parentError.value = null
  },
  { immediate: true }
)

// THÊM: Watch (theo dõi) "name" để "Tự tạo Slug"
watch(
  () => form.name,
  (newName) => {
    if (!props.editItem) { // Chỉ tự tạo khi 'Tạo mới'
      form.slug = toSlug(newName); 
    }
  }
);

/** Build flattened tree (Sửa: Dùng categoryStore.categories và ID string) */
const flattenedCategories = computed(() => {
  const map = new Map<string | null, any[]>()
  for (const c of categoryStore.categories) { // <-- DÙNG STORE
    const pid = (c as any).parentId ?? null
    if (!map.has(pid)) map.set(pid, [])
    map.get(pid)!.push(c)
  }
  for (const [k, arr] of map.entries()) {
    arr.sort((a, b) => a.name.localeCompare(b.name))
  }

  const out: { id: string; name: string; level: number }[] = [] // <-- ID LÀ STRING
  const dfs = (nodes: any[], level = 0) => {
    if (!nodes) return
    for (const n of nodes) {
      out.push({ id: n.id, name: n.name, level })
      const children = map.get(n.id) || []
      dfs(children, level + 1)
    }
  }

  const roots = map.get(null) || []
  dfs(roots, 0)
  return out
})

/** Helper: build parent chain (Sửa: Dùng ID string) */
const climbsTo = (startId: string | null, findId: string) => { // <-- ID LÀ STRING
  let cur = startId
  while (cur !== null && cur !== undefined) {
    if (cur === findId) return true
    const p = categoryStore.categories.find((c: any) => c.id === cur) // <-- DÙNG STORE
    cur = p ? ((p as any).parentId ?? null) : null
  }
  return false
}

/** Disable options (Sửa: Dùng ID string) */
const isOptionDisabled = (optionId: string) => { // <-- ID LÀ STRING
  if (!isEdit.value) return false
  const currentId = props.editItem?.id
  if (!currentId) return false
  if (optionId === currentId) return true
  if (climbsTo(optionId, currentId)) return true
  return false
}

/** Indentation helper */
const indent = (level: number) => {
  if (!level) return ''
  return '&nbsp;'.repeat(level * 4)
}

/** Validation (Sửa: Bỏ validate Slug) */
const validate = () => {
  validationError.value = null
  parentError.value = null

  if (!form.name) {
    validationError.value = 'Tên danh mục là bắt buộc.';
    return false
  }

  // (Xoá logic validate Slug)

  // parent validation (Giữ nguyên)
  if (form.parentId != null && form.id != null) {
    if (form.parentId === form.id) {
      parentError.value = 'Không thể chọn chính danh mục làm cha.'
      return false
    }
    if (climbsTo(form.parentId, form.id)) {
      parentError.value = 'Không thể chọn một danh mục con làm cha (vòng lặp).'
      return false
    }
  }

  return true
}

/** Save handler (Sửa: Bỏ 'slug' khỏi payload) */
const onSave = () => {
  if (!validate()) return

  const payload: any = {
    id: form.id,
    name: form.name.trim(),
    // (Xoá 'slug', vì BE tự tạo)
    description: form.description?.trim() || null,
    parentId: form.parentId ?? null
  }
  emit('saved', payload)
}

/** Close handler */
const onClose = () => {
  emit('close')
}
</script>