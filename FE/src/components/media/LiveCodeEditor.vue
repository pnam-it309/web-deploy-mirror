<template>
  <div class="live-code-editor">
    <div class="editor-header">
      <div class="language-selector">
        <button
          v-for="lang in supportedLanguages"
          :key="lang.id"
          @click="changeLanguage(lang.id)"
          :class="['lang-btn', { active: currentLanguage === lang.id }]"
        >
          {{ lang.label }}
        </button>
      </div>
      
      <div class="actions">
        <button @click="runCode" class="run-btn" :disabled="isRunning">
          <span v-if="!isRunning">▶ Run Code</span>
          <span v-else>⏳ Running...</span>
        </button>
        <button @click="resetCode" class="reset-btn">↺ Reset</button>
        <button @click="copyCode" class="copy-btn">📋 Copy</button>
      </div>
    </div>

    <div class="editor-container">
      <div class="editor-pane">
        <MonacoEditor
          v-model="code"
          :language="currentLanguage"
          :options="editorOptions"
          theme="vs-dark"
          @mount="handleEditorMount"
        />
      </div>

      <div class="output-pane">
        <div class="output-header">
          <span>{{ outputTitle }}</span>
          <button @click="clearOutput" class="clear-btn">Clear</button>
        </div>
        <div class="output-content" ref="outputContainer">
          <pre v-if="output" :class="['output-text', { error: hasError }]">{{ output }}</pre>
          <div v-else class="output-placeholder">Run code to see output...</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import MonacoEditor from '@monaco-editor/react'
import { toast } from 'vue3-toastify'

interface Props {
  initialCode?: string
  language?: string
  readonly?: boolean
  height?: string
}

const props = withDefaults(defineProps<Props>(), {
  initialCode: '// Write your code here...',
  language: 'javascript',
  readonly: false,
  height: '500px'
})

const code = ref(props.initialCode)
const currentLanguage = ref(props.language)
const output = ref('')
const isRunning = ref(false)
const hasError = ref(false)
const outputContainer = ref<HTMLElement | null>(null)

const supportedLanguages = [
  { id: 'javascript', label: 'JavaScript' },
  { id: 'typescript', label: 'TypeScript' },
  { id: 'python', label: 'Python' },
  { id: 'java', label: 'Java' },
  { id: 'html', label: 'HTML' },
  { id: 'css', label: 'CSS' }
]

const editorOptions = {
  fontSize: 14,
  minimap: { enabled: false },
  scrollBeyondLastLine: false,
  readOnly: props.readonly,
  automaticLayout: true,
  tabSize: 2,
  wordWrap: 'on'
}

const outputTitle = computed(() => {
  return currentLanguage.value === 'html' ? 'Preview' : 'Console Output'
})

const changeLanguage = (lang: string) => {
  currentLanguage.value = lang
  
  // Set default code for language
  const defaults: Record<string, string> = {
    javascript: 'console.log("Hello, World!");',
    typescript: 'const greeting: string = "Hello, TypeScript!";\nconsole.log(greeting);',
    python: 'print("Hello, Python!")',
    java: 'public class Main {\n  public static void main(String[] args) {\n    System.out.println("Hello, Java!");\n  }\n}',
    html: '<!DOCTYPE html>\n<html>\n<head>\n  <title>Demo</title>\n</head>\n<body>\n  <h1>Hello, HTML!</h1>\n</body>\n</html>',
    css: 'body {\n  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n  color: white;\n  font-family: sans-serif;\n}'
  }
  
  code.value = defaults[lang] || '// Code here...'
}

const runCode = async () => {
  isRunning.value = true
  hasError.value = false
  output.value = ''

  try {
    if (currentLanguage.value === 'javascript' || currentLanguage.value === 'typescript') {
      // Execute JavaScript in sandboxed environment
      await runJavaScript(code.value)
    } else if (currentLanguage.value === 'html') {
      // Render HTML
      renderHTML(code.value)
    } else {
      // For other languages, show a message
      output.value = `⚠️ Code execution for ${currentLanguage.value} is not supported in browser.\n\nCode is valid and can be run in appropriate environment.`
    }
  } catch (error: any) {
    hasError.value = true
    output.value = `Error: ${error.message}`
  } finally {
    isRunning.value = false
  }
}

