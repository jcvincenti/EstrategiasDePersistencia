package ar.edu.unq.eperdemic.services.utils


import ar.edu.unq.eperdemic.services.exceptions.NullPropertyException
import java.lang.reflect.Modifier
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaGetter

/**
 * Utils para chequeos relacionados a la estructura de objetos de manera generica
 */
object ObjectStructureUtils {

    /**
     * Metodo para validar que la estructura de un objeto no contenga nulls o strings vacios
     * Nota: los ID estan exceptuados de este chequeo
     */
    fun checkNullAttributes(o: Any) {
        val properties = o::class.declaredMemberProperties.filter{ isFieldAccessible(it) }
        properties.forEach {
            if (isNullOrEmpty(it, o)) {
                throw NullPropertyException("La propiedad ${it.name} esta vacia")
            }
        }
    }

    private fun isFieldAccessible(property: KProperty1<*, *>): Boolean {
        return property.javaGetter?.modifiers?.let { !Modifier.isPrivate(it) } ?: false
    }

    private fun isNullOrEmpty(property: KProperty1<*, *>, o: Any) : Boolean {
        val field = o.javaClass.getDeclaredField(property.name)
        val propertyValue = property.javaGetter!!.invoke(o)
        return field.name != "id" && propertyValue == null ||
                property.javaGetter!!.returnType == String::class.java &&
                (propertyValue == "" || propertyValue == null)
    }

}