package com.folx.itquest.restservice.protocol

import com.folx.itquest.restservice.domain.ProductStatus
import java.math.BigDecimal

data class ProductResponse(
    var version: Int?,
    var name: String?,
    var price: BigDecimal?,
    var status: ProductStatus?
) {
    constructor() : this(null, null, null, null)
}
