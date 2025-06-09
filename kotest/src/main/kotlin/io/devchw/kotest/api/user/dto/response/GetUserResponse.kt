package io.devchw.kotest.api.user.dto.response

import io.devchw.kotest.domain.user.User
import java.time.LocalDateTime

data class GetUserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val age: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun of(user: User): GetUserResponse {
            return GetUserResponse(
                id = user.id,
                email = user.email,
                name = user.name,
                age = user.age,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
            )
        }
    }
}
