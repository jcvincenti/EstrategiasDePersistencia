package ar.edu.unq.eperdemic.services.runner

import org.neo4j.driver.Session
import org.neo4j.driver.Transaction

object Neo4JTransactionFactory {
    private var session: Session? = null

    fun createTransaction(): Transaction? {
        session = Neo4JSessionFactoryProvider.instance.createSession()
        return session!!.beginTransaction()
    }
}

object Neo4JTransactionRunner {
    var currentTrx: Transaction? = null

    fun <T> runTrx(bloque: ()->T): T {
        currentTrx = Neo4JTransactionFactory.createTransaction()
        try {
            //codigo de negocio
            val resultado = bloque()
            currentTrx!!.commit()
            currentTrx!!.close()
            return resultado
        } catch (e: RuntimeException) {
            currentTrx!!.rollback()
            currentTrx!!.close()
            throw e
        }
    }
}