package ar.edu.unq.eperdemic


import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import org.junit.jupiter.api.Test

class VectorServiceTest {
    val vectorService = VectorServiceImpl(HibernateVectorDAO())

    @Test
    fun crearVectorTest() {
        vectorService.crearVector(Vector("Locacion-Test"))
        // TODO: chequear con recuperar
    }
}