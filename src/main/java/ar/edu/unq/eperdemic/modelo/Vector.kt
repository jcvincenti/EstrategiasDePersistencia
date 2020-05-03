package ar.edu.unq.eperdemic.modelo

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
class Vector() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    @ManyToOne
    @JoinColumn(name="nombre_de_locacion_actual")
    var nombreDeLocacionActual: Ubicacion? = null
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = "vector_especie",
            joinColumns = [JoinColumn(name = "especie_id")],
            inverseJoinColumns = [JoinColumn(name= "vector_id")])
    var especies: MutableList<Especie> = mutableListOf()
    @Column(name="tipo_de_vector")
    var tipo: TipoDeVectorEnum? = null

    constructor(nombreDeLocacionActual: Ubicacion) : this() {
        this.nombreDeLocacionActual = nombreDeLocacionActual
    }

    fun infectar(especie: Especie) = this.especies.add(especie)

    fun estaInfectado() = this.especies.isNotEmpty()

}



