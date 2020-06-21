package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Evento
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.mongo.MongoArriboDAO
import ar.edu.unq.eperdemic.persistencia.dao.mongo.MongoContagioDAO
import ar.edu.unq.eperdemic.services.FeedService

class FeedServiceImpl() : FeedService {

    val arriboDAO = MongoArriboDAO()
    val contagioDAO = MongoContagioDAO()
    val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())

    override fun feedPatogeno(tipoDePatogeno: String): List<Evento> {
        TODO("Not yet implemented")
    }

    override fun feedVector(vectorId: Int): List<Evento> {
        TODO("Not yet implemented")
    }

    override fun feedUbicacion(nombreDeUbicacion: String): List<Evento> {
        val nombreUbicacionesLindantes = ubicacionService.conectados(nombreDeUbicacion).map { it.nombreUbicacion }
        val arriboEventos = arriboDAO.getArribosPorUbicacion(nombreUbicacionesLindantes)
        val contagioEventos = contagioDAO.findEq("nombreUbicacion", nombreDeUbicacion)
        return listOf(arriboEventos, contagioEventos).flatten()
    }
}