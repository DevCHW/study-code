package io.hhplus.cleanarchitecture.hikaricpdeadlock.service

import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.StockEntity
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.StockEntityJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class StockService(
    private val stockEntityJpaRepository: StockEntityJpaRepository,
) {

    fun getByProductId(productId: Long): StockEntity {
        return stockEntityJpaRepository.findNullableByProductId(productId) ?: throw EntityNotFoundException("StockEntity not found")
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Transactional
    fun decreaseStock(stockId: Long, quantity: Int): StockEntity {
        val stock = stockEntityJpaRepository.findForUpdateById(stockId) ?: throw EntityNotFoundException("StockEntity not found")
        return stock.decreaseQuantity(quantity)
    }
}