// strategies/reconnect.ts

export type BackoffType = 'exponential' | 'linear'

export interface ReconnectOptions {
  baseDelayMs?: number // initial delay
  maxDelayMs?: number // cap delay
  multiplier?: number // for exponential
  jitterRatio?: number // 0..1, e.g., 0.2 -> +-20%
  backoffType?: BackoffType
}

/**
 * ReconnectStrategy manages delays between reconnect attempts using
 * exponential or linear backoff with optional jitter.
 */
export class ReconnectStrategy {
  private attempt = 0
  private readonly base: number
  private readonly max: number
  private readonly mult: number
  private readonly jitter: number
  private readonly type: BackoffType

  constructor(opts: ReconnectOptions = {}) {
    this.base = opts.baseDelayMs ?? 1000
    this.max = opts.maxDelayMs ?? 30000
    this.mult = opts.multiplier ?? 2
    this.jitter = Math.min(Math.max(opts.jitterRatio ?? 0.2, 0), 1)
    this.type = opts.backoffType ?? 'exponential'
  }

  nextDelayMs(): number {
    this.attempt += 1

    let delay = this.base
    if (this.type === 'exponential') {
      delay = this.base * Math.pow(this.mult, this.attempt - 1)
    } else {
      delay = this.base * this.attempt
    }

    delay = Math.min(delay, this.max)

    if (this.jitter > 0) {
      const jitterRange = delay * this.jitter
      const random = (Math.random() * 2 - 1) * jitterRange // [-range, +range]
      delay = Math.max(0, delay + random)
    }

    return Math.floor(delay)
  }

  reset() {
    this.attempt = 0
  }
}
