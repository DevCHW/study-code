package io.hhplus.cleanarchitecture.hikaricpdeadlock.repository

import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderEntityJpaRepository: JpaRepository<OrderEntity, Long> {
}