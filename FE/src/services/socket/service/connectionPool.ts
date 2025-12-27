// service/connectionPool.ts
import { hashDestinationToShard } from '../strategies/scaling'

export interface IWebSocketLike {
  connect(cb?: () => void): void
  subscribe(destination: string, cb: (msg: any) => void): void
  unsubscribe(destination: string): void
  sendMessage(destination: string, message: any): void
  disconnect(): void
  isConnected(): boolean
}

export interface ConnectionPoolOptions {
  poolSize?: number
}

export class ConnectionPool<T extends IWebSocketLike> implements IWebSocketLike {
  private readonly size: number
  private readonly factory: () => T
  private readonly clients: T[]

  constructor(factory: () => T, opts: ConnectionPoolOptions = {}) {
    this.size = Math.max(1, opts.poolSize ?? 1)
    this.factory = factory
    this.clients = Array.from({ length: this.size }, () => this.factory())
  }

  /**
   * Helper: Get the specific client responsible for this destination
   */
  private getClientForDestination(destination: string): T {
    const idx = hashDestinationToShard(destination, this.size)
    return this.clients[idx]
  }

  // --- IWebSocketLike Implementation ---

  connect(cb?: () => void) {
    this.connectAll(cb)
  }

  subscribe(destination: string, cb: (msg: any) => void) {
    const client = this.getClientForDestination(destination)
    client.subscribe(destination, cb)
  }

  unsubscribe(destination: string) {
    const client = this.getClientForDestination(destination)
    client.unsubscribe(destination)
  }

  sendMessage(destination: string, message: any) {
    const client = this.getClientForDestination(destination)
    client.sendMessage(destination, message)
  }

  disconnect() {
    this.disconnectAll()
  }

  isConnected(): boolean {
    // Return true if ALL shards are connected appropriate for a pool, 
    // or at least one. "All" is safer for consistency.
    return this.clients.every(c => c.isConnected())
  }

  // --- Internal Pool Management ---

  private connectAll(cb?: () => void) {
    let remaining = this.clients.length
    if (remaining === 0) {
      if (cb) cb()
      return
    }

    const done = () => {
      remaining -= 1
      if (remaining <= 0 && cb) cb()
    }
    this.clients.forEach((c) => c.connect(done))
  }

  private disconnectAll() {
    this.clients.forEach((c) => c.disconnect())
  }
}

