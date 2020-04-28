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
    @ManyToMany(cascade = [CascadeType.ALL])
    var especies: MutableSet<Especie> = HashSet()
    var tipo: String? = null
    @Transient
    var contagioStrategy: ContagioStrategy? = null

    constructor(nombreDeLocacionActual: String) : this() {
        this.nombreDeLocacionActual = nombreDeLocacionActual
    }

    fun infectar(especie: Especie) = this.especies.add(especie)

    fun puedeSerInfectadoPor(vector: Vector) : Boolean = contagioStrategy!!.puedeSerInfectadoPor(vector)
}



