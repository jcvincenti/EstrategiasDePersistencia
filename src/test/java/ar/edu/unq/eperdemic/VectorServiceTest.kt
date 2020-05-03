package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class VectorServiceTest {
    @Spy
    var vectorService = VectorServiceImpl(HibernateVectorDAO())
    val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())
    val dataService = DataServiceHibernate()
    var virus: Patogeno? = null
    var paperas: Especie? = null
    var pepe: Vector? = null

    @BeforeEach
    fun crearSetDeDatosIniciales() {
        MockitoAnnotations.initMocks(this);
        dataService.crearSetDeDatosIniciales()
        virus = Patogeno("Virus")
        paperas = virus!!.crearEspecie("Paperas", "Yugoslavia")
        pepe = vectorService.crearVector(Vector(Ubicacion("Buenos Aires")))
        virus!!.setCapacidadDeContagio(TipoDeVectorEnum.Persona, 100)
        virus!!.setCapacidadDeContagio(TipoDeVectorEnum.Insecto, 100)
        pepe!!.tipo = TipoDeVectorEnum.Persona
    }

    @AfterEach
    fun eliminarTodo() {
        dataService.eliminarTodo()
    }

    @Test
    fun crearVectorTest() {
        val locacion = ubicacionService.crearUbicacion("Locacion-Test")
        val vector = Vector(locacion)
        vectorService.crearVector(vector)

        assertEquals(6, vector.id)
        assertEquals("Locacion-Test", vector.nombreDeLocacionActual!!.nombreUbicacion)
    }

    @Test
    fun recuperarVectorTest() {
        val vector = vectorService.recuperarVector(1)

        assertEquals("Buenos Aires", vector.nombreDeLocacionActual!!.nombreUbicacion)
        assertEquals(1, vector.id)
    }

    @Test
    fun borrarVectorTest() {
        vectorService.borrarVector(3)
        val exception = assertThrows<EntityNotFoundException> { vectorService.recuperarVector(3) }
        assertEquals("No se encontro un vector con el id 3", exception.message)
    }

    @Test
    fun infectarHumanoContagioExitosoTest() {
        doReturn(true).`when`(vectorService).esContagioExitoso(anyInt())

        Assert.assertTrue(pepe!!.especies.isEmpty())

        vectorService.infectar(pepe!!, paperas!!)

        Assert.assertFalse(pepe!!.especies.isEmpty())
    }

    @Test
    fun infectarHumanoContagioNoExitosoTest() {
        doReturn(false).`when`(vectorService).esContagioExitoso(anyInt())

        Assert.assertTrue(pepe!!.especies.isEmpty())

        vectorService.infectar(pepe!!, paperas!!)

        Assert.assertTrue(pepe!!.especies.isEmpty())
    }

    @Test
    fun getEnfermedades(){
        val enfermedades = vectorService.enfermedades(1)
        Assert.assertTrue(enfermedades.size == 3)
    }

    @Test
    fun contagiarTest(){
        // el vector con id 2 es un animal, el vector con id 3 es un insecto
        val vectores = mutableListOf(vectorService.recuperarVector(2), vectorService.recuperarVector(3))

        vectorService.infectar(pepe!!, paperas!!)
        vectorService.contagiar(pepe!!, vectores)

        // el vector 2 no tiene que tener especies porque no pudo ser infectado por una Persona
        assertEquals(0, vectorService.enfermedades(2).size)
        // el vector 3 tiene que tener una especie y tiene que ser la misma que la del vector infectado
        assertEquals(1, vectorService.enfermedades(3).size)
        assertEquals(pepe!!.especies.get(0).id, vectorService.enfermedades(3).get(0).id)
    }
}