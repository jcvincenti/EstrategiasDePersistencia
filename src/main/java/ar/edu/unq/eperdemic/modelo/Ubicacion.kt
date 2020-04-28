package ar.edu.unq.eperdemic.modelo

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Ubicacion() {
    @Id var nombreUbicacion: String? = null

    constructor(nombreUbicacion: String) : this() {
        this.nombreUbicacion = nombreUbicacion
    }

}