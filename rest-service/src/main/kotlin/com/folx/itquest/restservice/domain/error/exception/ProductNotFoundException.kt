package com.folx.itquest.restservice.domain.error.exception

import com.folx.itquest.restservice.domain.error.ErrorCodes

class ProductNotFoundException(message: String) : ProductException(ErrorCodes.PRODUCT_NOT_FOUND_ERROR_CODE, message)