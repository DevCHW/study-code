package com.devchw.hikaricpdeadlock

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import kotlin.test.Test

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class KotlinParentServiceTest(
    private val parentService: KotlinParentService,
) {

    @Test
    fun `HikariCP_DeadLock_발생_테스트`() {
        parentService.saveTestEntity()
    }
}