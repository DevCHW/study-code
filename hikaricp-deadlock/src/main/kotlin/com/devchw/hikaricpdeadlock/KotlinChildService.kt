package com.devchw.hikaricpdeadlock

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class KotlinChildService(
    private val testEntityJpaRepository: KotlinTestEntityJpaRepository,
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveTestEntity() {
        testEntityJpaRepository.save(KotlinTestEntity(name = "hello world!"))
    }
}