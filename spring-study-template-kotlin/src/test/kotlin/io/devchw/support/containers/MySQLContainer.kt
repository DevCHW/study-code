package io.devchw.support.containers

import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

const val MYSQL_VERSION = "8.0"
const val INIT_SCRIPT_PATH = ""

object MySQLContainer {

    val mySqlContainer: MySQLContainer<*> = MySQLContainer(DockerImageName.parse("mysql:${MYSQL_VERSION}"))
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test")
        .apply {
            if (INIT_SCRIPT_PATH.isNotEmpty()) {
                withInitScript(INIT_SCRIPT_PATH)
            }
        }
        .waitingFor(Wait.forHttp("/")) // MySQL 컨테이너가 완전히 시작되고 요청을 처리할 준비가 될 때까지 대기하는 조건을 설정
        .withReuse(true)
}
