package io.hhplus.cleanarchitecture.hikaricpdeadlock

import io.hhplus.cleanarchitecture.hikaricpdeadlock.service.ProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConcurrentTestService(
    private val productService: ProductService,
) {

    @Transactional
    fun hello(productId: Long) {
        productService.getById(1L)

    }
}