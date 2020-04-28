package ar.edu.unq.eperdemic.modelo

import javax.persistence.*

@Entity
class Especie() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @ManyToOne
    @JoinColumn(name = "patogeno_id")
    var patogeno: Patogeno? = null
    var nombre: String? = null
    var paisDeOrigen: String? = null

    @ManyToMany(mappedBy = "especies")
    var vectores: MutableSet<Vector> = HashSet()

    constructor(patogeno: Patogeno, nombreEspecie: String, paisDeOrigen: String) : this() {
        this.patogeno = patogeno
        this.nombre = nombreEspecie
        this.paisDeOrigen = paisDeOrigen
    }

    fun getCapacidadDeContagio(tipoVector: String) : Int? {
        return this.patogeno!!.getCapacidadDeContagio(tipoVector)
    }
}