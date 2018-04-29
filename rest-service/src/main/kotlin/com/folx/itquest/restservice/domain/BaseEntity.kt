package com.folx.itquest.restservice.domain

import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
class BaseEntity(
    @Column(unique = true) var uuid: String = UUID.randomUUID().toString(),
    @Version var version: Int = 0
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseEntity

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}
