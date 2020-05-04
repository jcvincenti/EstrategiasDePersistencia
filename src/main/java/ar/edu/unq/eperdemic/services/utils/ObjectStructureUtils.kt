package ar.edu.unq.eperdemic.services.utils

import ar.edu.unq.eperdemic.services.exceptions.NullPropertyException
import java.lang.reflect.Modifier
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.kotlinProperty

object ObjectStructureUtils {

    fun checkNullAttributes(o: Any) {
        val properties = o::class.declaredMemberProperties.filter{ isFieldAccessible(it) }
        properties.forEach {
            val field = o.javaClass.getDeclaredField(it.name)
            if (field.name != "id" && it.javaGetter!!.invoke(o) == null) {
                throw NullPropertyException("La propiedad ${it.name} es null")
            }
        }
    }

    private fun isFieldAccessible(property: KProperty1<*, *>): Boolean {
        return property.javaGetter?.modifiers?.let { !Modifier.isPrivate(it) } ?: false
    }


}