package ar.edu.unq.eperdemic.persistencia.dao.mongo

import ar.edu.unq.eperdemic.services.transactions.MongoDBTransaction
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.conversions.Bson

open class GenericMongoDAO<T>(entityType: Class<T>) {

    var collection: MongoCollection<T> = MongoDBTransaction().getCollection(entityType.simpleName, entityType)

    fun deleteAll() {
        collection.drop(MongoDBTransaction.currentSession)
    }

    fun save(anObject: T) {
        save(listOf(anObject))
    }

    fun update(anObject: T, id: String?) {
        collection.replaceOne(MongoDBTransaction.currentSession, Filters.eq("id", id), anObject)
    }

    fun save(objects: List<T>) {
        collection.insertMany(MongoDBTransaction.currentSession, objects)

    }

    operator fun get(id: String?): T? {
        return getBy("id", id)
    }

    fun getBy(property: String, value: String?): T? {
        return collection.find(MongoDBTransaction.currentSession, Filters.eq(property, value)).first()
    }

    fun <E> findEq(field: String, value: E): List<T> {
        return find(Filters.eq(field, value))
    }

    fun find(filter: Bson): List<T> {
        return collection.find(MongoDBTransaction.currentSession, filter).into(mutableListOf())
    }

    fun <T> aggregate(pipeline: List<Bson>, resultClass: Class<T>): List<T> {
        return collection.aggregate(MongoDBTransaction.currentSession, pipeline, resultClass).into(ArrayList())
    }
}