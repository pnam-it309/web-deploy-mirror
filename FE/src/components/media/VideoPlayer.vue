<template>
  <div class="video-player">
    <div class="player-container">
      <video
        ref="videoElement"
        class="video-element"
        :poster="poster"
        @loadedmetadata="onLoadedMetadata"
        @timeupdate="onTimeUpdate"
        @ended="onEnded"
        @play="isPlaying = true"
        @pause="isPlaying = false"
      >
        <source :src="src" :type="type" />
        Your browser does not support the video tag.
      </video>

      <!-- Custom Controls -->
      <div class="controls" :class="{ visible: showControls || !isPlaying }">
        <!-- Play/Pause Button -->
        <button @click="togglePlay" class="play-btn">
          <svg v-if="!isPlaying" fill="currentColor" viewBox="0 0 24 24">
            <path d="M8 5v14l11-7z"/>
          </svg>
          <svg v-else fill="currentColor" viewBox="0 0 24 24">
            <path d="M6 4h4v16H6V4zm8 0h4v16h-4V4z"/>
          </svg>
        </button>

        <!-- Progress Bar -->
        <div class="progress-container" @click="seek">
          <div class="progress-bar">
            <div class="progress-filled" :style="{ width: progress + '%' }"></div>
            <div class="progress-handle" :style="{ left: progress + '%' }"></div>
          </div>
        </div>

        <!-- Time Display -->
        <span class="time">{{ formatTime(currentTime) }} / {{ formatTime(duration) }}</span>

        <!-- Volume Control -->
        <div class="volume-control">
          <button @click="toggleMute" class="volume-btn">
            <svg v-if="volume === 0 || isMuted" fill="currentColor" viewBox="0 0 24 24">
              <path d="M16.5 12c0-1.77-1.02-3.29-2.5-4.03v2.21l2.45 2.45c.03-.2.05-.41.05-.63zm2.5 0c0 .94-.2 1.82-.54 2.64l1.51 1.51C20.63 14.91 21 13.5 21 12c0-4.28-2.99-7.86-7-8.77v2.06c2.89.86 5 3.54 5 6.71zM4.27 3L3 4.27 7.73 9H3v6h4l5 5v-6.73l4.25 4.25c-.67.52-1.42.93-2.25 1.18v2.06c1.38-.31 2.63-.95 3.69-1.81L19.73 21 21 19.73l-9-9L4.27 3zM12 4L9.91 6.09 12 8.18V4z"/>
            </svg>
            <svg v-else-if="volume < 0.5" fill="currentColor" viewBox="0 0 24 24">
              <path d="M7 9v6h4l5 5V4l-5 5H7z"/>
            </svg>
            <svg v-else fill="currentColor" viewBox="0 0 24 24">
              <path d="M3 9v6h4l5 5V4L7 9H3zm13.5 3c0-1.77-1.02-3.29-2.5-4.03v8.05c1.48-.73 2.5-2.25 2.5-4.02z"/>
            </svg>
          </button>
          <input
            type="range"
            min="0"
            max="100"
            v-model="volumeSlider"
            @input="changeVolume"
            class="volume-slider"
          />
        </div>

        <!-- Fullscreen Button -->
        <button @click="toggleFullscreen" class="fullscreen-btn">
          <svg v-if="!isFullscreen" fill="currentColor" viewBox="0 0 24 24">
            <path d="M7 14H5v5h5v-2H7v-3zm-2-4h2V7h3V5H5v5zm12 7h-3v2h5v-5h-2v3zM14 5v2h3v3h2V5h-5z"/>
          </svg>
          <svg v-else fill="currentColor" viewBox="0 0 24 24">
            <path d="M5 16h3v3h2v-5H5v2zm3-8H5v2h5V5H8v3zm6 11h2v-3h3v-2h-5v5zm2-11V5h-2v5h5V8h-3z"/>
          </svg>
        </button>
      </div>

      <!-- Loading Spinner -->
      <div v-if="isLoading" class="loading-spinner">
        <div class="spinner"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

interface Props {
  src: string
  poster?: string
  type?: string
  autoplay?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'video/mp4',
  autoplay: false
})

const videoElement = ref<HTMLVideoElement | null>(null)
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const volume = ref(1)
const volumeSlider = ref(100)
const isMuted = ref(false)
const isFullscreen = ref(false)
const showControls = ref(false)
const isLoading = ref(false)

let controlsTimeout: NodeJS.Timeout

