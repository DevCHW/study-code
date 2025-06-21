package io.devchw.support.utils

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong

/**
 * @author DevCHW
 * @since 2025-06-21
 */

data class ConcurrencyTestResult(
    val successCount: Long,
    val failureCount: Long,
)

/**
 * 하나의 동일한 작업을 여러 스레드에서 동시에 실행
 * @param count 실행할 스레드 수
 * @param action 실행할 작업
 */
inline fun runSameActionConcurrently(count: Int, crossinline action: () -> Any): ConcurrencyTestResult {
    val executorService = Executors.newFixedThreadPool(count)
    val startSignal = CountDownLatch(1)
    val doneSignal = CountDownLatch(count)

    val successCount = AtomicLong()
    val failureCount = AtomicLong()

    repeat(count) {
        executorService.submit {
            startSignal.await()
            try {
                action()
                successCount.incrementAndGet()
            } catch (e: Exception) {
                failureCount.incrementAndGet()
            } finally {
                doneSignal.countDown()
            }
        }
    }

    startSignal.countDown()
    doneSignal.await()
    executorService.shutdown()

    return ConcurrencyTestResult(
        successCount = successCount.get(),
        failureCount = failureCount.get(),
    )
}

/**
 * 여러 개의 서로 다른 작업을 동시에 실행
 * @param actions 실행할 작업들의 리스트
 */
fun runMultipleActionsConcurrently(vararg actions: () -> Any): ConcurrencyTestResult {
    val executorService = Executors.newFixedThreadPool(actions.size)
    val startSignal = CountDownLatch(1)
    val doneSignal = CountDownLatch(actions.size)

    val successCount = AtomicLong()
    val failureCount = AtomicLong()

    actions.forEach { action ->
        executorService.submit {
            startSignal.await()
            try {
                action()
                successCount.incrementAndGet()
            } catch (e: Exception) {
                failureCount.incrementAndGet()
            } finally {
                doneSignal.countDown()
            }
        }
    }

    startSignal.countDown()
    doneSignal.await()
    executorService.shutdown()

    return ConcurrencyTestResult(
        successCount = successCount.get(),
        failureCount = failureCount.get(),
    )
}
