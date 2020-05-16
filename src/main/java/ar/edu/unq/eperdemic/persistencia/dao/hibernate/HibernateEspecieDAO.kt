package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateEspecieDAO: HibernateDAO<Especie>(Especie::class.java), EspecieDAO {
    override fun cantidadUbicacionesDeEspecie(especieId: Int): Long {
        val session = TransactionRunner.currentSession
        val hql = (
                """
                    select count(*)
                    from Ubicacion u
                    join u.vectores v
                    join v.especies ve
                    where ve.id = :especieId
                """.trimIndent()
                )
        val query = session.createQuery(hql, Long::class.javaObjectType)
        query.setParameter("especieId", especieId)
        return query.singleResult
    }
}