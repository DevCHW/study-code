package io.devchw.kotest.infra.storage.core.jpa

import io.devchw.kotest.domain.user.User
import io.devchw.kotest.domain.user.UserRepository
import io.devchw.kotest.infra.storage.core.jpa.user.UserEntity
import io.devchw.kotest.infra.storage.core.jpa.user.UserJpaRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserCoreRepository(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun findById(id: Long): User {
        return userJpaRepository.findByIdOrNull(id)?.toDomain() ?: throw EntityNotFoundException("유저 엔티티를 찾을 수 없습니다.")
    }

    override fun save(user: User): User {
        return userJpaRepository.save(UserEntity.of(user)).toDomain()
    }

    override fun delete(id: Long) {
        userJpaRepository.deleteById(id)
    }
}