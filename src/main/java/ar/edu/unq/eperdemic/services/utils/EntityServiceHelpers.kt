package ar.edu.unq.eperdemic.services.utils

import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateDAO
import ar.edu.unq.eperdemic.services.exceptions.EntityAlreadyExistsException
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException

inline fun <reified T> validateEntityExists(id: String) {
    val dao = HibernateDAO(T::class.java)
    if (dao.recuperar(id) == null)
        throw EntityNotFoundException("La entidad ${T::class.simpleName} no existe")
}

inline fun <reified T> validateEntityExists(id: Int) {
    val dao = HibernateDAO(T::class.java)
    if (dao.recuperar(id) == null)
        throw EntityNotFoundException("La entidad ${T::class.simpleName} no existe")
}

inline fun <reified T> validateEntityDoesNotExists(id: String)  {
    val dao = HibernateDAO(T::class.java)
    if (dao.recuperar(id) != null)
        throw EntityAlreadyExistsException("La entidad ${T::class.simpleName} ya existe")
}