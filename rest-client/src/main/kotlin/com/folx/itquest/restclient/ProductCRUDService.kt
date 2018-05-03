package com.folx.itquest.restclient

import com.folx.itquest.restclient.error.ProductClientException
import com.folx.itquest.restclient.error.ProductNameNotUniqueException
import com.folx.itquest.restclient.protocol.ProductCreateRequest
import com.folx.itquest.restclient.protocol.ProductStatus
import com.folx.itquest.restclient.protocol.ProductUpdateRequest
import com.folx.itquest.restclient.client.RestClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductCRUDService(private val restClient: RestClient) {

    fun getProductById(productId: Long) {
        restClient.getProductById(productId)
    }

    fun createProduct(createRequest: ProductCreateRequest): Long {
        return try {
            restClient.createProduct(createRequest)
        } catch (pce: ProductClientException) {
            when (pce) {
                is ProductNameNotUniqueException -> tryUpdateProduct(createRequest)
                else -> throw pce
            }
        }
    }

    fun updateProduct(updateRequest: ProductUpdateRequest, productId: Long) {
        return restClient.updateProduct(updateRequest, productId)
    }

    fun deleteProduct(productId: Long) {
        return restClient.deleteProduct(productId)
    }

    private fun tryUpdateProduct(createRequest: ProductCreateRequest): Long {
        logger.info("Try update product after attempt to create product {}", createRequest)
        val productFromServer = restClient.getProductByName(createRequest.name)
        val productId = productFromServer.id
        if (createRequest.price > productFromServer.price) {
            logger.info("Price in create request is higher than price from server: {} > {} " +
                    "Attempting to update product with id: {}",
                    createRequest.price, productFromServer.price, productId)
            val updateRequest = ProductUpdateRequest(productFromServer.version, createRequest.name, createRequest.price,
                    ProductStatus.getProductStatus(createRequest.status))
            restClient.updateProduct(updateRequest, productId)
            logger.info("Product with id: {} successfully updated", productId)
            return productId
        }
        logger.info("Price in create request is lower than price from server. {} < {} Not updating product with id: {}",
                createRequest.price, productFromServer.price, productId)
        return productId
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ProductCRUDService::class.java)
    }
}
