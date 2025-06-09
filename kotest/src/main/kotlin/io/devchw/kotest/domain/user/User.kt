package io.devchw.kotest.domain.user

import io.devchw.kotest.domain.support.CoreException
import java.time.LocalDateTime

data class User(
    val id: Long,
    val name: String,
    val age: Int,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    init {
        require(age >= 0) {
            throw CoreException("나이는 0 이상이어야 합니다.")
        }

        require(name.length < 20) {
            throw CoreException("이름은 20자 미만이어야 합니다.")
        }
    }

    companion object {
        fun create(userCreate: UserCreate): User {
            return User(
                id = 0L,
                name = userCreate.name,
                age = userCreate.age,
                email = userCreate.email,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        }
    }
}