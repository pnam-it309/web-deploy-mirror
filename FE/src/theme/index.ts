/**
 * Theme Configuration Entry Point
 * Re-export all theme-related configurations
 *
 * Usage:
 * import { brand, fontSize, spacing, shadows } from '@/theme'
 */

// Design tokens
export * from './colors'
export * from './typography'
export * from './spacing'
export * from './shadows'
export * from './animations'
export * from './breakpoints'
export * from './zIndex'

// Ant Design theme configuration
export { default as antdTheme, colorPrimary } from './antd/themeConfig'

// CSS imports (for global styles)
// Import this in main.ts: import '@/theme/css/variables.css'
