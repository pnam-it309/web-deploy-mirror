<template>
  <div :class="containerClass">
    <label v-if="label" :for="id" class="block text-sm font-medium text-gray-700 mb-1">
      {{ label }}
    </label>
    <div class="relative rounded-md shadow-sm">
      <div
        v-if="prependIcon || $slots.prepend"
        class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3"
      >
        <slot name="prepend">
          <i :class="[prependIcon, 'text-gray-400']" aria-hidden="true"></i>
        </slot>
      </div>
      <input
        :id="id"
        :type="type"
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="disabled"
        class="block w-full rounded-lg border-0 py-2.5 text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-[#1e293b] sm:text-sm sm:leading-6 transition-shadow"
        :class="[
          prependIcon || $slots.prepend ? 'pl-10' : 'pl-3',
          appendIcon || $slots.append ? 'pr-10' : 'pr-3',
          error ? 'ring-red-300 focus:ring-red-500 text-red-900 placeholder:text-red-300' : ''
        ]"
        @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
        @blur="$emit('blur', $event)"
        @focus="$emit('focus', $event)"
      />
      <div
        v-if="appendIcon || $slots.append"
        class="absolute inset-y-0 right-0 flex items-center pr-3"
      >
        <slot name="append">
          <i :class="[appendIcon, 'text-gray-400 cursor-pointer']" @click="$emit('append-click')"></i>
        </slot>
      </div>
    </div>
    <p v-if="error" class="mt-1 text-sm text-red-600" :id="`${id}-error`">
      {{ error }}
    </p>
    <p v-else-if="hint" class="mt-1 text-sm text-gray-500" :id="`${id}-hint`">
      {{ hint }}
    </p>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps({
  modelValue: {
    type: [String, Number],
    default: '',
  },
  label: {
    type: String,
    default: '',
  },
  id: {
    type: String,
    default: () => `input-${Math.random().toString(36).substring(2, 9)}`,
  },
  type: {
    type: String,
    default: 'text',
  },
  placeholder: {
    type: String,
    default: '',
  },
  prependIcon: {
    type: String,
    default: '',
  },
  appendIcon: {
    type: String,
    default: '',
  },
  error: {
    type: String,
    default: '',
  },
  hint: {
    type: String,
    default: '',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  containerClass: {
    type: String,
    default: '',
  }
});

defineEmits(['update:modelValue', 'blur', 'focus', 'append-click']);
</script>
