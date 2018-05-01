package com.folx.itquest.restservice.domain.error.exception

import com.folx.itquest.restservice.domain.error.ErrorCodes

class ProductConcurrentModificationException(message: String)
    : ProductException(ErrorCodes.PRODUCT_CONCURRENT_MODIFICATION_ERROR_CODE, message)