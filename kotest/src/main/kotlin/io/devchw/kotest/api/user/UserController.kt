package io.devchw.kotest.api.user

import io.devchw.kotest.api.user.dto.response.GetUserResponse
import io.devchw.kotest.domain.user.UserService
import io.devchw.kotest.support.ApiResponse
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {

    fun getUser(
        @RequestHeader("Authorization") userId: Long
    ): ApiResponse<GetUserResponse> {
        return ApiResponse.success(GetUserResponse.of(userService.get(userId)))
    }

}