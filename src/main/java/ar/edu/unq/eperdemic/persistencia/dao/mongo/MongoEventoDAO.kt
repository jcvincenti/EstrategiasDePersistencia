package ar.edu.unq.eperdemic.persistencia.dao.mongo

import ar.edu.unq.eperdemic.modelo.Evento
import com.mongodb.client.model.Filters.*

class MongoEventoDAO: GenericMongoDao<Evento>(Evento::class.java) {

    fun logearEvento(evento: Evento) {
        this.startTransaction()
        this.save(evento)
        this.commit()
    }

    fun getFeedUbicacion(nombreUbicacion: String, nombreUbicacionesLindantes: List<String>): List<Evento> {
        val eventos = this.find(
            or(
                and(
                    `in`("nombreUbicacion", nombreUbicacionesLindantes),
                    eq("tipo", "Arribo")
                ),
                and(
                    eq("nombreUbicacion", nombreUbicacion),
                    eq("tipo", "Contagio")
                )
            )
        )
        return eventos
    }
}