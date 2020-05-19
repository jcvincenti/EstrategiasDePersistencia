package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVectorEnum
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

open class HibernatePatogenoDAO: HibernateDAO<Patogeno>(Patogeno::class.java), PatogenoDAO {

    //TODO: Ver despues si cambiamos la interface. Esto quedo del tp de JDBC
    override fun crear(patogeno: Patogeno): Int {
        guardar(patogeno)
        return patogeno.id
    }

    override fun recuperarATodos(): List<Patogeno> {
            val session = TransactionRunner.currentSession
            val hql = """
            select p
            from Patogeno p
            order by tipo ASC
            """.trimIndent()
            val query = session.createQuery(hql, Patogeno::class.java)
            return query.resultList
    }

}