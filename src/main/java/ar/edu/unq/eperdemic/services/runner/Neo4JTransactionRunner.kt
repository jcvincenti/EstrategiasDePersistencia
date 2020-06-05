package ar.edu.unq.eperdemic.services.runner

import org.neo4j.driver.Transaction

object Neo4JTransactionRunner {
    var currentTrx: Transaction? = null

    fun <T> runTrx(bloque: (Transaction)->T): T {
        return Neo4JSessionFactoryProvider.instance.createSession().writeTransaction {
            //codigo de negocio
            return@writeTransaction bloque(it)
        }
    }
}