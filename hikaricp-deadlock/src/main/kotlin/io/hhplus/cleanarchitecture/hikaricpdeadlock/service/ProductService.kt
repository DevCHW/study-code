package io.hhplus.cleanarchitecture.hikaricpdeadlock.service

import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.ProductEntity
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.ProductEntityJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productEntityJpaRepository: ProductEntityJpaRepository,
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun getById(productId: Long): ProductEntity {
        return productEntityJpaRepository.findByIdOrNull(productId) ?: throw EntityNotFoundException("ProductEntity not found.")
    }
}