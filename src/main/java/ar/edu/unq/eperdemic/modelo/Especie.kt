package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Especie() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var nombre: String? = null
    @Column(name = "pais_de_origen")
    var paisDeOrigen: String? = null
    @ManyToOne(cascade = [CascadeType.ALL])
    var patogeno: Patogeno? = null

    @ManyToMany(mappedBy = "especies")
    var vectores: MutableSet<Vector> = HashSet()

    constructor(patogeno: Patogeno, nombreEspecie: String, paisDeOrigen: String) : this() {
        this.patogeno = patogeno
        this.nombre = nombreEspecie
        this.paisDeOrigen = paisDeOrigen
    }

    fun getCapacidadDeContagio(tipoVector: TipoDeVectorEnum) : Int? {
        return this.patogeno!!.getCapacidadDeContagio(tipoVector)
    }
}