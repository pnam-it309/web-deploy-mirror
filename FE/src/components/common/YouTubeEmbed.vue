<script setup lang="ts">
import { computed } from 'vue';

interface Props {
  url: string;
  title?: string;
  autoplay?: boolean;
  controls?: boolean;
  modestBranding?: boolean;
  mute?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  title: 'YouTube Video',
  autoplay: false,
  controls: true,
  modestBranding: true,
  mute: false
});

/**
 * Extract YouTube video ID from various URL formats:
 * - https://www.youtube.com/watch?v=VIDEO_ID
 * - https://youtu.be/VIDEO_ID
 * - https://www.youtube.com/embed/VIDEO_ID
 */
const extractVideoId = (url: string): string | null => {
  if (!url) return null;

  const patterns = [
    /(?:youtube\.com\/watch\?v=|youtu\.be\/|youtube\.com\/embed\/)([^&\n?#]+)/,
    /^([a-zA-Z0-9_-]{11})$/ // Direct video ID
  ];

  for (const pattern of patterns) {
    const match = url.match(pattern);
    if (match && match[1]) {
      return match[1];
    }
  }

  return null;
};

const videoId = computed(() => extractVideoId(props.url));

const embedUrl = computed(() => {
  if (!videoId.value) return null;

  const params = new URLSearchParams({
    autoplay: props.autoplay ? '1' : '0',
    controls: props.controls ? '1' : '0',
    modestbranding: props.modestBranding ? '1' : '0',
    rel: '0', // Don't show related videos
    mute: props.mute ? '1' : '0'
  });

  return `https://www.youtube.com/embed/${videoId.value}?${params.toString()}`;
});

const isValidYouTubeUrl = computed(() => !!videoId.value);
</script>

<template>
  <div class="youtube-embed-wrapper">
    <div v-if="isValidYouTubeUrl && embedUrl" class="relative w-full" style="padding-bottom: 56.25%;">
      <iframe :src="embedUrl" :title="title" class="absolute top-0 left-0 w-full h-full rounded-lg" frameborder="0"
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
        allowfullscreen></iframe>
    </div>

    <!-- Error/Invalid URL State -->
    <div v-else
      class="w-full aspect-video bg-gray-100 rounded-lg flex flex-col items-center justify-center text-gray-400 border-2 border-dashed border-gray-300">
      <svg class="w-12 h-12 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
          d="M15 10l4.553-2.276A1 1 0 0121 8.618v6.764a1 1 0 01-1.447.894L15 14M5 18h8a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z" />
      </svg>
      <span class="text-sm font-medium">Invalid YouTube URL</span>
      <span class="text-xs text-gray-400 mt-1">{{ url }}</span>
    </div>
  </div>
</template>

<style scoped>
.youtube-embed-wrapper {
  width: 100%;
}
</style>
