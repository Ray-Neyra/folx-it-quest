package com.folx.itquest.restservice.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.hibernate.id.enhanced.SequenceStyleGenerator
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "product")
class Product(

    @Id
    @GenericGenerator(
            name = SEQUENCE_NAME,
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = arrayOf(Parameter(name = "sequence_name", value = SEQUENCE_NAME),
            Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "50"))
    )
    @GeneratedValue(generator = SEQUENCE_NAME, strategy = GenerationType.SEQUENCE)
    @Column(name = "product_id")
    var id: Long = 0L,

    @NotNull
    var name: String = "",

    @NotNull
    var price: BigDecimal = BigDecimal.ZERO,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    var status: ProductStatus = ProductStatus.WITHDRAWN

) : BaseEntity() {
    companion object {
        const val SEQUENCE_NAME = "product_seq"
    }
}
