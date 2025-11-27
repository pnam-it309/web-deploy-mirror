<template>
  <ModalCustom :show="true" @close="onClose">
    <template #title>
      {{ isEdit ? 'Chỉnh sửa danh mục' : 'Thêm danh mục mới' }}
    </template>

    <div class="space-y-4">
      <InputCustom
        v-model.trim="form.name"
        label="Tên danh mục"
        required
        :disabled="loading"
      />
      
      <div>
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
            {{ formatOption(opt.name, opt.level) }}
          </option>
        </SelectCustom>
        
        <div class="mt-2">
          <AlertCustom
            v-if="parentError"
            type="error"
            :show="true"
            @close="parentError = null"
          >
            {{ parentError }}
          </AlertCustom>
        </div>
      </div>
    </div>

    <div class="mt-4">
      <AlertCustom
        v-if="validationError"
        type="error"
        :show="true"
        @close="validationError = null"
      >
        {{ validationError }}
      </AlertCustom>
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
import { toSlug } from '@/utils/slug';
import { useCategoryStore } from '@/stores/category.store';
import ModalCustom from '@/components/custom/Modal/ModalCustom.vue'
import ButtonCustom from '@/components/custom/Button/ButtonDefault.vue'
import InputCustom from '@/components/custom/Input/InputCustom.vue'
import SelectCustom from '@/components/custom/Select/SelectCustom.vue'
import TextareaCustom from '@/components/custom/TextArea/TextAreaCustom.vue'
import AlertCustom from '@/components/custom/Alert/AlertCustom.vue'

const props = defineProps({
  editItem: { type: Object, default: null },
  loading: Boolean,
})
const emit = defineEmits(['close', 'saved'])

const categoryStore = useCategoryStore();

const form = reactive({
  id: undefined as string | undefined,
  name: '',
  slug: '',
  description: '',
  parentId: null as string | null
})

const validationError = ref<string | null>(null)
const parentError = ref<string | null>(null)

const isEdit = computed(() => !!props.editItem)

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

watch(
  () => form.name,
  (newName) => {
    if (!props.editItem) {
      form.slug = toSlug(newName); 
    }
  }
);

const flattenedCategories = computed(() => {
  const map = new Map<string | null, any[]>()
  for (const c of categoryStore.categories) {
    const pid = (c as any).parentId ?? null
    if (!map.has(pid)) map.set(pid, [])
    map.get(pid)!.push(c)
  }
  for (const [k, arr] of map.entries()) {
    arr.sort((a, b) => a.name.localeCompare(b.name))
  }

  const out: { id: string; name: string; level: number }[] = []
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

const climbsTo = (startId: string | null, findId: string) => {
  let cur = startId
  while (cur !== null && cur !== undefined) {
    if (cur === findId) return true
    const p = categoryStore.categories.find((c: any) => c.id === cur)
    cur = p ? ((p as any).parentId ?? null) : null
  }
  return false
}

const isOptionDisabled = (optionId: string) => {
  if (!isEdit.value) return false
  const currentId = props.editItem?.id
  if (!currentId) return false
  if (optionId === currentId) return true
  if (climbsTo(optionId, currentId)) return true
  return false
}

const formatOption = (name: string, level: number) => {
  const spaces = '\u00A0\u00A0\u00A0\u00A0'.repeat(level);
  return `${spaces}${name}`;
}

const validate = () => {
  validationError.value = null
  parentError.value = null

  if (!form.name) {
    validationError.value = 'Tên danh mục là bắt buộc.';
    return false
  }

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

const onSave = () => {
  if (!validate()) return

  const payload: any = {
    id: form.id,
    name: form.name.trim(),
    description: form.description?.trim() || null,
    parentId: form.parentId ?? null
  }
  emit('saved', payload)
}

const onClose = () => {
  emit('close')
}
</script>