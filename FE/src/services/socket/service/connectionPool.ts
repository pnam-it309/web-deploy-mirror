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

export class ConnectionPool<T extends IWebSocketLike> {
  private readonly size: number
  private readonly factory: () => T
  private readonly clients: T[]

  constructor(factory: () => T, opts: ConnectionPoolOptions = {}) {
    this.size = Math.max(1, opts.poolSize ?? 1)
    this.factory = factory
    this.clients = Array.from({ length: this.size }, () => this.factory())
  }

  getClientForDestination(destination: string): T {
    const idx = hashDestinationToShard(destination, this.size)
    return this.clients[idx]
  }

  connectAll(cb?: () => void) {
    let remaining = this.clients.length
    const done = () => {
      remaining -= 1
      if (remaining <= 0 && cb) cb()
    }
    this.clients.forEach((c) => c.connect(done))
  }

  disconnectAll() {
    this.clients.forEach((c) => c.disconnect())
  }
}
