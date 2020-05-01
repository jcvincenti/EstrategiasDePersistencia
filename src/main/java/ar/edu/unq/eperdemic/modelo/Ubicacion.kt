package ar.edu.unq.eperdemic.modelo

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Ubicacion() {
    @Id
    @Column(name = "nombreUbicacion", nullable = false)
    var nombreUbicacion: String? = null

    constructor(nombreUbicacion: String) : this() {
        this.nombreUbicacion = nombreUbicacion
    }

}