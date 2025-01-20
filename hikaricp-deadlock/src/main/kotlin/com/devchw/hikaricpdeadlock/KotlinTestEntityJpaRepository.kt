package com.devchw.hikaricpdeadlock

import org.springframework.data.jpa.repository.JpaRepository

interface KotlinTestEntityJpaRepository : JpaRepository<KotlinTestEntity, Long>