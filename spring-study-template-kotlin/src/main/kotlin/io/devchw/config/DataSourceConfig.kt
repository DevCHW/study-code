package io.devchw.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy

@Configuration
class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    fun hikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    fun dataSource(): HikariDataSource {
        return HikariDataSource(hikariConfig())
    }

    @Bean
    @Primary
    fun lazyDataSource(): LazyConnectionDataSourceProxy {
        return LazyConnectionDataSourceProxy(dataSource())
    }
}
