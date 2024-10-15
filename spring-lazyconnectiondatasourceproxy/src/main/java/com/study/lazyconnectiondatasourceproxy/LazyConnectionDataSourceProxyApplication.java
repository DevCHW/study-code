package com.study.lazyconnectiondatasourceproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LazyConnectionDataSourceProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LazyConnectionDataSourceProxyApplication.class, args);
	}

}
