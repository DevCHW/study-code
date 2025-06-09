package io.devchw.ratelimiter

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * @author DevCHW
 * @since 2025-06-09
 *  Token Bucket 알고리즘 기반 처리율 제한기 (Rate Limiter).
 *
 * ## 동작 원리
 * - 일정 간격으로 토큰이 버킷에 추가된다 (초당 refillRate 만큼).
 * - 버킷의 최대 용량은 capacity로 제한된다.
 * - 요청이 들어오면 1개의 토큰을 소모하고 요청을 허용한다.
 * - 토큰이 부족하면 요청은 거부된다.
 * - 시간 경과에 따라 누적된 토큰을 계산해 버킷을 자동으로 충전한다.
 *
 * ## 예시 사용처
 * - API 요청 제한
 * - 외부 서비스 호출 빈도 제한
 * - 사용자별 속도 제한
 */
class TokenBucketRateLimiter(
    private val capacity: Int = 5,              // 최대 토큰 개수
    private val refillRate: Double = 1.0        // 초당 토큰 충전 속도
) : RateLimiter {
    private var tokens: Double = capacity.toDouble() // 현재 보유 중인 토큰 수
    private var lastRefillTimestamp: Long = System.nanoTime() // 마지막 토큰 충전 시각
    private val lock = ReentrantLock() // 멀티스레드 환경에서의 동기화용 Lock

    /**
     * 요청 허용 여부 반환 함수.
     * - 요청 시 토큰 1개를 소모한다.
     * - 토큰이 충분할 경우 true, 부족할 경우 false 반환.
     */
    override fun allowRequest(): Boolean {
        lock.withLock {
            refill()

            return if (tokens >= 1) {
                tokens -= 1
                true
            } else {
                false
            }
        }
    }

    /**
     * 현재 시각 기준으로 얼마나 시간이 지났는지를 바탕으로 버킷에 토큰을 채운다.
     * - 초당 refillRate만큼 토큰을 충전
     * - capacity를 초과하지 않도록 제한
     */
    private fun refill() {
        val now = System.nanoTime()
        val elapsedSeconds = (now - lastRefillTimestamp) / 1_000_000_000.0
        val tokensToAdd = elapsedSeconds * refillRate

        if (tokensToAdd > 0) {
            tokens = minOf(capacity.toDouble(), tokens + tokensToAdd)
            lastRefillTimestamp = now
        }
    }
}