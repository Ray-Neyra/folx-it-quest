package com.folx.itquest.restservice.service

import com.folx.itquest.restservice.domain.Product
import com.folx.itquest.restservice.domain.ProductRepository
import com.folx.itquest.restservice.domain.ProductStatus
import com.folx.itquest.restservice.domain.error.exception.ProductConcurrentModificationException
import com.folx.itquest.restservice.domain.error.exception.ProductNameNotUniqueException
import com.folx.itquest.restservice.domain.error.exception.ProductNotFoundException
import com.folx.itquest.restservice.protocol.ProductCreateRequest
import com.folx.itquest.restservice.protocol.ProductUpdateRequest
import spock.lang.Specification

class ProductServiceTest extends Specification {

    def productRepository = Mock(ProductRepository)
    def productService = new ProductService(productRepository)

    def "Should create product"() {
        given:
        def request = createProductCreateRequest()

        when:
        def result = productService.createProduct(request)

        then:
        result == 1L

        and:
        productRepository.save(_ as Product) >> { arg ->
            def product = (Product) arg[0]
            if (product.name == Fields.PRODUCT_NAME && product.price == Fields.PRODUCT_PRICE
                    && product.status == Fields.PRODUCT_STATUS) {
                return createProductFromDB()
            }
        }
        productRepository.existsByName(Fields.PRODUCT_NAME) >> false
        0 * _
    }

    def "Should not create product with not unique name"() {
        given:
        def request = createProductCreateRequest()

        when:
        productService.createProduct(request)

        then:
        thrown(ProductNameNotUniqueException)

        and:
        productRepository.existsByName(Fields.PRODUCT_NAME) >> true
    }

    def "Should get product"() {
        given:
        def productId = Fields.PRODUCT_ID

        when:
        def product = productService.findById(productId)

        then:
        product.id == productId

        and:
        productRepository.findOne(productId) >> createProductFromDB()
        0 * _
    }

    def "Should not get Product"() {
        given:
        def productId = Fields.PRODUCT_ID

        when:
        productService.findById(productId)

        then:
        thrown(ProductNotFoundException)

        and:
        productRepository.findOne(productId) >> null
    }

    def "Should not update product with mismatch version"() {
        given:
        def request = createInvalidProductUpdateRequest()

        when:
        productService.updateProduct(Fields.PRODUCT_ID, request)

        then:
        thrown(ProductConcurrentModificationException)

        and:
        productRepository.findOne(Fields.PRODUCT_ID) >> createProductFromDB()
    }

    def "Should not update product with not unique name"() {
        given:
        def request = createProductUpdateRequest()

        when:
        productService.updateProduct(Fields.PRODUCT_ID, request)

        then:
        thrown(ProductNameNotUniqueException)

        and:
        productRepository.findOne(Fields.PRODUCT_ID) >> createProductFromDB()
        productRepository.existsByName(Fields.PRODUCT_UPDATE_NAME) >> true
    }

    ProductCreateRequest createProductCreateRequest() {
        return new ProductCreateRequest(Fields.PRODUCT_NAME, Fields.PRODUCT_PRICE, Fields.PRODUCT_STATUS)
    }

    ProductUpdateRequest createInvalidProductUpdateRequest() {
        return new ProductUpdateRequest(
                Fields.PRODUCT_BAD_VERSION,
                Fields.PRODUCT_UPDATE_NAME,
                Fields.PRODUCT_PRICE,
                Fields.PRODUCT_STATUS)
    }

    ProductUpdateRequest createProductUpdateRequest() {
        return new ProductUpdateRequest(
                Fields.PRODUCT_GOOD_VERSION,
                Fields.PRODUCT_UPDATE_NAME,
                Fields.PRODUCT_PRICE,
                Fields.PRODUCT_STATUS)
    }

    Product createProduct() {
        return new Product(name: Fields.PRODUCT_NAME, price: Fields.PRODUCT_PRICE, status: Fields.PRODUCT_STATUS)
    }

    Product createProductFromDB() {
        return new Product(
                id: Fields.PRODUCT_ID,
                name: Fields.PRODUCT_NAME,
                price: Fields.PRODUCT_PRICE,
                status: Fields.PRODUCT_STATUS,
                version: Fields.PRODUCT_GOOD_VERSION)
    }

    static class Fields {
        static PRODUCT_NAME = "Product"
        static PRODUCT_PRICE = new BigDecimal(123)
        static PRODUCT_STATUS = ProductStatus.IN_STOCK
        static PRODUCT_ID = 1L
        static PRODUCT_BAD_VERSION = 0
        static PRODUCT_GOOD_VERSION = 1

        static PRODUCT_UPDATE_NAME = "Updated Product"
    }
}
