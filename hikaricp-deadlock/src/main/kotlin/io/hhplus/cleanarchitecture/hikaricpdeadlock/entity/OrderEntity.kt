package io.hhplus.cleanarchitecture.hikaricpdeadlock.entity

import jakarta.persistence.*

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val userId: Long,
    val productId: Long,
) {
}