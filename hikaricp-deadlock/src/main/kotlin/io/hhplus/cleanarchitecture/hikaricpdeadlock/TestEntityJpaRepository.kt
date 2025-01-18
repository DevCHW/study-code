package io.hhplus.cleanarchitecture.hikaricpdeadlock

import org.springframework.data.jpa.repository.JpaRepository

interface TestEntityJpaRepository : JpaRepository<TestEntity, Long>