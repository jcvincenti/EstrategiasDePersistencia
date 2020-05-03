package ar.edu.unq.eperdemic.modelo

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Max

@Entity
class FactorDeContagio() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    @Max(100)
    var factorDeContagio: Int = 0
    var tipo: TipoDeVectorEnum? = null

    constructor(tipo: TipoDeVectorEnum): this() {
        this.tipo = tipo
    }
}