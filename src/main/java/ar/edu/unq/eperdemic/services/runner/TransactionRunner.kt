import ar.edu.unq.eperdemic.services.transactions.HibernateTransaction
import ar.edu.unq.eperdemic.services.transactions.MongoDBTransaction
import ar.edu.unq.eperdemic.services.transactions.Neo4JTransaction
import ar.edu.unq.eperdemic.services.transactions.Transaction

object TransactionRunner {
    private var transactions:List<Transaction> = listOf(HibernateTransaction(), Neo4JTransaction(), MongoDBTransaction())

    fun <T> runTrx(bloque: ()->T): T {
        try{
            transactions.forEach { it.start() }
            val result = bloque()
            transactions.forEach { it.commit() }
            return result
        } catch (exception:Throwable){
            transactions.forEach { it.rollback() }
            throw exception
        } finally {
            transactions.forEach { it.close() }
        }
    }
}