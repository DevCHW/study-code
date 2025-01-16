package io.hhplus.cleanarchitecture.hikaricpdeadlock.repository

import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductEntityJpaRepository : JpaRepository<ProductEntity, Long> {
}