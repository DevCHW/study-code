package io.devchw.ratelimiter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class SlidingWindowLogRateLimiterTest {

    @Test
    fun `허용된 요청 수 이하일 경우 모두 허용된다`() {
        // given
        val rateLimiter = SlidingWindowLogRateLimiter(limit = 3, windowSizeMillis = 1000)

        // when
        val actual1 = rateLimiter.allowRequest()
        val actual2 = rateLimiter.allowRequest()
        val actual3 = rateLimiter.allowRequest()

        // then
        assertThat(actual1).isTrue
        assertThat(actual2).isTrue
        assertThat(actual3).isTrue
    }

    @Test
    fun `요청 수가 제한을 초과하면 거부된다`() {
        // given
        val rateLimiter = SlidingWindowLogRateLimiter(limit = 2, windowSizeMillis = 1000)

        // when
        val actual1 = rateLimiter.allowRequest()
        val actual2 = rateLimiter.allowRequest()
        val actual3 = rateLimiter.allowRequest()

        // then
        assertThat(actual1).isTrue
        assertThat(actual2).isTrue
        assertThat(actual3).isFalse
    }

    @Test
    fun `이전 요청이 만료되면 다시 요청이 허용된다`() {
        // given
        val rateLimiter = SlidingWindowLogRateLimiter(limit = 2, windowSizeMillis = 200)

        // when
        val actual1 = rateLimiter.allowRequest()
        val actual2 = rateLimiter.allowRequest()
        val actual3 = rateLimiter.allowRequest()

        Thread.sleep(200) // 기존 요청 만료될 때까지 대기
        val actual4 = rateLimiter.allowRequest()

        // then
        assertThat(actual1).isTrue
        assertThat(actual2).isTrue
        assertThat(actual3).isFalse // 초과
        assertThat(actual4).isTrue // 다시 허용
    }

    @Test
    fun `빠르게 여러 요청을 보낼 경우 제한이 적용된다`() {
        val rateLimiter = SlidingWindowLogRateLimiter(limit = 5, windowSizeMillis = 1000)

        val results = (1..10).map { rateLimiter.allowRequest() }
        val allowedCount = results.count { it }

        assertEquals(5, allowedCount)
    }

}