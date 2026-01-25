/**
 * Responsive Breakpoints
 * Mobile-first breakpoint system
 */

// Breakpoint values (in pixels)
export const breakpoints = {
  xs: 480, // Extra small devices (phones)
  sm: 640, // Small devices (landscape phones)
  md: 768, // Medium devices (tablets)
  lg: 1024, // Large devices (laptops)
  xl: 1280, // Extra large devices (desktops)
  '2xl': 1536, // 2X large devices (large desktops)
} as const

// Media query strings
export const mediaQueries = {
  xs: `@media (min-width: ${breakpoints.xs}px)`,
  sm: `@media (min-width: ${breakpoints.sm}px)`,
  md: `@media (min-width: ${breakpoints.md}px)`,
  lg: `@media (min-width: ${breakpoints.lg}px)`,
  xl: `@media (min-width: ${breakpoints.xl}px)`,
  '2xl': `@media (min-width: ${breakpoints['2xl']}px)`,

  // Max-width queries (for mobile-first)
  maxXs: `@media (max-width: ${breakpoints.xs - 1}px)`,
  maxSm: `@media (max-width: ${breakpoints.sm - 1}px)`,
  maxMd: `@media (max-width: ${breakpoints.md - 1}px)`,
  maxLg: `@media (max-width: ${breakpoints.lg - 1}px)`,
  maxXl: `@media (max-width: ${breakpoints.xl - 1}px)`,
  max2xl: `@media (max-width: ${breakpoints['2xl'] - 1}px)`,

  // Custom ranges
  smToMd: `@media (min-width: ${breakpoints.sm}px) and (max-width: ${breakpoints.md - 1}px)`,
  mdToLg: `@media (min-width: ${breakpoints.md}px) and (max-width: ${breakpoints.lg - 1}px)`,
  lgToXl: `@media (min-width: ${breakpoints.lg}px) and (max-width: ${breakpoints.xl - 1}px)`,
} as const

// Container max-widths
export const container = {
  xs: '100%',
  sm: '640px',
  md: '768px',
  lg: '1024px',
  xl: '1280px',
  '2xl': '1536px',
} as const

// Utility function to check breakpoint (for use in JS)
export const isBreakpoint = (breakpoint: keyof typeof breakpoints): boolean => {
  if (typeof window === 'undefined') return false
  return window.innerWidth >= breakpoints[breakpoint]
}

// Type exports
export type Breakpoint = keyof typeof breakpoints
export type MediaQuery = keyof typeof mediaQueries
export type Container = typeof container
