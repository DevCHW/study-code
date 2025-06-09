package io.devchw.ratelimiter

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * @author DevCHW
 * @since 2025-06-09
 * Leaky Bucket (누출 버킷) 알고리즘 기반 Rate Limiter.
 *
 * ## 동작 원리
 * - 요청은 버킷에 물을 붓는 행위로 생각.
 * - 일정 속도로 물이 새어나감 (처리 속도).
 * - 버킷이 가득 차 있으면 요청 거부 (과도한 트래픽 방지).
 */
class LeakyBucketRateLimiter(
    private val capacity: Int,           // 버킷 용량 (최대 요청 수)
    private val leakRatePerSecond: Double // 초당 새는 속도 (처리 속도)
) : RateLimiter{
    private var water: Double = 0.0 // 현재 버킷에 담긴 요청 수 (물의 양)
    private var lastLeakTimestamp: Long = System.nanoTime() // 마지막 누출 시각
    private val lock = ReentrantLock() // 동기화를 위한 Lock

    /**
     * 요청이 허용 가능한지 확인하고 버킷에 요청을 추가한다.
     * 버킷이 넘치면 false를 반환하여 요청을 거부한다.
     */
    override fun allowRequest(): Boolean {
        lock.withLock {
            leak() // 먼저 누출 처리

            return if (water < capacity) {
                water += 1
                true
            } else {
                false
            }
        }
    }

    /**
     * 시간 경과에 따라 누적된 물을 일정 속도로 제거한다.
     * 초당 leakRatePerSecond 만큼 빠져나간다고 가정.
     */
    private fun leak() {
        val now = System.nanoTime()
        val elapsedSeconds = (now - lastLeakTimestamp) / 1_000_000_000.0
        val leakedAmount = elapsedSeconds * leakRatePerSecond

        if (leakedAmount > 0) {
            water = maxOf(0.0, water - leakedAmount)
            lastLeakTimestamp = now
        }
    }
}