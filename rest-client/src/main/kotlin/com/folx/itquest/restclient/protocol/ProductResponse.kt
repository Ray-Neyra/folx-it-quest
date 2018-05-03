package com.folx.itquest.restclient.protocol

import java.math.BigDecimal

data class ProductResponse (
    var id: Long,
    var version: Int,
    var name: String,
    var price: BigDecimal,
    var status: ProductStatus
)