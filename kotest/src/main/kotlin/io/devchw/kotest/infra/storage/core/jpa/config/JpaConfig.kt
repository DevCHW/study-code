package io.devchw.kotest.infra.storage.core.jpa.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableJpaRepositories(basePackages = ["io.devchw.kotest.infra.storage.core"])
class JpaConfig {
    @Bean
    fun transactionManager(): PlatformTransactionManager {
        return JpaTransactionManager()
    }
}