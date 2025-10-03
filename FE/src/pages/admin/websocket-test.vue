<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold mb-4">WebSocket Test</h1>

    <div class="mb-4">
      <p class="text-lg mb-2">Connection Status:
        <span :class="isConnected ? 'text-green-500' : 'text-red-500'">
          {{ isConnected ? 'Connected' : 'Disconnected' }}
        </span>
      </p>
    </div>

    <div class="mb-4">
      <button
        @click="connect"
        :disabled="isConnected"
        class="px-4 py-2 bg-blue-500 text-white rounded disabled:bg-gray-300"
      >
        Connect
      </button>
      <button
        @click="disconnect"
        :disabled="!isConnected"
        class="px-4 py-2 bg-red-500 text-white rounded disabled:bg-gray-300 ml-2"
      >
        Disconnect
      </button>
    </div>

    <div class="mb-4">
      <h2 class="text-xl font-semibold mb-2">Test Messages</h2>
      <div class="mb-2">
        <input
          v-model="testMessage"
          placeholder="Enter test message"
          class="px-3 py-2 border border-gray-300 rounded"
        />
        <button
          @click="sendTestMessage"
          :disabled="!isConnected"
          class="px-4 py-2 bg-green-500 text-white rounded disabled:bg-gray-300 ml-2"
        >
          Send Test Message
        </button>
      </div>
    </div>

    <div class="mb-4">
      <h2 class="text-xl font-semibold mb-2">Subscribe to Topics</h2>
      <div class="mb-2">
        <input
          v-model="topic"
          placeholder="Enter topic (e.g., /topic/test)"
          class="px-3 py-2 border border-gray-300 rounded"
        />
        <button
          @click="subscribeToTopic"
          :disabled="!isConnected"
          class="px-4 py-2 bg-purple-500 text-white rounded disabled:bg-gray-300 ml-2"
        >
          Subscribe
        </button>
        <button
          @click="unsubscribeFromTopic"
          class="px-4 py-2 bg-orange-500 text-white rounded disabled:bg-gray-300 ml-2"
        >
          Unsubscribe
        </button>
      </div>
    </div>

    <div v-if="messages.length > 0">
      <h2 class="text-xl font-semibold mb-2">Received Messages</h2>
      <div class="border border-gray-300 rounded p-4 max-h-64 overflow-y-auto">
        <div
          v-for="(message, index) in messages"
          :key="index"
          class="mb-2 p-2 bg-gray-100 rounded"
        >
          <strong>{{ message.type }}:</strong> {{ message.message }}
          <pre class="text-xs mt-1">{{ JSON.stringify(message.data, null, 2) }}</pre>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useWebSocket } from '@/composable/useWebSocket'

const {
  isConnected,
  messages,
  connect,
  disconnect,
  subscribe,
  unsubscribe,
  sendMessage
} = useWebSocket()

const testMessage = ref('')
const topic = ref('/topic/test')

const sendTestMessage = () => {
  if (testMessage.value && isConnected.value) {
    sendMessage('/app/test', {
      message: testMessage.value,
      timestamp: new Date().toISOString()
    })
    testMessage.value = ''
  }
}

const subscribeToTopic = () => {
  if (topic.value && isConnected.value) {
    subscribe(topic.value, (message: any) => {
      console.log('Received message:', message)
      messages.value.push({
        type: 'Received',
        message: message.message || 'No message content',
        data: message,
        timestamp: new Date().toISOString()
      })
    })
  }
}

const unsubscribeFromTopic = () => {
  if (topic.value) {
    unsubscribe(topic.value)
  }
}
</script>
