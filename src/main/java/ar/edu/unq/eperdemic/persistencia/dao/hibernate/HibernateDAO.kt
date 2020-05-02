package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.services.runner.TransactionRunner


open class HibernateDAO<T>(private val entityType: Class<T>) {

    fun guardar(objeto: T) {
        val session = TransactionRunner.currentSession
        session.save(objeto)
    }

    fun recuperar(id: Int): T {
        val session = TransactionRunner.currentSession
        return session.get(entityType, id)
    }

    fun borrar(objeto : T){
        val session = TransactionRunner.currentSession
        session.delete(objeto)
    }

    fun actualizar(objeto : T) {
        val session = TransactionRunner.currentSession
        session.update(objeto)
    }

    fun recuperar(id: String): T {
        val session = TransactionRunner.currentSession
        return session.get(entityType, id)
    }

}