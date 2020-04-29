package ar.edu.unq.eperdemic


import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.transaction.Transactional

class VectorServiceTest {
    val vectorService = VectorServiceImpl(HibernateVectorDAO())
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
    fun crearVectorTest() {
        val vector = Vector("Locacion-Test")
        vectorService.crearVector(vector)
        Assert.assertEquals(4, vector.id)
        Assert.assertEquals("Locacion-Test", vector.nombreDeLocacionActual)
    }

    @Test
    fun recuperarVectorTest() {
        val vector = vectorService.recuperarVector(1)
        assertEquals(vector.nombreDeLocacionActual, "Buenos Aires")
        assertEquals(1, vector.id)
    }

    @Test
    fun borraVectorTest() {
        vectorService.borrarVector(3)
        Assert.assertNull(vectorService.recuperarVector(3))
    }

    @Test
    fun infectarHumanoTest() {
        var virus = Patogeno("Virus")
        virus.setCapacidadDeContagio("Persona", 90)
        var paperas = virus.crearEspecie("Paperas", "Yugoslavia")
        var pepe = Vector("Buenos Aires")
        pepe.tipo = "Persona"
        Assert.assertTrue(pepe.especies.isEmpty())
        vectorService.infectar(pepe, paperas)
        Assert.assertFalse(pepe.especies.isEmpty())
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

        var virus = Patogeno("Virus")

        virus.setCapacidadDeContagio("Insecto", 100)
        virus.setCapacidadDeContagio("Persona", 100)

        val paperas = virus.crearEspecie("Paperas", "Yugoslavia")
        var vectorInfectado = Vector("Las Heras")
        vectorInfectado.tipo = "Persona"

        vectorService.infectar(vectorInfectado, paperas)
        vectorService.contagiar(vectorInfectado, vectores)

        // el vector 2 no tiene que tener especies porque no pudo ser infectado por una Persona
        assertEquals(0, vectorService.enfermedades(2).size)
        // el vector 3 tiene que tener una especie y tiene que ser la misma que la del vector infectado
        assertEquals(1, vectorService.enfermedades(3).size)
        assertEquals(vectorInfectado.especies.get(0).id, vectorService.enfermedades(3).get(0).id)
    }
}