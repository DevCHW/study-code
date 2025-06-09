package io.devchw.kotest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class KotestApplication

fun main(args: Array<String>) {
    runApplication<KotestApplication>(*args)
}
