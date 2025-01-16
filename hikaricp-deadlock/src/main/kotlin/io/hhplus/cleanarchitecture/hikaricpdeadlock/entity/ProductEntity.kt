package io.hhplus.cleanarchitecture.hikaricpdeadlock.entity

import jakarta.persistence.*

@Entity
@Table(name = "product")
class ProductEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String,
)