package io.devchw.kotest.infra.storage.core.jpa.member

import io.devchw.kotest.infra.storage.core.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "user")
class UserEntity(
    @Column(name = "name")
    val name: String,

    @Column(name = "age")
    val age: Int,
) : BaseEntity()