package io.devchw.kotest.practice

import io.devchw.kotest.domain.support.CoreException
import io.devchw.kotest.domain.user.User
import io.devchw.kotest.domain.user.fixture.UserFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

class KotestPractice {

    // 테스트를 메서드로 정의하지 않아도 됨
    class MyFunSpec : FunSpec({
        test("my first test") {
            1 + 2 shouldBe 3
        }
    })

    // 중첩 테스트 편해짐
    class MyNestedTest : DescribeSpec({
        describe("an outer test") {
            it("first inner test") {
                1 + 2 shouldBe 3
            }

            it("second inner test") {
                3 + 4 shouldBe 7
            }
        }
    })

    @Nested
    inner class JunitUserCreateTest {
        @Test
        fun `20글자 이상인 이름으로 유저를 생성하면 CoreException이 발생한다`() {
            // given
            val name = "12345678901234567890"
            // when  & then
            assertThatThrownBy {
                UserFixture.get(name = name)
            }
                .isInstanceOf(CoreException::class.java)
                .hasMessage("이름은 20자 미만이어야 합니다.")
        }
    }

    // 제일 많이 쓰일듯 ??
    class MyBehaviorTest : BehaviorSpec({
        given("20글자 이상인 이름으로") {
            val name = "12345678901234567890"
            `when`("유저를 생성할 때") { // when은 코틀린 약어이므로, 백틱으로 감싸야 함
                UserFixture.get(name = name)
                then("CoreException 예외가 발생한다.") { // then 이후에는 그레이들 버그로, and 안됨
                    shouldThrow<CoreException> {
                        throw CoreException("이름은 20자 미만이어야 합니다.")
                    }
                }
            }
        }
    })

    // BehaviorSpec 통합테스트
    @SpringBootTest
    class BehaviorSpecIntegrationTest : BehaviorSpec({

    }) {
        override fun extensions() = listOf(
            SpringTestExtension(SpringTestLifecycleMode.Root) // Kotest Spring Extension 라이브러리 필요함.
        )
    }
}