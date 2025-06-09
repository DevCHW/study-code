package io.devchw.kotest.support

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T> private constructor(
    val data: T? = null,
    val error: Error? = null,
) {
    companion object {
        fun success(): ApiResponse<Any> {
            return ApiResponse( null)
        }

        fun <S> success(data: S): ApiResponse<S> {
            return ApiResponse(data)
        }
    }

    data class Error(
        val code: String,
        val message: String,
    )
}