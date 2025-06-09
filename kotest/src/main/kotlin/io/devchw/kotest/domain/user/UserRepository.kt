package io.devchw.kotest.domain.user

interface UserRepository {
    fun findById(id: Long): User
    fun save(user: User): User
    fun delete(id: Long)
}