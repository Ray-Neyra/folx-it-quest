package com.folx.itquest.restservice.domain.error.exception

open class ProductException(val code: Int, message: String) : RuntimeException(message)