package com.folx.itquest.restclient.protocol

import java.math.BigDecimal

data class ProductUpdateRequest(
    val version: Int,
    val name: String,
    val price: BigDecimal,
    val status: ProductStatus
)