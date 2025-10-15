import { ref, reactive } from 'vue'

export interface TableAnimationConfig {
  hoverScale?: number
  hoverTransition?: string
  rowHoverColor?: string
  selectedRowColor?: string
  loadingDuration?: number
  staggerDelay?: number
}

export interface TableAnimationState {
  hoveredRowIndex: number | null
  selectedRowIndex: number | null
  isLoading: boolean
  animatedRows: Set<number>
}

const defaultConfig: TableAnimationConfig = {
  hoverScale: 1.02,
  hoverTransition: 'all 0.2s cubic-bezier(0.4, 0, 0.2, 1)',
  rowHoverColor: 'rgba(99, 102, 241, 0.1)',
  selectedRowColor: 'rgba(99, 102, 241, 0.2)',
  loadingDuration: 600,
  staggerDelay: 50,
}

export function useTableAnimations(config: TableAnimationConfig = {}) {
  const finalConfig = { ...defaultConfig, ...config }

  const state = reactive<TableAnimationState>({
    hoveredRowIndex: null,
    selectedRowIndex: null,
    isLoading: false,
    animatedRows: new Set(),
  })

  // Computed styles for table elements
  const getRowStyle = (index: number) => {
    const baseStyle: Record<string, string> = {
      transition: finalConfig.hoverTransition || '',
      cursor: 'pointer',
    }

    // Hover effect
    if (state.hoveredRowIndex === index) {
      baseStyle.transform = `scale(${finalConfig.hoverScale})`
      baseStyle.backgroundColor = finalConfig.rowHoverColor || ''
      baseStyle.boxShadow = '0 4px 12px rgba(0, 0, 0, 0.15)'
    }

    // Selected effect
    if (state.selectedRowIndex === index) {
      baseStyle.backgroundColor = finalConfig.selectedRowColor || ''
      baseStyle.borderLeft = '4px solid #6366f1'
    }

    return baseStyle
  }

  const getCellStyle = (rowIndex: number, colIndex: number) => {
    const delay = (rowIndex * (finalConfig.staggerDelay || 50)) + (colIndex * 20)

    return {
      animationDelay: `${delay}ms`,
      opacity: state.animatedRows.has(rowIndex) ? 1 : 0,
      transform: state.animatedRows.has(rowIndex) ? 'translateY(0)' : 'translateY(20px)',
      transition: `opacity 0.3s ease ${delay}ms, transform 0.3s ease ${delay}ms`,
    }
  }

  // Table loading animation
  const startLoadingAnimation = async (rowCount: number) => {
    state.isLoading = true
    state.animatedRows.clear()

    // Simulate staggered row appearance
    for (let i = 0; i < rowCount; i++) {
      await new Promise(resolve => setTimeout(resolve, finalConfig.staggerDelay || 50))
      state.animatedRows.add(i)
    }

    state.isLoading = false
  }

  // Row interaction handlers
  const handleRowHover = (index: number | null) => {
    state.hoveredRowIndex = index
  }

  const handleRowClick = (index: number) => {
    state.selectedRowIndex = state.selectedRowIndex === index ? null : index
  }

  const clearSelection = () => {
    state.selectedRowIndex = null
  }

  // Skeleton loading styles
  const skeletonStyle = {
    background: 'linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%)',
    backgroundSize: '200% 100%',
    animation: 'loading 1.5s infinite',
  }

  // Pulse animation for loading states
  const pulseAnimation = {
    animation: 'pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite',
  }

  return {
    // State
    state,

    // Computed styles
    getRowStyle,
    getCellStyle,

    // Animation controls
    startLoadingAnimation,

    // Event handlers
    handleRowHover,
    handleRowClick,
    clearSelection,

    // Animation styles
    skeletonStyle,
    pulseAnimation,

    // Configuration
    config: finalConfig,
  }
}

// CSS animations (can be added to global styles)
export const tableAnimationsCSS = `
@keyframes loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes scaleIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

/* Table row hover effects */
.table-row-hover {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

.table-row-hover:hover {
  transform: scale(1.01);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* Smooth transitions for all table elements */
.table-transition {
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Loading skeleton */
.skeleton {
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: loading 1.5s infinite;
}

/* Pulse effect */
.pulse {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}
`
