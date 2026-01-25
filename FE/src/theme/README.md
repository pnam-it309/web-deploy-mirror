# 🎨 Theme System

Centralized design system và theme configuration cho FPL-UDPM-Catalog.

## 📁 Structure

```
theme/
├── index.ts                 # Main export - import all tokens here
├── colors.ts                # Color palette (brand, semantic, grayscale)
├── typography.ts            # Font system (family, size, weight, line-height)
├── spacing.ts               # Spacing scale & border radius
├── shadows.ts               # Shadow elevations
├── animations.ts            # Animation durations, easing, keyframes
├── breakpoints.ts           # Responsive breakpoints & media queries
├── zIndex.ts                # Z-index layering system
├── USAGE_EXAMPLES.ts        # Examples of how to use theme tokens
├── antd/                    # Ant Design specific configurations
│   └── themeConfig.ts       # Ant Design theme overrides
└── css/                     # CSS files
    └── variables.css        # CSS custom properties & global styles
```

## 🚀 Quick Start

### Basic Usage

```typescript
// Import what you need
import { brand, fontSize, spacing, shadows } from '@/theme'

// Use in your component
const styles = {
  backgroundColor: brand.primary,
  fontSize: fontSize.base,
  padding: spacing[4],
  boxShadow: shadows.md,
}
```

### Import All

```typescript
import * as theme from '@/theme'

const styles = {
  color: theme.brand.primary,
  fontSize: theme.fontSize.lg,
}
```

## 📚 Available Tokens

### Colors (`colors.ts`)

```typescript
import { brand, semantic, gray, getThemeColors } from '@/theme'

// Brand colors
brand.primary // '#224D66' - Navy blue
brand.primaryHover // '#1B3F53' - Darker navy
brand.primaryLight // '#D1E2EC' - Light blue
brand.secondary // '#F4F7F9' - Very light gray

// Semantic colors
semantic.success // '#52c41a'
semantic.warning // '#faad14'
semantic.error // '#ff4d4f'
semantic.info // '#1890ff'

// Grayscale
gray[50] // Lightest
gray[100]
// ... to gray[900]
gray[900] // Darkest

// Theme-aware colors
const colors = getThemeColors(isDark)
// Returns: { background, foreground, border, muted }
```

### Typography (`typography.ts`)

```typescript
import { fontFamily, fontSize, fontWeight, lineHeight } from '@/theme'

// Font families
fontFamily.sans // ['Inter', 'system-ui', ...]
fontFamily.serif // ['Georgia', 'serif']
fontFamily.mono // ['JetBrains Mono', 'monospace']

// Font sizes
fontSize.xs // '0.75rem' (12px)
fontSize.sm // '0.875rem' (14px)
fontSize.base // '1rem' (16px)
fontSize.lg // '1.125rem' (18px)
fontSize['2xl'] // '1.5rem' (24px)
// ... up to fontSize['6xl']

// Font weights
fontWeight.normal // 400
fontWeight.medium // 500
fontWeight.semibold // 600
fontWeight.bold // 700

// Line heights
lineHeight.tight // 1.25
lineHeight.normal // 1.5
lineHeight.relaxed // 1.75
```

### Spacing (`spacing.ts`)

```typescript
import { spacing, radius } from '@/theme'

// Spacing scale
spacing[0] // '0'
spacing[1] // '0.25rem' (4px)
spacing[2] // '0.5rem' (8px)
spacing[4] // '1rem' (16px)
spacing[8] // '2rem' (32px)
// ... up to spacing[96]

// Border radius
radius.none // '0'
radius.sm // '0.125rem' (2px)
radius.base // '0.25rem' (4px)
radius.lg // '0.5rem' (8px)
radius.full // '9999px'
```

### Shadows (`shadows.ts`)

```typescript
import { shadows, customShadows } from '@/theme'

// Standard shadows
shadows.none // 'none'
shadows.sm // Small shadow
shadows.md // Medium shadow
shadows.lg // Large shadow
shadows.xl // Extra large shadow

// Custom shadows
customShadows.tableRowHover // '0 4px 12px rgba(0, 0, 0, 0.15)'
customShadows.card // '0 2px 8px rgba(0, 0, 0, 0.1)'
customShadows.modal // '0 12px 48px rgba(0, 0, 0, 0.12)'
```

### Animations (`animations.ts`)

```typescript
import { duration, easing, animations } from '@/theme'

// Durations
duration.fastest // '75ms'
duration.fast // '150ms'
duration.normal // '200ms'
duration.slow // '300ms'

// Easing functions
easing.linear // 'linear'
easing.smooth // 'cubic-bezier(0.4, 0, 0.2, 1)'
easing.bounce // 'cubic-bezier(0.68, -0.55, 0.265, 1.55)'

// Predefined animations
animations.tableTransition // { duration: '200ms', easing: 'smooth' }
animations.skeleton // { duration: '1.5s', iteration: 'infinite' }
```

### Breakpoints (`breakpoints.ts`)

