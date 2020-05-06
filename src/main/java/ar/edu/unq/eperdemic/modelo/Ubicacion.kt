package ar.edu.unq.eperdemic.modelo


import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Ubicacion() {
    @Id
    @Column(name = "nombre_ubicacion", nullable = false, unique = true)
    var nombreUbicacion: String? = null

    @OneToMany(mappedBy = "nombreDeLocacionActual", targetEntity = Vector::class)
    var vectores: List<Vector> = mutableListOf()

    constructor(nombreUbicacion: String) : this() {
        this.nombreUbicacion = nombreUbicacion
    }

}