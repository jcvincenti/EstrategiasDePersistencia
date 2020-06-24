package ar.edu.unq.eperdemic.persistencia.dao.mongo

import ar.edu.unq.eperdemic.modelo.Evento
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Indexes
import org.bson.conversions.Bson

class MongoEventoDAO: GenericMongoDao<Evento>(Evento::class.java) {

    fun logearEvento(evento: Evento) {
        this.save(evento)
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
        return ordenarEventos(eventosFilter)
    }

    private fun ordenarEventos(filter: Bson): List<Evento> {
        val match = Aggregates.match(filter)
        val sort = Aggregates.sort(Indexes.descending("timestamp"))

        return aggregate(listOf(match, sort), Evento::class.java)
    }
}