package ar.edu.unq.eperdemic.services.utils

import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateDAO
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException

inline fun <reified T> entityNotFound(id: String) {
    val dao = HibernateDAO(T::class.java)
    if (dao.recuperar(id) == null)
        throw EntityNotFoundException("La entidad ${T::class.simpleName} no existe")
}

inline fun <reified T> entityExists(id: String)  {
    val dao = HibernateDAO(T::class.java)
    if (dao.recuperar(id) == null)
        throw EntityNotFoundException("La entidad ${T::class.simpleName} ya existe")
}