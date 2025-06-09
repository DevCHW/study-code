package io.devchw.kotest.domain.user

data class UserCreate(
    val name: String,
    val email: String,
    val age: Int,
) {
    fun create(): User {
        return User.create(this)
    }
}