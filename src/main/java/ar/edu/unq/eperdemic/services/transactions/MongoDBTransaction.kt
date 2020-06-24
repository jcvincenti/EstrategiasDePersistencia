package ar.edu.unq.eperdemic.services.transactions

import ar.edu.unq.eperdemic.persistencia.dao.mongo.MongoConnection

class MongoDBTransaction : Transaction {

    var connection: MongoConnection = MongoConnection()

    override fun start() {
        connection.startTransaction()
    }

    override fun commit() {
        connection.commitTransaction()
    }

    override fun rollback() {
        connection.rollbackTransaction()
    }

    override fun close() {
        connection.closeSession()
    }
}