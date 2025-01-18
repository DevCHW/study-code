package io.hhplus.cleanarchitecture.hikaricpdeadlock

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class ChildService(
    private val testEntityJpaRepository: TestEntityJpaRepository,
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveTestEntity() {
        testEntityJpaRepository.save(TestEntity(name = "hello world!"))
    }
}