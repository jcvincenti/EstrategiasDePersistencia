package ar.edu.unq.eperdemic.services.transactions

import ar.edu.unq.eperdemic.services.runner.MongoDBSessionFactoryProvider
import com.mongodb.*
import com.mongodb.client.*

class MongoDBTransaction : Transaction {

    val sessionFactory = MongoDBSessionFactoryProvider()

    companion object {
        private var session: ClientSession? = null
        val currentSession: ClientSession
            get() {
                if (session == null) {
                    throw RuntimeException("No hay ninguna session en el contexto")
                }
                return session!!
            }
    }

    fun <T> getCollection(name:String, entityType: Class<T> ): MongoCollection<T> {
        try{
            sessionFactory.dataBase.createCollection(name)
        } catch (exception: MongoCommandException){
            println("Ya existe la coleccion $name")
        }
        return sessionFactory.dataBase.getCollection(name, entityType)
    }

    override fun start() {
        try {
            session = sessionFactory.createSession()
            session?.startTransaction(TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build())
        }catch (exception: MongoClientException){
            exception.printStackTrace()
        }
    }

    override fun commit() {
        session?.commitTransaction()
        session?.close()
    }

    override fun rollback() {
        session?.abortTransaction()
        session?.close()
    }

    override fun close() {
        session?.close()
        session = null
    }
}