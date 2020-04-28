package ar.edu.unq.eperdemic.modelo

import javax.persistence.*
import kotlin.collections.HashSet
import kotlin.jvm.Transient
import kotlin.random.Random

@Entity
class Vector() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var nombreDeLocacionActual: String? = null
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var especies: MutableList<Especie> = mutableListOf()
    @Transient
    var contagioStrategy: ContagioStrategy? = null
    var tipo: String? = null

    constructor(nombreDeLocacionActual: String) : this() {
        this.nombreDeLocacionActual = nombreDeLocacionActual
    }

    fun infectar(especie: Especie) = this.especies.add(especie)

    fun esContagioExitoso(factorDeContagio: Int) : Boolean {
        var esContagioExitoso: Boolean
        if (factorDeContagio > 50)
            esContagioExitoso = Random.nextInt(factorDeContagio-50, 100) < factorDeContagio
        else
            esContagioExitoso = Random.nextInt(1, 100) < factorDeContagio
        return esContagioExitoso
    }

    fun puedeSerInfectadoPor(vector: Vector) : Boolean = contagioStrategy!!.puedeSerInfectadoPor(vector)
}



