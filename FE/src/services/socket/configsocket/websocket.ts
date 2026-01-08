// services/websocket.ts

import { Client, StompSubscription, IMessage, Frame } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { localStorageAction } from '@/utils/storage.ts'
import { ACCESS_TOKEN_STORAGE_KEY } from '@/constants/storagekey.ts'
import { ReconnectStrategy } from '../strategies/reconnect.ts'
import { BackpressureQueue } from '../middleware/backpressure.ts'
import { ConnectionPool } from '../service/connectionPool.ts'

// ENV
export const { VITE_BASE_URL_CLIENT_SOCKET } = import.meta.env || {}

type SubscriptionItem = {
  stompSub: StompSubscription
  callbacks: Set<(msg: any) => void>
}

// Export the class so ConnectionPool can instantiate it
export class WebSocketService {
  private client: Client | null = null
  private connected = false
  private subscriptions: Record<string, SubscriptionItem> = {}

  private isConnecting = false
  private pendingSubscriptions: Record<string, Set<(msg: any) => void>> = {}

  // Advanced Strategies
  private reconnectStrategy: ReconnectStrategy
  private messageQueue: BackpressureQueue<{ destination: string; body: string }>

  constructor() {
    this.reconnectStrategy = new ReconnectStrategy({
      baseDelayMs: 2000,
      maxDelayMs: 30000,
      backoffType: 'exponential',
      jitterRatio: 0.2,
    })

    this.messageQueue = new BackpressureQueue({
      highWaterMark: 1000,
      policy: 'drop_oldest',
    })
  }

  connect(callback?: () => void) {
    if (this.connected) {
      // console.log("✅ WebSocket shard connected.");
      if (callback) callback()
      return
    }

    if (this.isConnecting) {
      return
    }

    this.isConnecting = true

    const token = localStorageAction.get(ACCESS_TOKEN_STORAGE_KEY)
    const socketURL = `${VITE_BASE_URL_CLIENT_SOCKET}/ws?token=${encodeURIComponent(token || '')}`

    this.client = new Client({
      webSocketFactory: () => new SockJS(socketURL),
      reconnectDelay: 0,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,

      onConnect: (frame: Frame) => {
        console.log('✅ WebSocket Shard Connected')
        this.connected = true
        this.isConnecting = false
        this.reconnectStrategy.reset()

        if (callback) callback()

        this.resubscribeAll()
        this.flushMessageQueue()
      },

      onStompError: (frame) => {
        console.error('❌ STOMP Error:', frame)
        this.connected = false
        this.scheduleReconnect()
      },

      onWebSocketClose: () => {
        // console.warn("⚠️ WebSocket Shard Closed.");
        this.connected = false
        this.isConnecting = false
        this.scheduleReconnect()
      },
    })

    try {
      this.client.activate()
    } catch (e) {
      console.error('Connection Error:', e)
      this.scheduleReconnect()
    }
  }

  private scheduleReconnect() {
    if (this.connected) return
    const delay = this.reconnectStrategy.nextDelayMs()
    setTimeout(() => {
      this.connect()
    }, delay)
  }

  private resubscribeAll() {
    Object.entries(this.pendingSubscriptions).forEach(([destination, callbacks]) => {
      callbacks.forEach((cb) => this.internalSubscribe(destination, cb))
    })
    this.pendingSubscriptions = {}

    const oldSubs = this.subscriptions
    this.subscriptions = {}
    Object.entries(oldSubs).forEach(([destination, item]) => {
      item.callbacks.forEach((cb) => this.internalSubscribe(destination, cb))
    })
  }

  private flushMessageQueue() {
    if (this.messageQueue.isEmpty()) return
    const messages = this.messageQueue.drain(this.messageQueue.size())
    messages.forEach((msg) => {
      this.client?.publish({ destination: msg.destination, body: msg.body })
    })
  }

  subscribe(destination: string, callback: (msg: any) => void) {
    if (!this.connected) {
      // console.warn(`⏳ Shard not ready. Queue subscribe: ${destination}`);
      if (!this.pendingSubscriptions[destination]) {
        this.pendingSubscriptions[destination] = new Set()
      }
      this.pendingSubscriptions[destination].add(callback)

      if (!this.isConnecting) this.connect()
      return
    }
    this.internalSubscribe(destination, callback)
  }

  private internalSubscribe(destination: string, callback: (msg: any) => void) {
    if (!this.client || !this.connected) return

    if (!this.subscriptions[destination]) {
      const stompSub = this.client.subscribe(destination, (message: IMessage) => {
        let parsed
        try {
          parsed = JSON.parse(message.body)
        } catch {
          parsed = message.body
        }
        this.subscriptions[destination]?.callbacks.forEach((cb) => cb(parsed))
      })

      this.subscriptions[destination] = {
        stompSub,
        callbacks: new Set([callback]),
      }
    } else {
      this.subscriptions[destination].callbacks.add(callback)
    }
  }

  unsubscribe(destination: string) {
    const sub = this.subscriptions[destination]
    if (sub) {
      sub.stompSub.unsubscribe()
      delete this.subscriptions[destination]
    }
    if (this.pendingSubscriptions[destination]) {
      delete this.pendingSubscriptions[destination]
    }
  }

  sendMessage(destination: string, message: any) {
    const body = JSON.stringify(message)

    if (!this.client || !this.connected) {
      // console.warn("🚨 Shard offline. Queuing message...");
      this.messageQueue.enqueue({ destination, body })
      if (!this.isConnecting) this.connect()
      return
    }

    this.client.publish({
      destination,
      body,
    })
  }

  disconnect() {
    if (this.client) {
      this.client.deactivate()
      this.connected = false
      this.isConnecting = false
      this.subscriptions = {}
      this.pendingSubscriptions = {}
      this.messageQueue.clear()
    }
  }

  isConnected() {
    return this.connected
  }
}

// Create a Connection Pool with 2 shards (can be increased for higher load)
// This effectively uses scaling.ts (hashing) and connectionPool.ts (management)
export const websocketService = new ConnectionPool<WebSocketService>(() => new WebSocketService(), {
  poolSize: 2,
})