```typescript
import { breakpoints, mediaQueries, isBreakpoint } from '@/theme'

// Breakpoint values
breakpoints.sm // 640
breakpoints.md // 768
breakpoints.lg // 1024
breakpoints.xl // 1280

// Media queries
mediaQueries.md // '@media (min-width: 768px)'
mediaQueries.lg // '@media (min-width: 1024px)'

// JavaScript helper
if (isBreakpoint('md')) {
  console.log('Screen >= 768px')
}
```

### Z-Index (`zIndex.ts`)

```typescript
import { zIndex, componentZIndex } from '@/theme'

// Base scale
zIndex.base // 0
zIndex.dropdown // 100
zIndex.sticky // 200
zIndex.modal // 700
zIndex.tooltip // 900

// Component-specific
componentZIndex.header // 200
componentZIndex.sidebar // 10
componentZIndex.modal // 700
```

## 🎯 Usage Patterns

### Vue Component (Composition API)

```vue
<script setup lang="ts">
import { brand, spacing, fontSize, shadows } from '@/theme'

const cardStyles = {
  padding: spacing[6],
  backgroundColor: '#ffffff',
  borderRadius: '8px',
  boxShadow: shadows.md,
  fontSize: fontSize.base,
  color: brand.primary,
  transition: 'all 0.2s ease',
}
</script>

<template>
  <div :style="cardStyles">
    <h2>My Card</h2>
  </div>
</template>
```

### CSS-in-JS / Style Object

```typescript
import { brand, spacing, shadows, duration, easing } from '@/theme'

const styles = {
  container: {
    padding: spacing[8],
    maxWidth: '1200px',
    margin: '0 auto',
  },

  card: {
    padding: spacing[6],
    backgroundColor: '#ffffff',
    boxShadow: shadows.sm,
    borderRadius: '8px',
    transition: `all ${duration.normal} ${easing.smooth}`,

    '&:hover': {
      boxShadow: shadows.md,
      transform: 'translateY(-2px)',
    },
  },

  button: {
    backgroundColor: brand.primary,
    color: '#ffffff',
    padding: `${spacing[2]} ${spacing[4]}`,
    borderRadius: '4px',

    '&:hover': {
      backgroundColor: brand.primaryHover,
    },
  },
}
```

### Responsive Styles

```typescript
import { mediaQueries, fontSize, spacing } from '@/theme'

const responsiveStyles = {
  fontSize: fontSize.sm,
  padding: spacing[4],

  [mediaQueries.md]: {
    fontSize: fontSize.base,
    padding: spacing[6],
  },

  [mediaQueries.lg]: {
    fontSize: fontSize.lg,
    padding: spacing[8],
  },
}
```

## ✅ Best Practices

### DO ✅

```typescript
// ✅ Import from centralized theme
import { brand, spacing } from '@/theme'

const styles = {
  color: brand.primary,
  padding: spacing[4],
}

// ✅ Use semantic color names
const buttonStyles = {
  backgroundColor: semantic.success,
}

// ✅ Use spacing scale
const card = {
  padding: spacing[6],
  margin: spacing[4],
}
```

### DON'T ❌

```typescript
// ❌ Hardcoded values
const styles = {
  color: '#224D66',
  padding: '24px',
}

// ❌ Magic numbers
const card = {
  padding: '16px',
  margin: '12px',
}

// ❌ Inconsistent values
const header = {
  padding: '15px', // Not in spacing scale
}
```

## 🔄 Migration Guide

### Step 1: Identify Hardcoded Values

```typescript
// Before
const styles = {
  color: '#224D66',
  fontSize: '16px',
  padding: '16px',
}
```

### Step 2: Replace with Theme Tokens

```typescript
// After
import { brand, fontSize, spacing } from '@/theme'

const styles = {
  color: brand.primary,
  fontSize: fontSize.base,
  padding: spacing[4],
}
```

### Step 3: Test

- Verify visual appearance remains the same
- Check responsive behavior
- Test hover states and animations

## 📖 Related Files

- [USAGE_EXAMPLES.ts](./USAGE_EXAMPLES.ts) - Detailed usage examples
- [FRONTEND-ORGANIZATION-GUIDE.md](../FRONTEND-ORGANIZATION-GUIDE.md) - Complete organization guide
- [DEVELOPMENT-PROGRESS.md](../DEVELOPMENT-PROGRESS.md) - Development progress

## 🆘 Troubleshooting

### Import errors?

Make sure you're importing from `@/theme`:

```typescript
// ✅ Correct
import { brand } from '@/theme'

// ❌ Wrong
import { brand } from '@/theme/colors' // Don't import from subfiles
```

### TypeScript errors?

All theme tokens are typed. Use TypeScript's autocomplete:

```typescript
import { brand } from '@/theme'

// TypeScript will suggest: primary, primaryHover, primaryLight, secondary
const color = brand.
```

### Need a value that doesn't exist?

Add it to the appropriate file (`colors.ts`, `spacing.ts`, etc.) and it will be automatically exported.

---

**Last Updated:** 2026-01-13  
**Version:** 1.0.0  
**Maintained By:** Frontend Team
