package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateEspecieDAO: HibernateDAO<Especie>(Especie::class.java), EspecieDAO {
    override fun getLugaresPresenteEspecie(especieId: Int): List<Ubicacion> {
        val session = TransactionRunner.currentSession
        val hql = (
                """
                    select Ubicacion
                    from Ubicacion u
                    inner join u.vectores v
                """.trimIndent()
                )

        val query = session.createQuery(hql, Ubicacion::class.java)
        return query.resultList
    }
}