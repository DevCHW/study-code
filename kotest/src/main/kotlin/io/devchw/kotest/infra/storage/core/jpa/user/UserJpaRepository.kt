package io.devchw.kotest.infra.storage.core.jpa.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Long> {
}