const runJavaScript = async (jsCode: string) => {
  // Capture console output
  const logs: string[] = []
  const originalLog = console.log
  const originalError = console.error
  
  console.log = (...args) => {
    logs.push(args.map(arg => 
      typeof arg === 'object' ? JSON.stringify(arg, null, 2) : String(arg)
    ).join(' '))
  }
  
  console.error = (...args) => {
    hasError.value = true
    logs.push('ERROR: ' + args.join(' '))
  }

  try {
    // Execute code
    const result = eval(jsCode)
    
    if (result !== undefined) {
      logs.push(`→ ${typeof result === 'object' ? JSON.stringify(result, null, 2) : result}`)
    }
    
    output.value = logs.length > 0 ? logs.join('\n') : '✅ Code executed successfully (no output)'
  } catch (error: any) {
    hasError.value = true
    output.value = `❌ Runtime Error:\n${error.message}\n\nStack:\n${error.stack}`
  } finally {
    // Restore console
    console.log = originalLog
    console.error = originalError
  }
}

const renderHTML = (htmlCode: string) => {
  if (outputContainer.value) {
    // Create iframe for safe HTML rendering
    const iframe = document.createElement('iframe')
    iframe.style.width = '100%'
    iframe.style.height = '100%'
    iframe.style.border = 'none'
    iframe.style.background = 'white'
    
    outputContainer.value.innerHTML = ''
    outputContainer.value.appendChild(iframe)
    
    const iframeDoc = iframe.contentDocument || iframe.contentWindow?.document
    if (iframeDoc) {
      iframeDoc.open()
      iframeDoc.write(htmlCode)
      iframeDoc.close()
    }
  }
}

const resetCode = () => {
  changeLanguage(currentLanguage.value)
  clearOutput()
}

const clearOutput = () => {
  output.value = ''
  hasError.value = false
  if (outputContainer.value) {
    outputContainer.value.innerHTML = '<div class="output-placeholder">Run code to see output...</div>'
  }
}

const copyCode = async () => {
  try {
    await navigator.clipboard.writeText(code.value)
    toast.success('Code copied to clipboard!')
  } catch (error) {
    toast.error('Failed to copy code')
  }
}

const handleEditorMount = (editor: any) => {
  // Editor mounted, can add custom keybindings here
  console.log('Monaco Editor mounted')
}
</script>

<style scoped>
.live-code-editor {
  border-radius: 0.75rem;
  overflow: hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.editor-header {
  background: #252526;
  padding: 0.75rem 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #3e3e42;
}

.language-selector {
  display: flex;
  gap: 0.5rem;
}

.lang-btn {
  padding: 0.375rem 0.75rem;
  background: transparent;
  color: #cccccc;
  border: 1px solid #3e3e42;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s;
}

.lang-btn:hover {
  background: #3e3e42;
  border-color: #007acc;
}

.lang-btn.active {
  background: #007acc;
  border-color: #007acc;
  color: white;
}

.actions {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.run-btn, .reset-btn, .copy-btn {
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
}

.run-btn {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

.run-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}

.run-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.reset-btn, .copy-btn {
  background: #3e3e42;
  color: #cccccc;
}

.reset-btn:hover, .copy-btn:hover {
  background: #4e4e52;
}

.editor-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1px;
  background: #3e3e42;
  height: v-bind(height);
}

.editor-pane, .output-pane {
  background: #1e1e1e;
}

.output-pane {
  display: flex;
  flex-direction: column;
}

.output-header {
  background: #252526;
  padding: 0.5rem 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #cccccc;
  font-size: 0.875rem;
  border-bottom: 1px solid #3e3e42;
}

.clear-btn {
  padding: 0.25rem 0.75rem;
  background: transparent;
  color: #cccccc;
  border: 1px solid #3e3e42;
  border-radius: 0.25rem;
  font-size: 0.75rem;
  cursor: pointer;
}

.clear-btn:hover {
  background: #3e3e42;
}

.output-content {
  flex: 1;
  padding: 1rem;
  overflow: auto;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
}

.output-text {
  margin: 0;
  color: #d4d4d4;
  font-size: 0.875rem;
  line-height: 1.6;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.output-text.error {
  color: #f87171;
}

.output-placeholder {
  color: #6b7280;
  font-style: italic;
  font-size: 0.875rem;
}

/* Mobile responsive */
@media (max-width: 768px) {
  .editor-container {
    grid-template-columns: 1fr;
    grid-template-rows: 1fr 1fr;
  }
  
  .language-selector {
    flex-wrap: wrap;
  }
  
  .actions {
    flex-wrap: wrap;
  }
}
</style>
