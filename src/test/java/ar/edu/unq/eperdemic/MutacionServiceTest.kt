package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateMutacionDAO
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException
import ar.edu.unq.eperdemic.services.impl.MutacionServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MutacionServiceTest {
    val dataService = DataServiceHibernate()
    val mutacionService = MutacionServiceImpl(HibernateMutacionDAO())

    @BeforeEach
    fun crearSetDeDatosIniciales() {
        dataService.crearSetDeDatosIniciales()
    }

    @AfterEach
    fun eliminarTodo() {
        dataService.eliminarTodo()
    }

    @Test
    fun crearMutacionTest(){
        val mutacion = Mutacion("defensa", 5, 10)
        mutacionService.crearMutacion(mutacion)

        Assert.assertEquals("defensa", mutacion.atributoAIncrementar)
        Assert.assertEquals(5, mutacion.adnRequerido)
        Assert.assertEquals(10, mutacion.valorAIncrementar)
        Assert.assertTrue(mutacion.mutacionesRequeridas.isEmpty())
    }

    @Test
    fun recuperarMutacionCreadaTest(){
        val mutacionACrear = Mutacion("letalidad", 5, 10)
        mutacionService.crearMutacion(mutacionACrear)

        val mutacion = mutacionService.recuperarMutacion(1)

        Assert.assertEquals("letalidad", mutacion.atributoAIncrementar)
        Assert.assertEquals(5, mutacion.adnRequerido)
        Assert.assertEquals(10, mutacion.valorAIncrementar)
        Assert.assertTrue(mutacion.mutacionesRequeridas.isEmpty())
    }

    @Test
    fun recuperarMutacionInexistenteTest() {
        val exception = assertThrows<EntityNotFoundException> { mutacionService.recuperarMutacion(3) }
        assertEquals("La entidad Mutacion con id 3 no existe", exception.message)
    }
}