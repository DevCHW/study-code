package io.devchw.ratelimiter

/**
 * @author DevCHW
 * @since 2025-06-09
 */
interface RateLimiter {

    fun allowRequest(): Boolean

}
