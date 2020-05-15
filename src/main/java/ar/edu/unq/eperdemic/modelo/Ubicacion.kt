package ar.edu.unq.eperdemic.modelo


import javax.persistence.*

@Entity
class Ubicacion() {
    @Id
    @Column(name = "nombre_ubicacion", nullable = false, unique = true)
    var nombreUbicacion: String? = null

    @OneToMany(mappedBy = "nombreDeLocacionActual", targetEntity = Vector::class, fetch = FetchType.EAGER)
    var vectores: MutableSet<Vector> = HashSet()

    constructor(nombreUbicacion: String) : this() {
        this.nombreUbicacion = nombreUbicacion
    }

    fun vectoresTotales(): Int = this.vectores.size

    fun vectoresInfectados(): Int {
        return this.vectores.count {
            it.estaInfectado()
        }
    }

    fun especieConMasVectoresInfectados(): String {
        val especies = this.vectores.flatMap { it.especies }

        if (especies.isNotEmpty()) {
            return especies.groupingBy { it }
                    .eachCount()
                    .maxBy { it.value }!!
                    .key.nombre!!
        }
        return ""
    }
}