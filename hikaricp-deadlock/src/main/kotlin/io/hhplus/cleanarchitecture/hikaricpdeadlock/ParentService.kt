package io.hhplus.cleanarchitecture.hikaricpdeadlock

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ParentService(
    private val childService: ChildService,
) {

    @Transactional
    fun saveTestEntity() {
        childService.saveTestEntity()
    }
}