package com.folx.itquest.restservice.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ProductStatusValidator::class])
annotation class ProductStatusValidation(
    val message: String = "Product status can not be WITHDRAWN",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)