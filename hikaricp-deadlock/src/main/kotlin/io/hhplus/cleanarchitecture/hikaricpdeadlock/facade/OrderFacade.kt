package io.hhplus.cleanarchitecture.hikaricpdeadlock.facade

import com.zaxxer.hikari.HikariDataSource
import io.hhplus.cleanarchitecture.hikaricpdeadlock.service.OrderService
import io.hhplus.cleanarchitecture.hikaricpdeadlock.service.ProductService
import io.hhplus.cleanarchitecture.hikaricpdeadlock.service.StockService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderFacade(
    private val orderService: OrderService,
    private val productService: ProductService,
    private val stockService: StockService,
    private val hikariDataSource: HikariDataSource,
) {
    private val log = LoggerFactory.getLogger(OrderFacade::class.java)

    /**
     * 주문
     * 유저 ID, 상품 ID, 수량을 받아 주문을 한다.
     */
    @Transactional
    fun order(userId: Long, productId: Long, quantity: Int) {
        // 상품 조회
        val product = productService.getById(productId)

        // 재고 차감
        val stock = stockService.getByProductId(product.id)
        stockService.decreaseStock(stock.id, quantity)

        // 주문 생성
        val order = orderService.createOrder(userId, productId)
    }
}