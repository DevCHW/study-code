package io.devchw.support.containers

import jakarta.annotation.PreDestroy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

/**
 * Configuration class for managing test containers.
 */
@Configuration
class TestcontainersConfig {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @PreDestroy
    fun preDestroy() {
        try {
            if (mysqlContainer.isRunning) mysqlContainer.stop()
        } catch (e: Exception) {
            log.error("Error while stopping containers", e)
        }
    }

    companion object {

        private val mysqlContainer = MySQLContainer.mySqlContainer
            .apply { start() }

        init {
            // mysql
            System.setProperty(
                "spring.datasource.hikari.jdbc-url",
                mysqlContainer.jdbcUrl + "?characterEncoding=UTF-8&serverTimezone=UTC"
            )
            System.setProperty("spring.datasource.hikari.username", mysqlContainer.username)
            System.setProperty("spring.datasource.hikari.password", mysqlContainer.password)
            System.setProperty("spring.sql.init.mode", "never")
        }
    }
}
