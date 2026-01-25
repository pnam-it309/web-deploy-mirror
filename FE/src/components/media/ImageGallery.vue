<template>
  <div class="image-gallery">
    <!-- Thumbnail Grid -->
    <div class="gallery-grid">
      <div
        v-for="(image, index) in images"
        :key="index"
        class="gallery-item"
        @click="openLightbox(index)"
      >
        <img
          :src="image.thumbnail || image.url"
          :alt="image.alt || `Image ${index + 1}`"
          class="thumbnail"
          loading="lazy"
        />
        <div class="overlay">
          <svg class="zoom-icon" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM10 7v6m3-3H7" />
          </svg>
        </div>
      </div>
    </div>

    <!-- Lightbox Modal -->
    <Teleport to="body">
      <Transition name="fade">
        <div v-if="lightboxOpen" class="lightbox-overlay" @click="closeLightbox">
          <button class="close-btn" @click="closeLightbox">
            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>

          <button class="nav-btn prev" @click.stop="prevImage" v-if="images.length > 1">
            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
            </svg>
          </button>

          <button class="nav-btn next" @click.stop="nextImage" v-if="images.length > 1">
            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
            </svg>
          </button>

          <div class="lightbox-content" @click.stop>
            <img
              :src="currentImage.url"
              :alt="currentImage.alt"
              class="lightbox-image"
            />
            
            <div class="image-info">
              <p class="image-caption" v-if="currentImage.caption">
                {{ currentImage.caption }}
              </p>
              <p class="image-counter">
                {{ currentIndex + 1 }} / {{ images.length }}
              </p>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface GalleryImage {
  url: string
  thumbnail?: string
  alt?: string
  caption?: string
}

interface Props {
  images: GalleryImage[]
  columns?: number
}

const props = withDefaults(defineProps<Props>(), {
  columns: 3
})

const lightboxOpen = ref(false)
const currentIndex = ref(0)

const currentImage = computed(() => props.images[currentIndex.value] || {})

const openLightbox = (index: number) => {
  currentIndex.value = index
  lightboxOpen.value = true
  document.body.style.overflow = 'hidden' // Prevent scroll
}

const closeLightbox = () => {
  lightboxOpen.value = false
  document.body.style.overflow = '' // Restore scroll
}

const nextImage = () => {
  currentIndex.value = (currentIndex.value + 1) % props.images.length
}

const prevImage = () => {
  currentIndex.value = (currentIndex.value - 1 + props.images.length) % props.images.length
}

// Keyboard navigation
const handleKeydown = (e: KeyboardEvent) => {
  if (!lightboxOpen.value) return
  
  if (e.key === 'Escape') closeLightbox()
  if (e.key === 'ArrowRight') nextImage()
  if (e.key === 'ArrowLeft') prevImage()
}

// Add keyboard listener
if (typeof window !== 'undefined') {
  window.addEventListener('keydown', handleKeydown)
}
</script>

<style scoped>
.gallery-grid {
  display: grid;
  grid-template-columns: repeat(v-bind(columns), 1fr);
  gap: 1rem;
  padding: 1rem 0;
}

.gallery-item {
  position: relative;
  aspect-ratio: 4/3;
  overflow: hidden;
  border-radius: 0.75rem;
  cursor: pointer;
  background: #f3f4f6;
}

.thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.gallery-item:hover .thumbnail {
  transform: scale(1.1);
}

.overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.gallery-item:hover .overlay {
  opacity: 1;
}

.zoom-icon {
  width: 48px;
  height: 48px;
  color: white;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

/* Lightbox */
.lightbox-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.95);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
}

.lightbox-content {
  max-width: 90vw;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.lightbox-image {
  max-width: 100%;
  max-height: 80vh;
  object-fit: contain;
  border-radius: 0.5rem;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
}

.image-info {
  text-align: center;
  color: white;
}

.image-caption {
  font-size: 1rem;
  margin-bottom: 0.5rem;
}

.image-counter {
  font-size: 0.875rem;
  color: #d1d5db;
}

.close-btn, .nav-btn {
  position: fixed;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: white;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
  backdrop-filter: blur(10px);
}

.close-btn:hover, .nav-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.1);
}

.close-btn {
  top: 2rem;
  right: 2rem;
}

.nav-btn {
  top: 50%;
  transform: translateY(-50%);
}

.nav-btn.prev {
  left: 2rem;
}

.nav-btn.next {
  right: 2rem;
}

.close-btn svg, .nav-btn svg {
  width: 24px;
  height: 24px;
}

/* Fade transition */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

/* Mobile responsive */
@media (max-width: 768px) {
  .gallery-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 0.75rem;
  }
  
  .lightbox-overlay {
    padding: 1rem;
  }
  
  .close-btn {
    top: 1rem;
    right: 1rem;
  }
  
  .nav-btn.prev {
    left: 1rem;
  }
  
  .nav-btn.next {
    right: 1rem;
  }
}

@media (max-width: 480px) {
  .gallery-grid {
    grid-template-columns: 1fr;
  }
}
</style>
