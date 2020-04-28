package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

open class HibernateVectorDAO : HibernateDAO<Vector>(Vector::class.java), VectorDAO {
    override fun enfermedades(vectorId: Int) : List<Especie> {
        val session = TransactionRunner.currentSession
        val hql = ("select e from Especie e"
                + " inner join e.vectores ve "
                + "where ve.id = :vectorId ")

        val query = session.createQuery(hql, Especie::class.java)
        query.setParameter("vectorId", vectorId)

        return query.resultList
    }
}