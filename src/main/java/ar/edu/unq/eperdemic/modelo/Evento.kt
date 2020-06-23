package ar.edu.unq.eperdemic.modelo

import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.BSONTimestamp
import java.time.LocalDateTime

class Evento() {
    @BsonProperty("id")
    val id: String? = null
    var timestamp: BSONTimestamp? = null
    var vectorId: Int? = null
    var nombreUbicacion: String? = null
    var tipoDePatogeno: String? = null
    var tipo: String? = null
    var especie: String? = null
    var especies: List<String>? = null
    var explicacion: String? = null

    constructor(vectorId: Int?, nombreUbicacion: String?, explicacion: String) : this() {
        this.vectorId = vectorId
        this.nombreUbicacion = nombreUbicacion
        this.tipo = "Arribo"
        this.explicacion = explicacion
        this.timestamp = BSONTimestamp()
    }

    constructor(tipoDePatogeno: String?, especie: String?, explicacion: String) : this() {
        this.tipoDePatogeno = tipoDePatogeno
        this.tipo = "Mutacion"
        this.especie = especie
        this.explicacion = explicacion
        this.timestamp = BSONTimestamp()
    }

    constructor(
            vectorId: Int?,
            nombreUbicacion: String?,
            tipoDePatogeno: String?,
            especie: String?,
            especies: List<String>?,
            explicacion: String
    ) : this() {
        this.vectorId = vectorId
        this.nombreUbicacion = nombreUbicacion
        this.tipoDePatogeno = tipoDePatogeno
        this.tipo = "Contagio"
        this.especie = especie
        this.especies = especies
        this.explicacion = explicacion
        this.timestamp = BSONTimestamp()
    }
}