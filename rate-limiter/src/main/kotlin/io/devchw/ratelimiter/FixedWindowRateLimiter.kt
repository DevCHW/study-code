package io.devchw.ratelimiter

import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author DevCHW
 * @since 2025-06-09
 * 고정 윈도우 카운터 기반 Rate Limiter
 *
 * @param limit 윈도우당 허용 요청 수
 * @param windowSizeMillis 윈도우 크기 (밀리초 단위)
 */
class FixedWindowRateLimiter(
    private val limit: Int,
    private val windowSizeMillis: Long
) : RateLimiter {

    // 현재 윈도우 시작 시간
    @Volatile
    private var currentWindowStart: Long = Instant.now().toEpochMilli()

    // 현재 윈도우 내 요청 수
    private val requestCount = AtomicInteger(0)

    /**
     * 요청을 허용할 수 있는지 확인
     * @return true: 요청 허용, false: 거부
     */
    override fun allowRequest(): Boolean {
        val now = Instant.now().toEpochMilli()

        synchronized(this) {
            // 현재 윈도우가 지났는지 확인
            if (now - currentWindowStart >= windowSizeMillis) {
                // 새 윈도우 시작
                currentWindowStart = now
                requestCount.set(0)
            }

            return if (requestCount.get() < limit) {
                requestCount.incrementAndGet()
                true
            } else {
                false
            }
        }
    }
}