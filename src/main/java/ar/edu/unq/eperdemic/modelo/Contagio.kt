package ar.edu.unq.eperdemic.modelo

import org.bson.codecs.pojo.annotations.BsonProperty

class Contagio() : Evento() {
    @BsonProperty("id")
    val id: String? = null
    var vectorId: Int = 0
    lateinit var nombreUbicacion: String

    constructor(vectorId: Int, nombreUbicacion: String) : this() {
        this.vectorId = vectorId
        this.nombreUbicacion = nombreUbicacion
    }
}