package com.folx.itquest.restclient.error

class ProductConcurrentModificationException(message: String?, throwable: Throwable)
    : ProductClientException(message, throwable)