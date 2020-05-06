package ar.edu.unq.eperdemic.modelo

import ar.edu.unq.eperdemic.modelo.exceptions.VectorNoInfectadoException
import javax.persistence.*
import kotlin.random.Random

@Entity
class Vector() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "nombre_de_locacion_actual")
    var nombreDeLocacionActual: String? = null

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = "vector_especie",
            joinColumns = [JoinColumn(name = "especie_id")],
            inverseJoinColumns = [JoinColumn(name = "vector_id")])
    var especies: MutableList<Especie> = mutableListOf()

    @Column(name = "tipo_de_vector", nullable = false)
    var tipo: TipoDeVectorEnum? = null

    constructor(ubicacion: String) : this() {
        this.nombreDeLocacionActual = ubicacion
    }

    fun infectar(especie: Especie) {
        if (esContagioExitoso(especie.getCapacidadDeContagio(this.tipo!!)!!)) {
            this.especies.add(especie)
        }
    }

    fun estaInfectado() = this.especies.isNotEmpty()

    fun puedeSerInfectadoPor(tipo: TipoDeVectorEnum) = this.tipo!!.puedeSerInfectadoPor(tipo)

    fun contagiar(vector: Vector) {
        if (estaInfectado()) {
            if (vector.puedeSerInfectadoPor(tipo!!)) {
                especies.forEach { especie ->
                    vector.infectar(especie)
                }
            }
        } else {
            throw VectorNoInfectadoException("El vector no esta infectado")
        }
    }

    fun esContagioExitoso(factorDeContagio: Int) : Boolean {
        val factorDeContagioExitoso = factorDeContagio.plus(Random.nextInt(1, 10))
        return if (factorDeContagioExitoso > 50) {
            Random.nextInt(factorDeContagioExitoso-50, 100) < factorDeContagioExitoso
        } else {
            Random.nextInt(1, 100) < factorDeContagioExitoso
        }
    }

    fun puedeMoverse(ubicacion: Ubicacion?) : Boolean = nombreDeLocacionActual != ubicacion!!.nombreUbicacion

    fun moverse(ubicacion: String?) {
        this.nombreDeLocacionActual = ubicacion
    }

}



