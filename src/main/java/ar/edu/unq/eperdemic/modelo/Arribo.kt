package ar.edu.unq.eperdemic.modelo

import org.bson.codecs.pojo.annotations.BsonProperty

class Arribo() : Evento() {
    @BsonProperty("id")
    val id: String? = null
    var vectorId: Int = 0
    lateinit var nombreUbicacionOrigen: String
    lateinit var nombreUbicacionDestino: String

    constructor(vectorId: Int, nombreUbicacionOrigen: String, nombreUbicacionDestino: String) : this() {
        this.vectorId = vectorId
        this.nombreUbicacionOrigen = nombreUbicacionOrigen
        this.nombreUbicacionDestino = nombreUbicacionDestino
    }
}