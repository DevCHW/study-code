package io.hhplus.cleanarchitecture.hikaricpdeadlock

import com.zaxxer.hikari.HikariDataSource
import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.ProductEntity
import io.hhplus.cleanarchitecture.hikaricpdeadlock.facade.OrderFacade
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.OrderEntityJpaRepository
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.ProductEntityJpaRepository
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.StockEntityJpaRepository
import io.hhplus.cleanarchitecture.support.concurrent.ConcurrencyTestUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ConcurrentTestServiceTest(
    @Autowired
    private val concurrentTestService: ConcurrentTestService,
    @Autowired
    private val productEntityJpaRepository: ProductEntityJpaRepository,
) {

    @Test
    fun deadlock() {
        // given
        val product = productEntityJpaRepository.save(
            ProductEntity(
                name = "나이키 신발",
            )
        )

        val action = {
            concurrentTestService.hello(product.id)
        }

        ConcurrencyTestUtils.executeConcurrently(100, action)
    }
}