package com.folx.itquest.restservice.protocol

import com.folx.itquest.restservice.domain.ProductStatus
import com.folx.itquest.restservice.validation.ProductStatusValidation
import org.hibernate.validator.constraints.NotBlank
import java.math.BigDecimal

data class ProductCreateRequest(

    @get:NotBlank
    val name: String,
    val price: BigDecimal,

    @get:ProductStatusValidation
    val status: ProductStatus
)
