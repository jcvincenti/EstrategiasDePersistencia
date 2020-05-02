package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

open class HibernateVectorDAO : HibernateDAO<Vector>(Vector::class.java), VectorDAO {
    override fun getVectoresByLocacion(nombreDeLocacionActual: String?) : List<Vector> {
        val session = TransactionRunner.currentSession
        val hql = ("from Vector v "
                + "where v.nombreDeLocacionActual.nombreUbicacion = :nombreDeLocacionActual ")

        val query = session.createQuery(hql, Vector::class.java)
        query.setParameter("nombreDeLocacionActual", nombreDeLocacionActual)

        return query.resultList
    }
}