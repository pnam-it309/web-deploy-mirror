/**
 * Color Contrast Utilities
 * Functions for calculating color contrast
 */

/**
 * Get appropriate text color (black or white) based on background color
 * Uses relative luminance calculation for accessibility
 * @param bgColor - Background color in hex format (without #)
 * @returns '#000000' for dark text or '#ffffff' for light text
 * @example
 * getTextColor('224D66') // '#ffffff' (light text on dark background)
 * getTextColor('F4F7F9') // '#000000' (dark text on light background)
 */
export const getTextColor = (bgColor: string): string => {
  if (!bgColor || bgColor.length < 3) return '#ffffff'

  const r = parseInt(bgColor.substring(0, 2), 16)
  const g = parseInt(bgColor.substring(2, 4), 16)
  const b = parseInt(bgColor.substring(4, 6), 16)

  // Calculate relative luminance
  const brightness = (r * 299 + g * 587 + b * 114) / 1000

  // Return black text for light backgrounds, white text for dark backgrounds
  return brightness > 125 ? '#000000' : '#ffffff'
}
