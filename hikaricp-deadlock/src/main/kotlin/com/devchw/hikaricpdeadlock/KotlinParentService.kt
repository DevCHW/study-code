package com.devchw.hikaricpdeadlock

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class KotlinParentService(
    private val childService: KotlinChildService,
) {

    @Transactional
    fun saveTestEntity() {
        childService.saveTestEntity()
    }
}