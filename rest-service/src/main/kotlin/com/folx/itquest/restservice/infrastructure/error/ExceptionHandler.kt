package com.folx.itquest.restservice.infrastructure.error

import com.folx.itquest.restservice.domain.error.ErrorCodes
import com.folx.itquest.restservice.domain.error.exception.ProductException
import com.folx.itquest.restservice.domain.error.exception.ProductNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.persistence.OptimisticLockException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ProductException::class)
    fun handleProductException(ex: ProductException): ResponseEntity<ErrorResponse> {
        logger.info("Handling ProductException. Message: {}", ex.message)
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse(ex.code, ex.message))
    }

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFoundException(ex: ProductNotFoundException): ResponseEntity<ErrorResponse> {
        logger.info("Handling ProductNotFoundException. Message: {}", ex.message)
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse(ex.code, ex.message))
    }

    @ExceptionHandler(OptimisticLockException::class, ConcurrencyFailureException::class)
    fun handleConcurrencyExceptions(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.info("Handling Concurrency modification exceptions. Message: {}", ex.message)
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse(ErrorCodes.PRODUCT_CONCURRENT_MODIFICATION_ERROR_CODE, ex.message))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ExceptionHandler::class.java)
    }
}