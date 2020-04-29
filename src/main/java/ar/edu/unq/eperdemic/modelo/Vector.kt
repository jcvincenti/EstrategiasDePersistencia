package ar.edu.unq.eperdemic.modelo

import javax.persistence.*
import kotlin.jvm.Transient

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

    fun estaInfectado() = this.especies.isNotEmpty()

}



