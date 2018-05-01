package com.folx.itquest.restservice.service

import com.folx.itquest.restservice.domain.Product
import com.folx.itquest.restservice.domain.ProductRepository
import com.folx.itquest.restservice.domain.error.exception.ProductConcurrentModificationException
import com.folx.itquest.restservice.domain.error.exception.ProductNameNotUniqueException
import com.folx.itquest.restservice.domain.error.exception.ProductNotFoundException
import com.folx.itquest.restservice.protocol.ProductCreateRequest
import com.folx.itquest.restservice.protocol.ProductUpdateRequest
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class ProductService(
    private val productRepository: ProductRepository
) {

    fun createProduct(createRequest: ProductCreateRequest): Long {
        verifyName(createRequest.name)
        val product = Product(name = createRequest.name, price = createRequest.price, status = createRequest.status)
        return productRepository.save(product).id
    }

    fun updateProduct(productId: Long, updateRequest: ProductUpdateRequest) {
        val product = findById(productId)
        verifyVersion(updateRequest.version, product.version)
        verifyName(product.name, updateRequest.name)
        product.name = updateRequest.name
        product.price = updateRequest.price
        product.status = updateRequest.status
    }

    fun deleteProduct(productId: Long) {
        val product = findById(productId)
        productRepository.delete(product)
    }

    fun findById(productId: Long): Product {
        return productRepository.findOne(productId)
                ?: throw ProductNotFoundException("Product with id: $productId not found")
    }

    fun verifyVersion(requestedVer: Int, entityVer: Int) {
        if (requestedVer != entityVer) {
            throw ProductConcurrentModificationException(
                    "Requested entity version: $requestedVer is not equal current entity version: $entityVer")
        }
    }

    fun verifyName(entityName: String, requestedName: String) {
        if (entityName != requestedName) {
            verifyName(requestedName)
        }
    }

    fun verifyName(requestedName: String) {
        if (productRepository.existsByName(requestedName)) {
            throw ProductNameNotUniqueException("In database exist product with requested name: $requestedName")
        }
    }
}
