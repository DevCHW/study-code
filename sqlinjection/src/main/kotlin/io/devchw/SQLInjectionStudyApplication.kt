package io.devchw

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SQLInjectionStudyApplication

fun main(args: Array<String>) {
    runApplication<SQLInjectionStudyApplication>(*args)
}
