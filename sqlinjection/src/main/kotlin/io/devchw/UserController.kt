package io.devchw

/**
 * @author DevCHW
 * @since 2025-06-14
 */
import java.sql.Connection
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val connection: Connection
) {

    /**
     * SQL Injection 취약 API
     */
    @GetMapping("/users")
    fun getUsers(@RequestParam id: String): List<Map<String, Any?>> {
        // 고의로 PreparedStatement를 사용하지 않음 (Injection 실습용)
        val sql = "SELECT * FROM users WHERE id = '$id'"  // <-- 취약

        println("sql = ${sql}") // 실행되는 SQL 확인
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery(sql)

        val response = mutableListOf<Map<String, Any?>>()
        while (rs.next()) {
            val data = mutableMapOf<String, Any?>()
            data["id"] = rs.getString(1)
            data["username"] = rs.getString(2)
            response.add(data)
        }
        return response
    }
}
