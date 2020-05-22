package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.TipoDeVectorEnum
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class HibernateEspecieDAO: HibernateDAO<Especie>(Especie::class.java), EspecieDAO {
    override fun lideres(): List<Especie> {
        val session = TransactionRunner.currentSession
        val hql = """
            select e
            from Vector v
            join v.especies e
            where v.tipo in (${TipoDeVectorEnum.Persona.ordinal}, ${TipoDeVectorEnum.Animal.ordinal})
            group by e 
            order by count(v) desc
        """.trimIndent()
        val query = session.createQuery(hql, Especie::class.java)
        return query.setMaxResults(10).resultList
    }

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

    override fun findEspecieLider(): Especie {
        val session = TransactionRunner.currentSession
        val hql = """
            select e
            from Vector v
            join v.especies e
            where v.tipo in (${TipoDeVectorEnum.Persona.ordinal})
            group by e 
            order by count(v) desc
        """.trimIndent()
        val query = session.createQuery(hql, Especie::class.java)
        return query.resultList.first()
    }

    override fun cantidadDeInfectados(especieId: Int): Int {
        val session = TransactionRunner.currentSession
        val hql = (
                """
                    select count(e.id)
                    from Vector v
                    join v.especies e
                    where e.id = :especieId
                """.trimIndent()
                )
        val query = session.createQuery(hql, Long::class.javaObjectType)
        query.setParameter("especieId", especieId)
        return query.singleResult.toInt()
    }
}
