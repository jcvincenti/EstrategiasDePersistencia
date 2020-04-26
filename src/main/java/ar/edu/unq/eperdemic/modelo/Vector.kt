package ar.edu.unq.eperdemic.modelo

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Vector() {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Int? = null
    var nombreDeLocacionActual: String? = null

    constructor(nombreDeLocacionActual: String) : this() {
        this.nombreDeLocacionActual = nombreDeLocacionActual
    }
}