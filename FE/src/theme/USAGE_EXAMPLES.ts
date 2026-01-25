/**
 * Example: How to Use Theme Tokens
 *
 * This file demonstrates best practices for using the new theme system
 */

import { defineComponent } from 'vue'
import {
  brand,
  semantic,
  gray,
  fontSize,
  fontWeight,
  spacing,
  radius,
  shadows,
  duration,
  easing,
  zIndex,
} from '@/theme'

export default defineComponent({
  name: 'ThemeExample',

  setup() {
    // Example 1: Using colors
    const colorStyles = {
      primary: {
        backgroundColor: brand.primary,
        color: '#ffffff',
        '&:hover': {
          backgroundColor: brand.primaryHover,
        },
      },
      success: {
        backgroundColor: semantic.success,
        color: '#ffffff',
      },
      muted: {
        backgroundColor: gray[100],
        color: gray[700],
      },
    }

    // Example 2: Using typography
    const typographyStyles = {
      heading: {
        fontSize: fontSize['2xl'],
        fontWeight: fontWeight.bold,
        lineHeight: 1.25,
      },
      body: {
        fontSize: fontSize.base,
        fontWeight: fontWeight.normal,
        lineHeight: 1.5,
      },
      small: {
        fontSize: fontSize.sm,
        fontWeight: fontWeight.normal,
        color: gray[500],
      },
    }

    // Example 3: Using spacing & radius
    const layoutStyles = {
      card: {
        padding: spacing[6], // 24px
        margin: spacing[4], // 16px
        borderRadius: radius.lg, // 8px
        gap: spacing[4], // 16px
      },
      button: {
        padding: `${spacing[2]} ${spacing[4]}`, // 8px 16px
        borderRadius: radius.md, // 6px
      },
    }

    // Example 4: Using shadows
    const shadowStyles = {
      card: {
        boxShadow: shadows.md,
        '&:hover': {
          boxShadow: shadows.lg,
        },
      },
    }

    // Example 5: Using animations
    const animationStyles = {
      transition: {
        transitionDuration: duration.normal, // 200ms
        transitionTimingFunction: easing.smooth, // cubic-bezier(0.4, 0, 0.2, 1)
        transitionProperty: 'all',
      },
      hover: {
        transitionDuration: duration.fast, // 150ms
        transform: 'scale(1.02)',
      },
    }

    // Example 6: Using z-index
    const layerStyles = {
      modal: {
        position: 'fixed' as const,
        zIndex: zIndex.modal, // 700
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
      },
      dropdown: {
        position: 'absolute' as const,
        zIndex: zIndex.dropdown, // 100
      },
    }

    // Example 7: Complete component style
    const completeCardStyle = {
      // Layout
      padding: spacing[6],
      margin: spacing[4],
      borderRadius: radius.lg,

      // Colors
      backgroundColor: '#ffffff',
      borderColor: gray[200],
      borderWidth: '1px',
      borderStyle: 'solid',

      // Typography
      fontSize: fontSize.base,
      fontWeight: fontWeight.normal,
      color: gray[900],

      // Shadow
      boxShadow: shadows.sm,

      // Animation
      transition: `all ${duration.normal} ${easing.smooth}`,

      '&:hover': {
        boxShadow: shadows.md,
        borderColor: brand.primary,
        transform: 'translateY(-2px)',
      },
    }

    return {
      colorStyles,
      typographyStyles,
      layoutStyles,
      shadowStyles,
      animationStyles,
      layerStyles,
      completeCardStyle,
    }
  },
})
