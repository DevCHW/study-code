package io.devchw.kotest.domain.user.fixture

import io.devchw.kotest.domain.user.User
import java.time.LocalDateTime

object UserFixture {
    fun get(
        id: Long = 0L,
        name: String = "testUser",
        age: Int = 20,
        email: String = "test@example.com",
        createdAt: LocalDateTime = LocalDateTime.now(),
        updatedAt: LocalDateTime = LocalDateTime.now(),
        ): User {
        return User(
            id = id,
            name = name,
            age = age,
            email = email,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}