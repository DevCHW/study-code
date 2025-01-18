package io.hhplus.cleanarchitecture.hikaricpdeadlock

import jakarta.persistence.*

@Entity
@Table(name = "hikari_test")
class TestEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
) {

}