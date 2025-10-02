import { ref, onMounted, onUnmounted } from 'vue'

export function useWebSocket() {
  const isConnected = ref(false)
  const messages = ref<any[]>([])

  const connect = () => {
    if (typeof window !== 'undefined' && (window as any).$websocket) {
      const websocket = (window as any).$websocket

      websocket.connect(() => {
        isConnected.value = true
        console.log('WebSocket connected successfully')
      })
    }
  }

  const disconnect = () => {
    if (typeof window !== 'undefined' && (window as any).$websocket) {
      const websocket = (window as any).$websocket
      websocket.disconnect()
      isConnected.value = false
      console.log('WebSocket disconnected')
    }
  }

  const subscribe = (destination: string, callback: (message: any) => void) => {
    if (typeof window !== 'undefined' && (window as any).$websocket) {
      const websocket = (window as any).$websocket
      websocket.subscribe(destination, callback)
    }
  }

  const unsubscribe = (destination: string) => {
    if (typeof window !== 'undefined' && (window as any).$websocket) {
      const websocket = (window as any).$websocket
      websocket.unsubscribe(destination)
    }
  }

  const sendMessage = (destination: string, message: any) => {
    if (typeof window !== 'undefined' && (window as any).$websocket) {
      const websocket = (window as any).$websocket
      websocket.sendMessage(destination, message)
    }
  }

  onMounted(() => {
    connect()
  })

  onUnmounted(() => {
    disconnect()
  })

  return {
    isConnected,
    messages,
    connect,
    disconnect,
    subscribe,
    unsubscribe,
    sendMessage
  }
}
