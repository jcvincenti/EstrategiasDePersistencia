package ar.edu.unq.eperdemic.services.runner

import org.neo4j.driver.Session
import org.neo4j.driver.Transaction

object Neo4JTransactionRunner {
    private var session: Session? = null
    var currentTrx: Transaction? = null

    fun <T> runTrx(bloque: ()->T): T {
        currentTrx = createTransaction()
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

    fun createTransaction(): Transaction? {
        session = Neo4JSessionFactoryProvider.instance.createSession()
        return session!!.beginTransaction()
    }
}