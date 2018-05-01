package com.folx.itquest.restservice.protocol

import com.folx.itquest.restservice.domain.ProductStatus
import org.hibernate.validator.constraints.NotBlank
import java.math.BigDecimal

data class ProductUpdateRequest(

    val version: Int,

    @get:NotBlank
    val name: String,
    val price: BigDecimal,
    val status: ProductStatus
)