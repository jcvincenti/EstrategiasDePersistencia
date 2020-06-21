package ar.edu.unq.eperdemic.persistencia.dao.mongo

import ar.edu.unq.eperdemic.modelo.Contagio

class MongoContagioDAO : GenericMongoDao<Contagio>(Contagio::class.java) {
}