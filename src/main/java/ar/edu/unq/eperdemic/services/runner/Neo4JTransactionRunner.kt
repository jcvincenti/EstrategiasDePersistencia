package ar.edu.unq.eperdemic.services.runner

import org.neo4j.driver.Transaction

object Neo4JTransactionRunner {
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
        return Neo4JSessionFactoryProvider.instance.createSession().beginTransaction()
    }
}