package com.folx.itquest.restservice.domain.error.exception

import com.folx.itquest.restservice.domain.error.ErrorCodes

class ProductNameNotUniqueException(message: String)
    : ProductException(ErrorCodes.PRODUCT_NOT_UNIQUE_NAME_ERROR_CODE, message)