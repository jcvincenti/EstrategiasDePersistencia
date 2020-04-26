package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.services.runner.TransactionRunner


open class HibernateDAO<T>(private val entityType: Class<T>) {

    fun guardar(item: T) {
        val session = TransactionRunner.currentSession
        session.save(item)
    }

    fun recuperar(id: Int): T {
        val session = TransactionRunner.currentSession
        return session.get(entityType, id)
    }
}