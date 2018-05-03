package com.folx.itquest.restclient.protocol

enum class ProductStatus {
    IN_STOCK,
    OUT_OF_STOCK,
    WITHDRAWN;

    companion object {
        fun getProductStatus(statusCreateRequest: ProductStatusCreateRequest): ProductStatus {
            return when (statusCreateRequest) {
                ProductStatusCreateRequest.IN_STOCK -> IN_STOCK
                ProductStatusCreateRequest.OUT_OF_STOCK -> OUT_OF_STOCK
            }
        }
    }
}