package ar.edu.unq.eperdemic.modelo

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
class Vector() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var nombreDeLocacionActual: String? = null
    @ManyToMany(cascade = [CascadeType.ALL])
    var especies: MutableSet<Especie> = HashSet()
    @Transient
    var contagioStrategy: ContagioStrategy? = null
    var tipo: String? = null

    constructor(nombreDeLocacionActual: String) : this() {
        this.nombreDeLocacionActual = nombreDeLocacionActual
    }

    fun infectar(especie: Especie) {
        especies.add(especie)
        // juan implementa esto bien
    }

    fun puedeSerInfectadoPor(vector: Vector) : Boolean = contagioStrategy!!.puedeSerInfectadoPor(vector)
}

abstract class ContagioStrategy {
    abstract fun puedeSerInfectadoPor(vector: Vector) : Boolean
}

class Animal : ContagioStrategy() {
    override fun puedeSerInfectadoPor(vector: Vector) : Boolean {
        return listOf("Insecto").contains(vector.tipo!!)
    }
}

class Persona  : ContagioStrategy() {
    override fun puedeSerInfectadoPor(vector: Vector) : Boolean {
        return listOf("Humano", "Insecto", "Animal").contains(vector.tipo!!)
    }
}

class Insecto  : ContagioStrategy() {
    override fun puedeSerInfectadoPor(vector: Vector) : Boolean {
        return listOf("Humano", "Animal").contains(vector.tipo!!)
    }
}



