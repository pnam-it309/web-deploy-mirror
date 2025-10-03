// middleware/backpressure.ts

export type BackpressurePolicy = 'drop_oldest' | 'drop_latest' | 'reject'

export interface BackpressureOptions {
  highWaterMark?: number // max queued items
  policy?: BackpressurePolicy
}

export class BackpressureQueue<T> {
  private readonly max: number
  private readonly policy: BackpressurePolicy
  private queue: T[] = []

  constructor(opts: BackpressureOptions = {}) {
    this.max = Math.max(0, opts.highWaterMark ?? 500)
    this.policy = opts.policy ?? 'drop_oldest'
  }

  size() {
    return this.queue.length
  }

  isEmpty() {
    return this.queue.length === 0
  }

  enqueue(item: T): boolean {
    if (this.queue.length < this.max) {
      this.queue.push(item)
      return true
    }

    switch (this.policy) {
      case 'drop_oldest':
        this.queue.shift()
        this.queue.push(item)
        return true
      case 'drop_latest':
        // do nothing, reject the newest
        return false
      case 'reject':
        return false
      default:
        return false
    }
  }

  dequeue(): T | undefined {
    return this.queue.shift()
  }

  drain(maxItems: number): T[] {
    const n = Math.min(maxItems, this.queue.length)
    if (n <= 0) return []
    return this.queue.splice(0, n)
  }

  clear() {
    this.queue = []
  }
}

export interface TokenBucketOptions {
  capacity?: number // max tokens
  refillRatePerSec?: number // tokens/sec
}

export class TokenBucket {
  private readonly capacity: number
  private readonly refillPerSec: number
  private tokens: number
  private lastRefill: number

  constructor(opts: TokenBucketOptions = {}) {
    this.capacity = Math.max(1, opts.capacity ?? 50)
    this.refillPerSec = Math.max(0, opts.refillRatePerSec ?? 25)
    this.tokens = this.capacity
    this.lastRefill = Date.now()
  }

  private refill() {
    const now = Date.now()
    const deltaMs = now - this.lastRefill
    if (deltaMs <= 0) return

    const add = (deltaMs / 1000) * this.refillPerSec
    this.tokens = Math.min(this.capacity, this.tokens + add)
    this.lastRefill = now
  }

  tryRemove(cost = 1): boolean {
    this.refill()
    if (this.tokens >= cost) {
      this.tokens -= cost
      return true
    }
    return false
  }

  nextAvailabilityMs(cost = 1): number {
    this.refill()
    if (this.tokens >= cost || this.refillPerSec <= 0) return 0
    const missing = cost - this.tokens
    return Math.ceil((missing / this.refillPerSec) * 1000)
  }
}
