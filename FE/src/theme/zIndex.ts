/**
 * Z-Index Scale
 * Consistent layering system for UI elements
 */

export const zIndex = {
  // Base layers
  base: 0,

  // Content layers
  content: 1,
  docked: 10,

  // UI elements
  dropdown: 100,
  sticky: 200,
  fixed: 300,

  // Overlays
  backdrop: 400,
  overlay: 500,
  drawer: 600,

  // Modals and popovers
  modal: 700,
  popover: 800,

  // Tooltips
  tooltip: 900,

  // Notifications
  notification: 1000,
  toast: 1100,

  // Max layer
  max: 9999,
} as const

// Common z-index values for specific components
export const componentZIndex = {
  header: zIndex.sticky,
  sidebar: zIndex.docked,
  mobileNav: zIndex.drawer,
  searchOverlay: zIndex.overlay,
  modalBackdrop: zIndex.backdrop,
  modal: zIndex.modal,
  dropdown: zIndex.dropdown,
  tooltip: zIndex.tooltip,
  notification: zIndex.notification,
  toast: zIndex.toast,
} as const

// Ant Design component z-index (for reference)
export const antdZIndex = {
  affix: 10,
  backTop: 10,
  badge: 1,
  drawer: 1000,
  dropdown: 1050,
  message: 1010,
  modal: 1000,
  modalMask: 1000,
  notification: 1010,
  popover: 1030,
  tooltip: 1060,
} as const

// Type exports
export type ZIndex = typeof zIndex
export type ComponentZIndex = typeof componentZIndex
export type AntdZIndex = typeof antdZIndex
