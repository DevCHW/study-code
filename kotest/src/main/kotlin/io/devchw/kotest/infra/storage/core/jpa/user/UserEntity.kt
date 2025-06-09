package io.devchw.kotest.infra.storage.core.jpa.user

import io.devchw.kotest.domain.user.User
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

    @Column(name = "email")
    val email: String,
) : BaseEntity() {

    fun toDomain(): User {
        return User(
            id = id,
            name = name,
            age = age,
            email = email,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    companion object {
        fun of(user: User): UserEntity {
            return UserEntity(
                name = user.name,
                age = user.age,
                email = user.email,
            )
        }
    }
}