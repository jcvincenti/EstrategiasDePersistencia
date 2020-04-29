package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

open class HibernateUbicacionDAO: HibernateDAO<Ubicacion>(Ubicacion::class.java), UbicacionDAO {
    override fun recuperar(nombreUbicacion: String): Ubicacion {
        val session = TransactionRunner.currentSession
        return session.get(Ubicacion::class.java, nombreUbicacion)
    }
}