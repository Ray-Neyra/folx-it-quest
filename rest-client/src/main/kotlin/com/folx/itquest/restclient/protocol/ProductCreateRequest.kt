package com.folx.itquest.restclient.protocol

import java.math.BigDecimal

data class ProductCreateRequest (
    val name: String,
    val price: BigDecimal,
    val status: ProductStatusCreateRequest
)