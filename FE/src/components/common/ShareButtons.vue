<template>
  <div class="flex items-center gap-2">
    <span v-if="showLabel" class="text-sm text-gray-500 dark:text-gray-400 mr-1">Chia sẻ:</span>
    
    <!-- Facebook -->
    <button 
      @click="shareToFacebook" 
      class="share-btn bg-blue-600 hover:bg-blue-700"
      title="Chia sẻ lên Facebook"
    >
      <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
        <path d="M18.77 7.46H14.5v-1.9c0-.9.6-1.1 1-1.1h3V.5h-4.33C10.24.5 9.5 3.44 9.5 5.32v2.15h-3v4h3v12h5v-12h3.85l.42-4z"/>
      </svg>
    </button>

    <!-- Twitter/X -->
    <button 
      @click="shareToTwitter" 
      class="share-btn bg-black hover:bg-gray-800"
      title="Chia sẻ lên X (Twitter)"
    >
      <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
        <path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"/>
      </svg>
    </button>

    <!-- LinkedIn -->
    <button 
      @click="shareToLinkedIn" 
      class="share-btn bg-blue-700 hover:bg-blue-800"
      title="Chia sẻ lên LinkedIn"
    >
      <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 24 24">
        <path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433c-1.144 0-2.063-.926-2.063-2.065 0-1.138.92-2.063 2.063-2.063 1.14 0 2.064.925 2.064 2.063 0 1.139-.925 2.065-2.064 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"/>
      </svg>
    </button>

    <!-- Copy Link -->
    <button 
      @click="copyLink" 
      class="share-btn bg-gray-600 hover:bg-gray-700"
      :class="{ 'bg-green-600 hover:bg-green-600': copied }"
      :title="copied ? 'Đã sao chép!' : 'Sao chép link'"
    >
      <svg v-if="!copied" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
          d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
      </svg>
      <svg v-else class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
      </svg>
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const props = defineProps<{
  url?: string
  title?: string
  description?: string
  showLabel?: boolean
}>()

const copied = ref(false)

const shareUrl = computed(() => props.url || window.location.href)
const shareTitle = computed(() => props.title || document.title)
const shareDescription = computed(() => props.description || '')

const shareToFacebook = () => {
  const url = `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(shareUrl.value)}`
  window.open(url, '_blank', 'width=600,height=400')
}

const shareToTwitter = () => {
  const url = `https://twitter.com/intent/tweet?url=${encodeURIComponent(shareUrl.value)}&text=${encodeURIComponent(shareTitle.value)}`
  window.open(url, '_blank', 'width=600,height=400')
}

const shareToLinkedIn = () => {
  const url = `https://www.linkedin.com/sharing/share-offsite/?url=${encodeURIComponent(shareUrl.value)}`
  window.open(url, '_blank', 'width=600,height=400')
}

const copyLink = async () => {
  try {
    await navigator.clipboard.writeText(shareUrl.value)
    copied.value = true
    setTimeout(() => {
      copied.value = false
    }, 2000)
  } catch (err) {
    console.error('Failed to copy:', err)
  }
}
</script>

<style scoped>
.share-btn {
  @apply p-2 rounded-full text-white transition-all duration-200 transform hover:scale-110 focus:outline-none focus:ring-2 focus:ring-offset-2;
}
</style>
