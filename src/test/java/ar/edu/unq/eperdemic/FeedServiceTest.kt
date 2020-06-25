package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.mongo.MongoEventoDAO
import ar.edu.unq.eperdemic.services.impl.FeedServiceImpl
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import ar.edu.unq.eperdemic.utils.neo4j.DataServiceNeo4j
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations

class FeedServiceTest {

    val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())
    val feedService = FeedServiceImpl()
    val dataService = DataServiceHibernate()
    val neo4jDataService = DataServiceNeo4j()
    val mongoDAO = MongoEventoDAO()
    val vectorService = VectorServiceImpl(HibernateVectorDAO())
    lateinit var portenho: Vector

    @BeforeEach
    fun crearSetDeDatosIniciales() {
        MockitoAnnotations.initMocks(this)
        dataService.crearSetDeDatosIniciales()
        neo4jDataService.crearSetDeDatosIniciales()
        portenho = vectorService.recuperarVector(1)
    }

    @AfterEach
    fun eliminarTodo() {
        dataService.eliminarTodo()
        neo4jDataService.eliminarTodo()
        mongoDAO.deleteAll()
    }

    @Test
    fun feedVectorTest() {
        ubicacionService.conectar("Cordoba", "Bariloche", "terrestre")
        ubicacionService.mover(1, "Cordoba")
        ubicacionService.mover(4, "Bariloche")
        val eventos = feedService.feedVector(4)
        assertEquals(4, eventos.size)
        assertEquals("Contagio", eventos.first().tipo)
        assertEquals("Contagio", eventos.last().tipo)
        assertEquals(5, eventos.last().especies!!)
    }

    @Test
    fun moverUnVectorGeneraEventoDeArriboYContagioTest() {
        ubicacionService.conectar("Cordoba", "Quilmes", "terrestre")
        ubicacionService.mover(1, "Cordoba")
        ubicacionService.mover(1, "Quilmes")
        val eventos = feedService.feedUbicacion("Cordoba")
        assertEquals(3, eventos.size)
        assertEquals("Arribo", eventos.first().tipo)
        assertEquals("Quilmes", eventos.first().nombreUbicacion)
        assertEquals("Contagio", eventos[1].tipo)
        assertEquals("Arribo", eventos.last().tipo)
        assertEquals("Cordoba", eventos.last().nombreUbicacion)
    }
}