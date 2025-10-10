<template>
  <div class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
    <div class="bg-white rounded-lg shadow-lg w-full max-w-lg text-gray-900">
      <!-- Header -->
      <div class="flex justify-between items-center px-6 py-4 border-b">
        <h3 class="text-lg font-semibold">
          {{ isEdit ? 'Chỉnh sửa danh mục' : 'Thêm danh mục mới' }}
        </h3>
        <button @click="onClose" class="text-gray-500 hover:text-gray-700 text-xl">×</button>
      </div>

      <!-- Body -->
      <div class="px-6 py-4 space-y-4">
        <div>
          <label class="block text-sm font-medium text-gray-700">Tên danh mục</label>
          <input v-model.trim="form.name" type="text" class="input" />
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Slug</label>
          <input v-model.trim="form.slug" type="text" class="input" />
          <p v-if="slugError" class="text-xs text-red-600 mt-1">{{ slugError }}</p>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Mô tả</label>
          <textarea v-model.trim="form.description" class="input h-24"></textarea>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Danh mục cha</label>
          <select v-model="form.parentId" class="input">
            <option :value="null">— Không có —</option>
            <option
              v-for="opt in flattened"
              :key="opt.id"
              :value="opt.id"
              :disabled="isOptionDisabled(opt.id)"
            >
              <span v-html="indent(opt.level)"></span>{{ opt.name }}
            </option>
          </select>
          <p v-if="parentError" class="text-xs text-red-600 mt-1">{{ parentError }}</p>
        </div>
      </div>

      <!-- Footer -->
      <div class="flex justify-end space-x-3 px-6 py-4 border-t">
        <button @click="onClose" class="px-4 py-2 bg-gray-100 hover:bg-gray-200 rounded text-gray-700">
          Hủy
        </button>
        <button @click="onSave" class="px-4 py-2 bg-pink-600 hover:bg-pink-700 text-white rounded">
          {{ isEdit ? 'Lưu thay đổi' : 'Thêm mới' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, computed, watch, ref } from 'vue'

/**
 * Props:
 * - categories: danh sách hiện tại (mảng), mỗi item { id, name, slug, description, parentId }
 * - editItem: nếu sửa, object truyền vào (copy)
 *
 * Emits:
 * - close
 * - saved (payload minimal: { id?, name, slug, description, parentId })
 */

const props = defineProps({
  categories: { type: Array, default: () => [] },
  editItem: { type: Object, default: null },
})
const emit = defineEmits(['close', 'saved'])

/** Reactive form */
const form = reactive({
  id: undefined as number | undefined,
  name: '',
  slug: '',
  description: '',
  parentId: null as number | null,
})

/** Errors */
const slugError = ref<string | null>(null)
const parentError = ref<string | null>(null)

/** isEdit */
const isEdit = computed(() => !!props.editItem)

/** When editItem changes, populate form */
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
    // reset errors
    slugError.value = null
    parentError.value = null
  },
  { immediate: true }
)

/** Build flattened tree for select (root-first) with level for indentation */
const flattened = computed(() => {
  const map = new Map<number | null, any[]>()
  for (const c of props.categories) {
    const pid = c.parentId ?? null
    if (!map.has(pid)) map.set(pid, [])
    map.get(pid)!.push(c)
  }
  // sort children by name optionally
  for (const [k, arr] of map.entries()) {
    arr.sort((a, b) => a.name.localeCompare(b.name))
  }

  const out: { id: number; name: string; level: number }[] = []
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

/** Helper: build parent chain: climb from candidate up to root */
const climbsTo = (startId: number | null, findId: number) => {
  let cur = startId
  while (cur !== null && cur !== undefined) {
    if (cur === findId) return true
    const p = props.categories.find((c: any) => c.id === cur)
    cur = p ? (p.parentId ?? null) : null
  }
  return false
}

/** If current editing id exists, we must disable options:
 *  - the category itself
 *  - any descendant of the category (so we avoid cycles)
 */
const isOptionDisabled = (optionId: number) => {
  if (!isEdit.value) return false
  const currentId = props.editItem?.id
  if (!currentId) return false
  if (optionId === currentId) return true
  // if option is a descendant of current (i.e., climbing from option reaches current), disable
  if (climbsTo(optionId, currentId)) return true
  return false
}

/** Indentation helper for option label (non-breaking spaces) */
const indent = (level: number) => {
  if (!level) return ''
  return '&nbsp;'.repeat(level * 4)
}

/** Validation */
const validate = () => {
  slugError.value = null
  parentError.value = null

  if (!form.name || !form.slug) {
    slugError.value = !form.slug ? 'Slug bắt buộc' : null
    return false
  }

  // slug unique check: compare against existing categories except current id (if editing)
  const exists = props.categories.some((c: any) => c.slug === form.slug && c.id !== form.id)
  if (exists) {
    slugError.value = 'Slug đã tồn tại. Vui lòng chọn slug khác.'
    return false
  }

  // parent validation: cannot select self or descendant (extra safety)
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

/** Save handler */
const onSave = () => {
  if (!validate()) return

  // build payload: if editing include id; if creating id undefined (parent will assign)
  const payload: any = {
    id: form.id,
    name: form.name.trim(),
    slug: form.slug.trim(),
    description: form.description?.trim() || null,
    parentId: form.parentId ?? null,
  }
  emit('saved', payload)
  // do not auto-close here; parent will close modal on saved; but also emit close for safety
  // emit('close')
}

/** Close handler */
const onClose = () => {
  emit('close')
}
</script>

<style scoped>
.input {
  @apply mt-1 block w-full rounded-md border border-gray-300 shadow-sm
  focus:ring-pink-500 focus:border-pink-500 sm:text-sm text-gray-900;
}
select.input {
  height: 2.5rem;
}
</style>
