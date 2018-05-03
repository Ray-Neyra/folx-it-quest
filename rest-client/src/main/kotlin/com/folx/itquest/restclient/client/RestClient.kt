package com.folx.itquest.restclient.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.folx.itquest.restclient.config.BasePathConfigurationProperty
import com.folx.itquest.restclient.error.ErrorCodes
import com.folx.itquest.restclient.error.ErrorResponse
import com.folx.itquest.restclient.error.ProductClientException
import com.folx.itquest.restclient.error.ProductConcurrentModificationException
import com.folx.itquest.restclient.error.ProductNameNotUniqueException
import com.folx.itquest.restclient.error.ProductNotFoundException
import com.folx.itquest.restclient.protocol.ProductCreateRequest
import com.folx.itquest.restclient.protocol.ProductResponse
import com.folx.itquest.restclient.protocol.ProductUpdateRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class RestClient(
    private val restTemplate: RestTemplate,
    basePathConfiguration: BasePathConfigurationProperty,
    private val objectMapper: ObjectMapper
) {

    val baseProductPath = basePathConfiguration.basePath + "/api/products"

    fun createProduct(productCreateRequest: ProductCreateRequest): Long {
        logger.info("Trying to create new product {}", productCreateRequest)
        val uri = UriComponentsBuilder.fromUriString(baseProductPath).build().toUri()
        try {
            val id = restTemplate.postForEntity(uri, productCreateRequest, Long::class.java).body
            logger.info("Product with id: {} created", id)
            return id
        } catch (ex: HttpClientErrorException) {
            val errorResponse = getErrorResponse(ex.responseBodyAsString)
            if (errorResponse?.code == ErrorCodes.PRODUCT_NOT_UNIQUE_NAME_ERROR_CODE) {
                logger.info("Handling product name not unique exception", ex)
                throw ProductNameNotUniqueException(errorResponse.message, ex)
            }
            logger.warn("Handling unknown http client exception", ex)
            throw ProductClientException("Unknown http client exception", ex)
        }
    }

    fun updateProduct(productUpdateRequest: ProductUpdateRequest, productId: Long) {
        logger.info("Trying to update product with id {}", productId)
        val uri = getBaseUriWithGivenProductId(productId)
        try {
            restTemplate.put(uri, productUpdateRequest)
            logger.info("Product with id {} updated", productId)
        } catch (ex: HttpClientErrorException) {
            val errorResponse = getErrorResponse(ex.responseBodyAsString)
            when {
                errorResponse?.code == ErrorCodes.PRODUCT_NOT_FOUND_ERROR_CODE -> {
                    logger.info("Handling product not found exception", ex)
                    throw ProductNotFoundException(errorResponse.message, ex)
                }
                errorResponse?.code == ErrorCodes.PRODUCT_NOT_UNIQUE_NAME_ERROR_CODE -> {
                    logger.info("Handling product name not unique exception", ex)
                    throw ProductNameNotUniqueException(errorResponse.message, ex)
                }
                errorResponse?.code == ErrorCodes.PRODUCT_CONCURRENT_MODIFICATION_ERROR_CODE -> {
                    logger.info("Handling product concurrent modification exception", ex)
                    throw ProductConcurrentModificationException(errorResponse.message, ex)
                }
                else -> {
                    logger.warn("Handling unknown http client exception", ex)
                    throw ProductClientException("Unknown http client exception", ex)
                }
            }
        }
    }

    fun deleteProduct(productId: Long) {
        logger.info("Trying to delete product with id {}", productId)
        val uri = getBaseUriWithGivenProductId(productId)
        try {
            restTemplate.delete(uri)
            logger.info("Product with id {} deleted", productId)
        } catch (ex: HttpClientErrorException) {
            val errorResponse = getErrorResponse(ex.responseBodyAsString)
            if (errorResponse?.code == ErrorCodes.PRODUCT_NOT_FOUND_ERROR_CODE) {
                logger.info("Handling product not found exception", ex)
                throw ProductNotFoundException(errorResponse.message, ex)
            }
            logger.warn("Handling unknown http client exception", ex)
            throw ProductClientException("Unknown http client exception", ex)
        }
    }

    fun getProductByName(productName: String): ProductResponse {
        logger.info("Trying to get Product with name: {}", productName)
        val uri = UriComponentsBuilder.fromUriString("$baseProductPath/name/$productName").build().toUri()
        try {
            val response = restTemplate.exchange(uri, HttpMethod.GET,
                    null, ProductResponse::class.java)
            return response.body
        } catch (ex: HttpClientErrorException) {
            val errorResponse = getErrorResponse(ex.responseBodyAsString)
            if (errorResponse?.code == ErrorCodes.PRODUCT_NOT_FOUND_ERROR_CODE) {
                logger.info("Handling product not found exception", ex)
                throw ProductNotFoundException(errorResponse.message, ex)
            }
            logger.warn("Handling unknown http client exception", ex)
            throw ProductClientException("Unknown http client exception", ex)
        }
    }

    fun getProductById(productId: Long): ProductResponse {
        logger.info("Trying to get Product with id: {}", productId)
        val uri = getBaseUriWithGivenProductId(productId)
        try {
            val response = restTemplate.exchange(uri, HttpMethod.GET,
                    null, ProductResponse::class.java)
            return response.body
        } catch (ex: HttpClientErrorException) {
            val errorResponse = getErrorResponse(ex.responseBodyAsString)
            if (errorResponse?.code == ErrorCodes.PRODUCT_NOT_FOUND_ERROR_CODE) {
                logger.info("Handling product not found exception", ex)
                throw ProductNotFoundException(errorResponse.message, ex)
            }
            logger.warn("Handling unknown http client exception", ex)
            throw ProductClientException("Unknown http client exception", ex)
        }
    }

    private fun getBaseUriWithGivenProductId(productId: Long) =
            UriComponentsBuilder.fromUriString("$baseProductPath/$productId").build().toUri()

    private fun getErrorResponse(responseBody: String): ErrorResponse? {
        try {
            return objectMapper.readValue(responseBody, ErrorResponse::class.java)
        } catch (e: Exception) {
        }
        return null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RestClient::class.java)
    }
}
