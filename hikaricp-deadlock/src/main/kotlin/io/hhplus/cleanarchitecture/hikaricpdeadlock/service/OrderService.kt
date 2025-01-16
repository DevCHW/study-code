package io.hhplus.cleanarchitecture.hikaricpdeadlock.service

import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.OrderEntity
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.OrderEntityJpaRepository
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.ProductEntityJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderEntityJpaRepository: OrderEntityJpaRepository,
) {

    @Transactional
    fun createOrder(userId: Long, productId: Long): OrderEntity {
        return orderEntityJpaRepository.save(
            OrderEntity(
                userId = userId,
                productId = productId,
            )
        )
    }
}