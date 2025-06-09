package io.devchw.kotest.domain.user

import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    fun create(
        userCreate: UserCreate,
    ) {
        val user = userCreate.create()
        userRepository.save(user)
    }

    fun get(id: Long): User {
        return userRepository.findById(id)
    }

    fun delete(id: Long) {
        userRepository.delete(id)
    }

}