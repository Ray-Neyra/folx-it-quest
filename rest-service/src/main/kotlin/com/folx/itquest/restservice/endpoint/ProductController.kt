package com.folx.itquest.restservice.endpoint

import com.folx.itquest.restservice.mapper.ProductToProductResponse
import com.folx.itquest.restservice.protocol.ProductCreateRequest
import com.folx.itquest.restservice.protocol.ProductResponse
import com.folx.itquest.restservice.protocol.ProductUpdateRequest
import com.folx.itquest.restservice.service.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    fun createProduct(@Valid @RequestBody createRequest: ProductCreateRequest): ResponseEntity<Long> {
        val productId = productService.createProduct(createRequest)
        return ResponseEntity.created(URI.create("/api/products/$productId")).body(productId)
    }

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable("productId") productId: Long,
        @Valid @RequestBody updateRequest: ProductUpdateRequest
    ) {
        productService.updateProduct(productId, updateRequest)
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(@PathVariable("productId") productId: Long) {
        productService.deleteProduct(productId)
    }

    @GetMapping("/{productId}")
    fun getProduct(@PathVariable("productId") productId: Long): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(ProductToProductResponse.INSTANCE.convert(
                productService.findById(productId)))
    }

    @GetMapping("/name/{productName}")
    fun getProductNyName(@PathVariable("productName") productName: String): ResponseEntity<ProductResponse> {
        return ResponseEntity.ok(ProductToProductResponse.INSTANCE.convert(
                productService.findByName(productName)))
    }
}
