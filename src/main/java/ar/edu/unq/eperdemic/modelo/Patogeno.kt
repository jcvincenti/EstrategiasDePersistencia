package ar.edu.unq.eperdemic.modelo

import java.io.Serializable
import javax.persistence.*

@Entity
class Patogeno() : Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int? = null
    @Column(name = "cantidad_de_especies")
    var cantidadDeEspecies: Int = 0
    var defensa: Int = 0
    var letalidad: Int = 0
    var tipo: String? = null

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = "patogeno_factor_de_contagio",
            joinColumns = [JoinColumn(name = "patogeno_id")],
            inverseJoinColumns = [JoinColumn(name= "factor_de_contagio_id")])
    var capacidadDeContagio: MutableList<FactorDeContagio> = mutableListOf(FactorDeContagio(TipoDeVectorEnum.Persona),
            FactorDeContagio(TipoDeVectorEnum.Insecto),
            FactorDeContagio(TipoDeVectorEnum.Animal))

    constructor(tipo: String) : this() {
        this.tipo = tipo
    }

    fun crearEspecie(nombreEspecie: String, paisDeOrigen: String) : Especie{
        cantidadDeEspecies++
        return Especie(this, nombreEspecie, paisDeOrigen)
    }

    fun getCapacidadDeContagio(tipoVector: TipoDeVectorEnum) : Int? {
        return this.capacidadDeContagio.find{it.tipo == tipoVector}!!.factorDeContagio
    }

    fun setCapacidadDeContagio(tipoVector: TipoDeVectorEnum, capacidad: Int) {
        this.capacidadDeContagio.find{it.tipo == tipoVector}!!.factorDeContagio = capacidad
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