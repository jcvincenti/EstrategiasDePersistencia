package ar.edu.unq.eperdemic.modelo

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
class Vector() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @ManyToOne
    var nombreDeLocacionActual: Ubicacion? = null

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var especies: MutableList<Especie> = mutableListOf()
    var tipo: TipoDeVectorEnum? = null

    constructor(nombreDeLocacionActual: Ubicacion) : this() {
        this.nombreDeLocacionActual = nombreDeLocacionActual
    }

    fun infectar(especie: Especie) = this.especies.add(especie)

    fun estaInfectado() = this.especies.isNotEmpty()

}



