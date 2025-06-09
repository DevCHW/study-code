package io.devchw.kotest.domain.support

class CoreException(
    val errorMessage: String? = null,
) : RuntimeException(errorMessage)