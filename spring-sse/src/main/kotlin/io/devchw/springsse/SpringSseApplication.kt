package io.devchw.springsse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringSseApplication

fun main(args: Array<String>) {
    runApplication<SpringSseApplication>(*args)
}
