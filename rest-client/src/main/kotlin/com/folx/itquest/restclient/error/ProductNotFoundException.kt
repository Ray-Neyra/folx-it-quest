package com.folx.itquest.restclient.error

class ProductNotFoundException(message: String?, throwable: Throwable) : ProductClientException(message, throwable)