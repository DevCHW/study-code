package io.devchw.kotest.api.user.dto.request

import io.devchw.kotest.domain.user.UserCreate

data class CreateUserRequest(
    val name: String,
    val email: String,
    val age: Int,
) {
    fun toDomain(): UserCreate {
        return UserCreate(
            age = age,
            name = name,
            email = email,
        )
    }
}
