package io.devchw.ratelimiter

import java.time.Instant
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author DevCHW
 * @since 2025-06-09
 *
 * 슬라이딩 윈도 로깅 기반 Rate Limiter
 * @param limit 윈도우 내 허용 요청 수
 * @param windowSizeMillis 윈도우 크기 (밀리초)
 */
class SlidingWindowLogRateLimiter(
    private val limit: Int,
    private val windowSizeMillis: Long
) : RateLimiter {
    private val requestTimestamps = ConcurrentLinkedQueue<Long>()

    /**
     * 요청 허용 여부 확인
     */
    @Synchronized
    override fun allowRequest(): Boolean {
        val now = Instant.now().toEpochMilli()

        // 윈도우 밖 요청 제거
        while (requestTimestamps.peek() != null &&
            now - requestTimestamps.peek() > windowSizeMillis
        ) {
            requestTimestamps.poll()
        }

        return if (requestTimestamps.size < limit) {
            requestTimestamps.add(now)
            true
        } else {
            false
        }
    }
}