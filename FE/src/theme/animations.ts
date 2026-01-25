/**
 * Animation Configuration
 * Durations, timing functions, and keyframe names
 */

// Animation durations
export const duration = {
  fastest: '75ms',
  faster: '100ms',
  fast: '150ms',
  normal: '200ms',
  slow: '300ms',
  slower: '400ms',
  slowest: '500ms',
} as const

// Easing functions (timing functions)
export const easing = {
  linear: 'linear',
  ease: 'ease',
  easeIn: 'ease-in',
  easeOut: 'ease-out',
  easeInOut: 'ease-in-out',
  // Custom cubic-bezier from table animations
  smooth: 'cubic-bezier(0.4, 0, 0.2, 1)',
  bounce: 'cubic-bezier(0.68, -0.55, 0.265, 1.55)',
} as const

// Animation keyframe names (from index.css)
export const keyframes = {
  slideInUp: 'slideInUp',
  fadeIn: 'fadeIn',
  scaleIn: 'scaleIn',
  loading: 'loading', // Skeleton loading
  pulse: 'pulse',
} as const

// Predefined animation configurations
export const animations = {
  // Table animations
  tableTransition: {
    duration: duration.normal,
    easing: easing.smooth,
  },
  tableRowHover: {
    duration: duration.normal,
    easing: easing.smooth,
  },

  // Loading animations
  skeleton: {
    duration: '1.5s',
    easing: easing.linear,
    iteration: 'infinite',
  },
  pulse: {
    duration: '2s',
    easing: easing.smooth,
    iteration: 'infinite',
  },

  // Page transitions
  pageSlideIn: {
    duration: duration.slow,
    easing: easing.smooth,
  },
  pageFadeIn: {
    duration: duration.normal,
    easing: easing.ease,
  },

  // Component animations
  modalScaleIn: {
    duration: duration.normal,
    easing: easing.smooth,
  },
  tooltipFadeIn: {
    duration: duration.fast,
    easing: easing.easeOut,
  },
} as const

// Transition properties
export const transition = {
  all: 'all',
  colors: 'color, background-color, border-color, text-decoration-color, fill, stroke',
  opacity: 'opacity',
  shadow: 'box-shadow',
  transform: 'transform',
  default: 'all 0.2s cubic-bezier(0.4, 0, 0.2, 1)',
} as const

// Type exports
export type Duration = typeof duration
export type Easing = typeof easing
export type Keyframes = typeof keyframes
export type Animations = typeof animations
export type Transition = typeof transition
