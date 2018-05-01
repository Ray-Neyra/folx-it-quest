package com.folx.itquest.restservice.validation

import com.folx.itquest.restservice.domain.ProductStatus
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ProductStatusValidator : ConstraintValidator<ProductStatusValidation, ProductStatus> {

    override fun initialize(constraintAnnotation: ProductStatusValidation?) {
    }

    override fun isValid(value: ProductStatus?, context: ConstraintValidatorContext?): Boolean {
        return value != ProductStatus.WITHDRAWN
    }
}
