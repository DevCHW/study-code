package io.devchw.kotest.api.user

import io.devchw.kotest.domain.user.UserService
import io.devchw.kotest.support.ApiResponse
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val userService: UserService,
) {

    fun getUser(@RequestHeader("Authorization") userId: Long): ApiResponse<> {
        userService.get(userId)

    }
}