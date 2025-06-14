package io.devchw

import java.sql.Connection
import java.sql.DriverManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author DevCHW
 * @since 2025-06-14
 */
@Configuration
class ConnectionConfig {

    @Bean
    fun connection(): Connection {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3307/study",
            "application",
            "application"
        )
    }
}
