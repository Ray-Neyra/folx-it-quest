package com.folx.itquest.restservice.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

    fun existsByName(name: String): Boolean
    fun findByName(name: String): Product?
}
