package com.folx.itquest.restservice.mapper

import com.folx.itquest.restservice.domain.Product
import com.folx.itquest.restservice.protocol.ProductResponse
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import org.springframework.core.convert.converter.Converter

@Mapper
interface ProductToProductResponse : Converter<Product, ProductResponse> {

    override
    fun convert(product: Product): ProductResponse

    companion object {
        val INSTANCE: ProductToProductResponse = Mappers.getMapper(ProductToProductResponse::class.java)
    }
}