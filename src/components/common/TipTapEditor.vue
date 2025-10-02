<template>
  <div class="editor">
    <MenuBar :editor="editor" class="editor__menu" v-if="editor">
      <button :class="{ 'is-active': editor.isActive('bold') }" @click="editor.chain().focus().toggleBold().run()">
        <span class="icon">B</span>
      </button>
      <button :class="{ 'is-active': editor.isActive('italic') }" @click="editor.chain().focus().toggleItalic().run()">
        <span class="icon"><i>I</i></span>
      </button>
      <button :class="{ 'is-active': editor.isActive('heading', { level: 1 }) }"
        @click="editor.chain().focus().toggleHeading({ level: 1 }).run()">
        H1
      </button>
      <button :class="{ 'is-active': editor.isActive('heading', { level: 2 }) }"
        @click="editor.chain().focus().toggleHeading({ level: 2 }).run()">
        H2
      </button>
      <button :class="{ 'is-active': editor.isActive('bulletList') }"
        @click="editor.chain().focus().toggleBulletList().run()">
        • List
      </button>
      <button :class="{ 'is-active': editor.isActive('orderedList') }"
        @click="editor.chain().focus().toggleOrderedList().run()">
        1. List
      </button>
    </MenuBar>

    <editor-content :editor="editor" class="editor__content" />
  </div>
</template>

<script setup lang="ts">
import { useEditor, EditorContent, Editor } from '@tiptap/vue-3';
import StarterKit from '@tiptap/starter-kit';
import { onBeforeUnmount, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['update:modelValue']);

const editor = useEditor({
  content: props.modelValue,
  extensions: [
    StarterKit,
  ],
  onUpdate: () => {
    emit('update:modelValue', editor.value?.getHTML() || '');
  },
});

// Update editor content when modelValue changes from parent
watch(() => props.modelValue, (value) => {
  const isSame = editor.value?.getHTML() === value;
  if (!isSame) {
    editor.value?.commands.setContent(value, false);
  }
});

onBeforeUnmount(() => {
  editor.value?.destroy();
});
</script>

<style scoped>
.editor {
  border: 1px solid #ccc;
  border-radius: 4px;
  overflow: hidden;
}

.editor__menu {
  display: flex;
  align-items: center;
  background: #f5f5f5;
  padding: 0.5rem;
  border-bottom: 1px solid #ddd;
  gap: 0.5rem;
}

.editor__menu button {
  background: white;
  border: 1px solid #ddd;
  border-radius: 3px;
  padding: 0.25rem 0.5rem;
  cursor: pointer;
  font-size: 14px;
}

.editor__menu button:hover {
  background: #f0f0f0;
}

.editor__menu button.is-active {
  background: #e0e0e0;
}

.editor__content {
  padding: 1rem;
  min-height: 200px;
  outline: none;
}

/* Basic editor styles */
:deep(.ProseMirror) {
  > * + * {
    margin-top: 0.75em;
  }
  
  ul, ol {
    padding: 0 1rem;
  }
  
  h1 {
    font-size: 2em;
  }
  
  h2 {
    font-size: 1.5em;
  }
  
  code {
    background-color: rgba(97, 97, 97, 0.1);
    color: #616161;
  }
  
  pre {
    background: #0d0d0d;
    color: #fff;
    font-family: 'JetBrainsMono', monospace;
    padding: 0.75rem 1rem;
    border-radius: 0.5rem;
  }
  
  pre code {
    color: inherit;
    padding: 0;
    background: none;
    font-size: 0.8rem;
  }
}
</style>
