package ar.edu.unq.eperdemic


import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        Assert.assertEquals(vector.nombreDeLocacionActual, "Buenos Aires")
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
}