const progress = computed(() => {
  if (duration.value === 0) return 0
  return (currentTime.value / duration.value) * 100
})

const togglePlay = () => {
  if (!videoElement.value) return
  
  if (isPlaying.value) {
    videoElement.value.pause()
  } else {
    videoElement.value.play()
  }
}

const seek = (e: MouseEvent) => {
  if (!videoElement.value) return
  
  const container = e.currentTarget as HTMLElement
  const rect = container.getBoundingClientRect()
  const clickX = e.clientX - rect.left
  const percentage = clickX / rect.width
  
  videoElement.value.currentTime = percentage * duration.value
}

const toggleMute = () => {
  if (!videoElement.value) return
  
  if (isMuted.value) {
    videoElement.value.volume = volume.value
    isMuted.value = false
  } else {
    videoElement.value.volume = 0
    isMuted.value = true
  }
}

const changeVolume = () => {
  if (!videoElement.value) return
  
  const newVolume = Number(volumeSlider.value) / 100
  videoElement.value.volume = newVolume
  volume.value = newVolume
  isMuted.value = newVolume === 0
}

const toggleFullscreen = () => {
  const container = videoElement.value?.parentElement
  if (!container) return
  
  if (!document.fullscreenElement) {
    container.requestFullscreen()
    isFullscreen.value = true
  } else {
    document.exitFullscreen()
    isFullscreen.value = false
  }
}

const onLoadedMetadata = () => {
  if (videoElement.value) {
    duration.value = videoElement.value.duration
    isLoading.value = false
  }
}

const onTimeUpdate = () => {
  if (videoElement.value) {
    currentTime.value = videoElement.value.currentTime
  }
}

const onEnded = () => {
  isPlaying.value = false
}

const formatTime = (seconds: number): string => {
  if (isNaN(seconds)) return '0:00'
  
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

// Show/hide custom controls on mouse move
const handleMouseMove = () => {
  showControls.value = true
  clearTimeout(controlsTimeout)
  
  controlsTimeout = setTimeout(() => {
    if (isPlaying.value) {
      showControls.value = false
    }
  }, 3000)
}

onMounted(() => {
  if (props.autoplay && videoElement.value) {
    videoElement.value.play()
  }
  
  // Add mouse move listener
  videoElement.value?.parentElement?.addEventListener('mousemove', handleMouseMove)
})

onUnmounted(() => {
  clearTimeout(controlsTimeout)
  videoElement.value?.parentElement?.removeEventListener('mousemove', handleMouseMove)
})
</script>

<style scoped>
.player-container {
  position: relative;
  width: 100%;
  background: #000;
  border-radius: 0.75rem;
  overflow: hidden;
}

.video-element {
  width: 100%;
  display: block;
}

.controls {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.8) 0%, transparent 100%);
  padding: 1rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.controls.visible {
  opacity: 1;
}

.play-btn, .volume-btn, .fullscreen-btn {
  background: transparent;
  border: none;
  color: white;
  width: 36px;
  height: 36px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s;
}

.play-btn:hover, .volume-btn:hover, .fullscreen-btn:hover {
  transform: scale(1.1);
}

.play-btn svg, .volume-btn svg, .fullscreen-btn svg {
  width: 24px;
  height: 24px;
}

.progress-container {
  flex: 1;
  height: 36px;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.progress-bar {
  position: relative;
  width: 100%;
  height: 4px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
}

.progress-filled {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background: #3b82f6;
  border-radius: 2px;
  transition: width 0.1s linear;
}

.progress-handle {
  position: absolute;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 12px;
  height: 12px;
  background: white;
  border-radius: 50%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.time {
  color: white;
  font-size: 0.875rem;
  font-family: monospace;
  white-space: nowrap;
}

.volume-control {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.volume-slider {
  width: 80px;
  -webkit-appearance: none;
  height: 4px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
  outline: none;
}

.volume-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 12px;
  height: 12px;
  background: white;
  border-radius: 50%;
  cursor: pointer;
}

.volume-slider::-moz-range-thumb {
  width: 12px;
  height: 12px;
  background: white;
  border-radius: 50%;
  cursor: pointer;
  border: none;
}

.loading-spinner {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.spinner {
  width: 48px;
  height: 48px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Mobile responsive */
@media (max-width: 768px) {
  .volume-control {
    display: none;
  }
  
  .controls {
    padding: 0.75rem;
    gap: 0.5rem;
  }
  
  .time {
    font-size: 0.75rem;
  }
}
</style>
