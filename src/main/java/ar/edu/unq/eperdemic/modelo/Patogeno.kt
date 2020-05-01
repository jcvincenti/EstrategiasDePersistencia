package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import javax.persistence.*
import kotlin.random.Random

@Entity
class Patogeno() : Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int? = null
    var cantidadDeEspecies: Int = 0
    var defensa: Int = 0
    var letalidad: Int = 0
    var tipo: String? = null
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "vector")
    @Column(name = "capacidad_de_contagio")
    var capacidadDeContagio: MutableMap<TipoDeVectorEnum, Int> = mutableMapOf(TipoDeVectorEnum.Persona to 0,
            TipoDeVectorEnum.Animal to 0,
            TipoDeVectorEnum.Insecto to 0)

    constructor(tipo: String) : this() {
        this.tipo = tipo
    }

    fun crearEspecie(nombreEspecie: String, paisDeOrigen: String) : Especie{
        cantidadDeEspecies++
        return Especie(this, nombreEspecie, paisDeOrigen)
    }

    fun getCapacidadDeContagio(tipoVector: TipoDeVectorEnum) : Int? {
        return this.capacidadDeContagio[tipoVector]
    }

    fun setCapacidadDeContagio(tipoVector: TipoDeVectorEnum, capacidad: Int) {
        this.capacidadDeContagio.replace(tipoVector, capacidad)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Patogeno

        if (tipo != other.tipo) return false
        if (id != other.id) return false
        if (cantidadDeEspecies != other.cantidadDeEspecies) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tipo.hashCode()
        result = 31 * result + (id ?: 0)
        result = 31 * result + cantidadDeEspecies
        return result
    }

}