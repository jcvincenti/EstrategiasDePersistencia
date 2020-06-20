package ar.edu.unq.eperdemic.modelo

import org.bson.codecs.pojo.annotations.BsonProperty

class Evento() {
    @BsonProperty("id")
    val id: String? = null
    var tipo: TipoDeEvento? = null
    var timeStamp: Long? = null
    var descripcion: String = ""

    constructor(tipo: TipoDeEvento, timeStamp: Long, descripcion: String) : this() {
        this.tipo = tipo
        this.timeStamp = timeStamp
        this.descripcion = descripcion
    }
}