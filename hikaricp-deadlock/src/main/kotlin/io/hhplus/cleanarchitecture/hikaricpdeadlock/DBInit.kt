package io.hhplus.cleanarchitecture.hikaricpdeadlock

import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.ProductEntity
import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.StockEntity
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.ProductEntityJpaRepository
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.StockEntityJpaRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DBInit(
    private val productEntityJpaRepository: ProductEntityJpaRepository,
    private val stockEntityJpaRepository: StockEntityJpaRepository,
) {
    @Transactional
    @EventListener(ApplicationReadyEvent::class)
    fun init() {
        val product1 = productEntityJpaRepository.save(
            ProductEntity(
                name = "나이키 신발",
            )
        )

        val stock1 = stockEntityJpaRepository.save(
            StockEntity(
                productId = product1.id,
                quantity = 100,
            )
        )
    }
}