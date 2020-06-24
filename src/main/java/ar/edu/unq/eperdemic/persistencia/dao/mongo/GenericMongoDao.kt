package ar.edu.unq.eperdemic.persistencia.dao.mongo

import ar.edu.unq.eperdemic.services.transactions.MongoDBTransaction
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.conversions.Bson

open class GenericMongoDao<T>(entityType: Class<T>) {

    protected var transaction = MongoDBTransaction()
    protected var collection:MongoCollection<T>

    init {
        collection = transaction.connection.getCollection(entityType.simpleName, entityType)
    }

    fun deleteAll() {
        if(transaction.connection.session != null) {
            collection.drop(transaction.connection.session!!)
        }else{
            collection.drop()
        }
    }

    fun save(anObject: T) {
        save(listOf(anObject))
    }

    fun update(anObject: T, id: String?) {
        if(transaction.connection.session != null) {
            collection.replaceOne(transaction.connection.session!!, Filters.eq("id", id), anObject)
        }else{
            collection.replaceOne(Filters.eq("id", id), anObject)
        }
    }

    fun save(objects: List<T>) {
        if(transaction.connection.session != null) {
            collection.insertMany(transaction.connection.session!!, objects)
        }else{
            collection.insertMany(objects)
        }

    }

    operator fun get(id: String?): T? {
        return getBy("id", id)
    }

    fun getBy(property:String, value: String?): T? {
        if(transaction.connection.session != null) {
            return collection.find(transaction.connection.session!!, Filters.eq(property, value)).first()
        }
        return collection.find(Filters.eq(property, value)).first()
    }

    fun <E> findEq(field:String, value:E ): List<T> {
        return find(Filters.eq(field, value))
    }

    fun find(filter:Bson): List<T> {
        if(transaction.connection.session != null) {
            return collection.find(transaction.connection.session!!, filter).into(mutableListOf())
        }
        return collection.find(filter).into(mutableListOf())
    }

    fun <T> aggregate(pipeline:List<Bson> , resultClass:Class<T>): List<T> {
        if(transaction.connection.session != null) {
            return collection.aggregate(transaction.connection.session!!, pipeline, resultClass).into(ArrayList())
        }
        return collection.aggregate(pipeline, resultClass).into(ArrayList())
    }
}