package io.hhplus.cleanarchitecture.hikaricpdeadlock.repository

import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.StockEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface StockEntityJpaRepository : JpaRepository<StockEntity, Long> {
    fun findNullableByProductId(productId: Long): StockEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findForUpdateById(stockId: Long): StockEntity?
}