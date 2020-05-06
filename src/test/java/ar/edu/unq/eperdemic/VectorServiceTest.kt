package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.modelo.exceptions.VectorNoInfectadoException
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException
import ar.edu.unq.eperdemic.services.exceptions.EmptyPropertyException
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.*
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class VectorServiceTest {
    var vectorService = VectorServiceImpl(HibernateVectorDAO())
    val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())
    val patogenoService = PatogenoServiceImpl(HibernatePatogenoDAO())
    val dataService = DataServiceHibernate()
    var virus: Patogeno? = null
    var paperas: Especie? = null
    var cordobes: Vector? = null

    @BeforeEach
    fun crearSetDeDatosIniciales() {
        MockitoAnnotations.initMocks(this)
        dataService.crearSetDeDatosIniciales()
        virus = patogenoService.recuperarPatogeno(1)
        paperas = virus!!.crearEspecie("Paperas", "Yugoslavia")
        cordobes = vectorService.recuperarVector(4)
        virus!!.setCapacidadDeContagio(TipoDeVectorEnum.Insecto, 100)
    }

    @AfterEach
    fun eliminarTodo() {
        dataService.eliminarTodo()
    }

    @Test
    fun crearVectorTest() {
        val locacion = ubicacionService.crearUbicacion("Locacion-Test")
        val vector = Vector(locacion.nombreUbicacion!!)
        vector.tipo = TipoDeVectorEnum.Persona
        vectorService.crearVector(vector)

        assertEquals(5, vector.id)
        assertEquals("Locacion-Test", vector.nombreDeLocacionActual!!)
    }

    @Test
    fun crearVectorSinLocacionTest() {
        val vector = Vector()
        vector.tipo = TipoDeVectorEnum.Persona
        val exception = assertThrows<EmptyPropertyException> { vectorService.crearVector(vector) }

        assertEquals("La propiedad nombreDeLocacionActual esta vacia", exception.message)
    }

    @Test
    fun crearVectorSinTipoDeVectorTest() {
        val locacion = ubicacionService.crearUbicacion("Locacion-Test")
        val vector = Vector(locacion.nombreUbicacion!!)
        val exception = assertThrows<EmptyPropertyException> { vectorService.crearVector(vector) }

        assertEquals("La propiedad tipo esta vacia", exception.message)
    }

    @Test
    fun recuperarVectorTest() {
        val vector = vectorService.recuperarVector(1)

        assertEquals("Buenos Aires", vector.nombreDeLocacionActual!!)
        assertEquals(1, vector.id)
    }

    @Test
    fun recuperarVectorNoExistenteTest() {
        val exception = assertThrows<EntityNotFoundException> { vectorService.recuperarVector(100) }

        assertEquals("No se encontro un vector con el id 100", exception.message)
    }

    @Test
    fun borrarVectorTest() {
        vectorService.borrarVector(3)
        
        val exception = assertThrows<EntityNotFoundException> { vectorService.recuperarVector(3) }
        assertEquals("No se encontro un vector con el id 3", exception.message)
    }

    @Test
    fun infectarHumanoContagioExitosoTest() {
        val paperasSpy = spy(paperas)
        doReturn(true).`when`(paperasSpy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)

        Assert.assertTrue(cordobes!!.especies.isEmpty())

        vectorService.infectar(cordobes!!, paperasSpy!!)

        Assert.assertFalse(cordobes!!.especies.isEmpty())
    }

    @Test
    fun infectarHumanoContagioNoExitosoTest() {
        val paperasSpy = spy(paperas)
        doReturn(false).`when`(paperasSpy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)

        Assert.assertTrue(cordobes!!.especies.isEmpty())

        vectorService.infectar(cordobes!!, paperasSpy!!)

        Assert.assertTrue(cordobes!!.especies.isEmpty())
    }

    @Test
    fun getEnfermedadesTest(){
        val enfermedades = vectorService.enfermedades(1)

        Assert.assertTrue(enfermedades.size == 3)
    }

    @Test
    fun contagiarTest(){
        // el vector con id 2 es un animal, el vector con id 3 es un insecto
        val vectores = mutableListOf(vectorService.recuperarVector(2), vectorService.recuperarVector(3))

        vectorService.infectar(cordobes!!, paperas!!)
        vectorService.contagiar(cordobes!!, vectores)

        // el vector 2 no tiene que tener especies porque no pudo ser infectado por una Persona
        assertEquals(0, vectorService.enfermedades(2).size)
        // el vector 3 tiene que tener una especie y tiene que ser la misma que la del vector infectado
        assertEquals(1, vectorService.enfermedades(3).size)
        assertEquals(cordobes!!.especies.get(0).id, vectorService.enfermedades(3).get(0).id)
    }

    @Test
    fun contagiarConVectorNoInfectadoTest() {
        val vectores = mutableListOf(vectorService.recuperarVector(2), vectorService.recuperarVector(3))

        val exception = assertThrows<VectorNoInfectadoException> { vectorService.contagiar(cordobes!!, vectores) }

        assertEquals("El vector no esta infectado", exception.message)
    }

    @Test
    fun crearVectorConUbicacionInexistente() {
        var pibe = Vector()
        pibe.nombreDeLocacionActual = "pibelandia"
        pibe.tipo = TipoDeVectorEnum.Persona
        val exception = assertThrows<EntityNotFoundException> { vectorService.crearVector(pibe) }
        assertEquals("No se encontro una ubicacion con el nombre pibelandia", exception.message)
    }
}