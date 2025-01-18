package io.hhplus.cleanarchitecture.hikaricpdeadlock

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import kotlin.test.Test

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ParentServiceTest(
    private val parentService: ParentService,
) {

    @Test
    fun `TestEntity를 저장할 수 있다`() {
        parentService.saveTestEntity()
    }
}