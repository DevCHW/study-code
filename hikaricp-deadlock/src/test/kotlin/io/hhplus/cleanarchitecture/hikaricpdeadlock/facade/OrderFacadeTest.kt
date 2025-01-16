package io.hhplus.cleanarchitecture.hikaricpdeadlock.facade

import com.zaxxer.hikari.HikariDataSource
import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.ProductEntity
import io.hhplus.cleanarchitecture.hikaricpdeadlock.entity.StockEntity
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.OrderEntityJpaRepository
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.ProductEntityJpaRepository
import io.hhplus.cleanarchitecture.hikaricpdeadlock.repository.StockEntityJpaRepository
import io.hhplus.cleanarchitecture.support.concurrent.ConcurrencyTestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test

@SpringBootTest
class OrderFacadeTest(
    @Autowired private val orderFacade: OrderFacade,
    @Autowired private val productEntityJpaRepository: ProductEntityJpaRepository,
    @Autowired private val stockEntityJpaRepository: StockEntityJpaRepository,
    @Autowired private val orderEntityJpaRepository: OrderEntityJpaRepository,
    @Autowired private val hikariDataSource: HikariDataSource,
) {

    private val log = LoggerFactory.getLogger(OrderFacadeTest::class.java)
    @AfterEach
    fun cleanup() {
        productEntityJpaRepository.deleteAllInBatch()
        stockEntityJpaRepository.deleteAllInBatch()
        orderEntityJpaRepository.deleteAllInBatch()
    }

    @Test
    fun `주문이 완료되면 상품의 재고는 주문 수량만큼 차감되어야 한다`() {
        // given
        val product = productEntityJpaRepository.save(
            ProductEntity(
                name = "나이키 신발",
            )
        )

        val stock = stockEntityJpaRepository.save(
            StockEntity(
                productId = product.id,
                quantity = 100,
            )
        )

        val userId = 1L
        val orderQuantity = 1

        // when
        orderFacade.order(userId, product.id, orderQuantity)
        val findStock = stockEntityJpaRepository.findByIdOrNull(stock.id)!!

        // then
        Assertions.assertThat(findStock.quantity).isEqualTo(stock.quantity - orderQuantity)
    }

    @Test
    fun `동시에 주문이 50개 들어와도 재고가 순차적으로 차감되어야 한다`() {

        // given
        val product = productEntityJpaRepository.save(
            ProductEntity(
                name = "나이키 신발",
            )
        )

        val stock = stockEntityJpaRepository.save(
            StockEntity(
                productId = product.id,
                quantity = 100,
            )
        )



        val actions = mutableListOf<Runnable>()
        for (i in 1..9) {
            val userId = i.toLong()
            val orderQuantity = 1
            val action = Runnable {
                val hikariPoolMXBean = hikariDataSource.hikariPoolMXBean
                log.info("""
                    "전체 커넥션 수 : {}"
                    "놀고있는 커넥션 수 : {}" 
                    "대기중인 커넥션 수 : {}" 
                    "활성 커넥션 수 : {}"
                """, hikariPoolMXBean.totalConnections, hikariPoolMXBean.idleConnections, hikariPoolMXBean.threadsAwaitingConnection, hikariPoolMXBean.activeConnections)
//                log.info("전체 커넥션 수 : {}", hikariPoolMXBean.totalConnections)
//                log.info("놀고있는 커넥션 수 : {}", hikariPoolMXBean.idleConnections)
//                log.info("대기중인 커넥션 수 : {}", hikariPoolMXBean.threadsAwaitingConnection)
//                log.info("활성 커넥션 수 : {}", hikariPoolMXBean.activeConnections)
                orderFacade.order(userId, product.id, orderQuantity)
            }
            actions.add(action)
        }

        // when
        ConcurrencyTestUtils.executeConcurrently(actions)
    }

}