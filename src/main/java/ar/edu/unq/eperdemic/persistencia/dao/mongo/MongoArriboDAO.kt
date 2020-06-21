package ar.edu.unq.eperdemic.persistencia.dao.mongo

import ar.edu.unq.eperdemic.modelo.Arribo
import com.mongodb.client.model.Filters.*

class MongoArriboDAO: GenericMongoDao<Arribo>(Arribo::class.java) {

    fun getArribosPorUbicacion(nombreUbicacionesLindantes: List<String>): List<Arribo> {
        return this.find(`in`("nombreUbicacionDestino", nombreUbicacionesLindantes))
    }
}