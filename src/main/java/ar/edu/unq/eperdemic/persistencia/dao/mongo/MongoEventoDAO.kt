package ar.edu.unq.eperdemic.persistencia.dao.mongo

import ar.edu.unq.eperdemic.modelo.Evento
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Indexes

class MongoEventoDAO: GenericMongoDao<Evento>(Evento::class.java) {

    fun logearEvento(evento: Evento) {
        this.startTransaction()
        this.save(evento)
        this.commit()
    }

    fun getFeedUbicacion(nombreUbicacion: String, nombreUbicacionesLindantes: List<String>): List<Evento> {
        val eventosFilter = or(
            and(
                `in`("nombreUbicacion", nombreUbicacionesLindantes),
                eq("tipo", "Arribo")
            ),
            and(
                eq("nombreUbicacion", nombreUbicacion),
                eq("tipo", "Contagio")
            )
        )

        val match = Aggregates.match(eventosFilter)
        val sort = Aggregates.sort(Indexes.descending("timestamp"))

        return aggregate(listOf(match, sort), Evento::class.java)
    }
}