// strategies/scaling.ts

/**
 * Hash a destination string to a shard index in [0, shardCount)
 */
export function hashDestinationToShard(destination: string, shardCount: number): number {
  if (shardCount <= 1) return 0
  let hash = 0
  for (let i = 0; i < destination.length; i++) {
    hash = (hash * 31 + destination.charCodeAt(i)) | 0
  }
  const idx = Math.abs(hash) % shardCount
  return idx
}
