package com.study.lazyconnectiondatasourceproxy.domain;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public HikariDataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    @Primary
    public LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy() {
        return new LazyConnectionDataSourceProxy(dataSource());
    }

    @Bean
    public PlatformTransactionManager lazyConnectionDataSourceProxyTransactionManager() {
        return new DataSourceTransactionManager(lazyConnectionDataSourceProxy());
    }

    @Bean
    public PlatformTransactionManager defaultDataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

}
