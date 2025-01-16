package io.hhplus.cleanarchitecture.hikaricpdeadlock.entity

import jakarta.persistence.*

@Entity
@Table(name = "stock")
class StockEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "productId")
    val productId: Long,

    @Column(name = "stock")
    var quantity: Int,
) {

    fun decreaseQuantity(quantity: Int): StockEntity {
        if (this.quantity < quantity) {
            throw IllegalArgumentException("재고가 부족합니다.")
        }
        this.quantity -= quantity
        return this
    }
}