package com.devchw.hikaricpdeadlock

import jakarta.persistence.*

@Entity
@Table(name = "kotlin_test_entity")
class KotlinTestEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
)