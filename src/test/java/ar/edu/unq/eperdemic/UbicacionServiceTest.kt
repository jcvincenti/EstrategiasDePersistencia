package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.TipoDeVectorEnum
import ar.edu.unq.eperdemic.modelo.Vector
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
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class UbicacionServiceTest {

    val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())
    @Spy
    val vectorService = VectorServiceImpl(HibernateVectorDAO())
    val patogenoService = PatogenoServiceImpl(HibernatePatogenoDAO())
    val dataService = DataServiceHibernate()

    @BeforeEach
    fun crearSetDeDatosIniciales() {
        MockitoAnnotations.initMocks(this);
        dataService.crearSetDeDatosIniciales()
    }

    @AfterEach
    fun eliminarTodo() {
        dataService.eliminarTodo()
    }

    @Test
    fun crearUbicacionTest(){
        val ubicacion = ubicacionService.crearUbicacion("Adrogue")

        Assert.assertEquals("Adrogue", ubicacion.nombreUbicacion)
    }

    @Test
    fun recuperarUbicacionTest() {
        val ubicacion = ubicacionService.recuperarUbicacion("Entre Rios")
        Assert.assertEquals(ubicacion!!.nombreUbicacion, "Entre Rios")
    }

    @Test
    fun moverConVectorInfectadoTest(){
        var persona = vectorService.recuperarVector(1)
        var cordobes = vectorService.recuperarVector(4)

        Assert.assertEquals("Buenos Aires", persona.nombreDeLocacionActual!!.nombreUbicacion)
        Assert.assertTrue(persona.estaInfectado())
        Assert.assertFalse(cordobes.estaInfectado())
        Mockito.doReturn(true).`when`(vectorService).esContagioExitoso(Mockito.anyInt())
        ubicacionService.mover(1, "Cordoba")
        persona = vectorService.recuperarVector(1)
        cordobes = vectorService.recuperarVector(4)

        //La Persona cambio su ubicacion a "Cordoba" y el Cordobes ahora esta infectado
        Assert.assertEquals("Cordoba", persona.nombreDeLocacionActual!!.nombreUbicacion)
        Assert.assertTrue(cordobes.estaInfectado())
    }

    @Test
    fun moverConVectorNoInfectadoTest(){
        var insecto = vectorService.recuperarVector(3)
        var cordobes = vectorService.recuperarVector(4)

        Assert.assertEquals("Bariloche", insecto.nombreDeLocacionActual!!.nombreUbicacion)
        Assert.assertFalse(insecto.estaInfectado())
        Assert.assertFalse(cordobes.estaInfectado())

        ubicacionService.mover(3, "Cordoba")
        insecto = vectorService.recuperarVector(3)
        cordobes = vectorService.recuperarVector(4)

        //El Insecto cambio su ubicacion a "Cordoba" pero el cordobes no se infecto
        Assert.assertEquals("Cordoba", insecto.nombreDeLocacionActual!!.nombreUbicacion)
        Assert.assertFalse(cordobes.estaInfectado())
    }

    @ExperimentalStdlibApi
    @Test
    fun expandirVectorInfectadoTest(){
        var buenosAires = ubicacionService.recuperarUbicacion("Buenos Aires")
        var carlos = Vector(buenosAires!!)
        carlos.tipo = TipoDeVectorEnum.Persona
       //Mockito.doReturn(true).`when`(vectorService).esContagioExitoso(Mockito.anyInt())
        var carlosId = vectorService.crearVector(carlos).id
        Assert.assertFalse(carlos.estaInfectado())
        ubicacionService.expandir("Buenos Aires")
        Assert.assertTrue(vectorService.recuperarVector(carlosId!!).estaInfectado())
    }

    @ExperimentalStdlibApi
    @Test
    fun expandirVectorNoInfectadoTest(){
        var ricardo = vectorService.recuperarVector(4)
        var cordoba = ubicacionService.recuperarUbicacion("Cordoba")
        var jorge = Vector(cordoba!!)
        jorge.tipo = TipoDeVectorEnum.Persona
        var jorgeId = vectorService.crearVector(jorge).id
        Assert.assertFalse(ricardo.estaInfectado())
        Assert.assertFalse(jorge.estaInfectado())
        ubicacionService.expandir("Cordoba")
        Assert.assertFalse(vectorService.recuperarVector(jorgeId!!).estaInfectado())
    }
}
