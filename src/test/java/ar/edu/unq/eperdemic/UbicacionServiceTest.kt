package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UbicacionServiceTest {

    val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())
    val vectorService = VectorServiceImpl(HibernateVectorDAO())
    val patogenoService = PatogenoServiceImpl(HibernatePatogenoDAO())
    val dataService = DataServiceHibernate()

    @BeforeEach
    fun crearSetDeDatosIniciales() {
        dataService.crearSetDeDatosIniciales()
    }

    @AfterEach
    fun eliminarTodo() {
        dataService.eliminarTodo()
    }

    @Test
    fun crearUbicacionTest(){
        val ubicacion = ubicacionService.crearUbicacion("Quilmes")

        Assert.assertEquals("Quilmes", ubicacion.nombreUbicacion)
    }

    @Test
    fun recuperarUbicacionTest() {
        val ubicacion = ubicacionService.recuperarUbicacion("Entre Rios")
        Assert.assertEquals(ubicacion.nombreUbicacion, "Entre Rios")
    }

    @Test
    fun moverConVectorInfectadoTest(){
        var persona = vectorService.recuperarVector(1)
        var cordobes = vectorService.recuperarVector(4)
        val virus = patogenoService.recuperarPatogeno(1)
        virus.setCapacidadDeContagio("Animal", 100)
        virus.setCapacidadDeContagio("Insecto", 100)
        virus.setCapacidadDeContagio("Persona", 100)
        patogenoService.actualizarPatogeno(virus)

        Assert.assertEquals("Buenos Aires", persona.nombreDeLocacionActual)
        Assert.assertTrue(persona.estaInfectado())
        Assert.assertFalse(cordobes.estaInfectado())

        ubicacionService.mover(1, "Cordoba")
        persona = vectorService.recuperarVector(1)
        cordobes = vectorService.recuperarVector(4)

        //La Persona cambio su ubicacion a "Cordoba" y el Cordobes ahora esta infectado
        Assert.assertEquals("Cordoba", persona.nombreDeLocacionActual)
        Assert.assertTrue(cordobes.estaInfectado())
    }

    @Test
    fun moverConVectorNoInfectadoTest(){
        var insecto = vectorService.recuperarVector(3)
        var cordobes = vectorService.recuperarVector(4)

        Assert.assertEquals("Bariloche", insecto.nombreDeLocacionActual)
        Assert.assertFalse(insecto.estaInfectado())
        Assert.assertFalse(cordobes.estaInfectado())

        ubicacionService.mover(3, "Cordoba")
        insecto = vectorService.recuperarVector(3)
        cordobes = vectorService.recuperarVector(4)

        //El Insecto cambio su ubicacion a "Cordoba" pero el cordobes no se infecto
        Assert.assertEquals("Cordoba", insecto.nombreDeLocacionActual)
        Assert.assertFalse(cordobes.estaInfectado())
    }
}