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
            from Especie e
            join e.vectores v
            group by e.id, v.tipo
            having v.tipo in ('${TipoDeVectorEnum.Persona.name}', '${TipoDeVectorEnum.Animal.name}')
            order by count(e.id)
        """.trimIndent()
        val query = session.createQuery(hql, Especie::class.java)
        // se utiliza asReversed porque la query no ordena de forma descendente correctamente
        // se utiliza subList debido a que query.setMaxElements cambia el orden de la lista
        return query.resultList.asReversed().subList(0,10)
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
            from Especie e
            join e.vectores v
            group by e.id, v.tipo
            having v.tipo in ('${TipoDeVectorEnum.Persona.name}')
            order by count(e.id) desc
        """.trimIndent()
        val query = session.createQuery(hql, Especie::class.java)
        return query.resultList[0]
    }
}