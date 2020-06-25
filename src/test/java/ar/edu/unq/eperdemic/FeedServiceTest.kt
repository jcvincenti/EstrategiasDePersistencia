package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.dto.VectorFrontendDTO
import ar.edu.unq.eperdemic.modelo.AtributoEnum
import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.modelo.TipoDeEventoEnum
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateMutacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.mongo.MongoEventoDAO
import ar.edu.unq.eperdemic.services.impl.*
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import ar.edu.unq.eperdemic.utils.neo4j.DataServiceNeo4j
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
    val patogenoService = PatogenoServiceImpl(HibernatePatogenoDAO())
    val mutacionService = MutacionServiceImpl(HibernateMutacionDAO())
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
        //mongoDAO.deleteAll()
    }

    @Test
    fun feedPatogenoTest(){
        val sarampion = patogenoService.agregarEspecie(1, "sarampion", "indefinido")
        val patogeno = patogenoService.recuperarPatogeno(1)
        val mutacion = Mutacion(AtributoEnum.Defensa, 0F, 10)
        val cordobes = vectorService.recuperarVector(4)
        val pampeano = vectorService.recuperarVector(6)
        val barilochense = vectorService.recuperarVector(5)
        val quilmenho = VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Persona,"Quilmes").aModelo()
        val catamarquenho = VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Persona,"Catamarca").aModelo()
        val vectores = listOf(cordobes, pampeano, barilochense, catamarquenho,portenho)
        vectorService.crearVector(quilmenho)
        vectorService.crearVector(catamarquenho)
        quilmenho.infectar(sarampion)
        vectorService.contagiar(quilmenho, vectores)
        mutacion.mutacionesRequeridas.add(mutacionService.recuperarMutacion(1))
        mutacionService.crearMutacion(mutacion)
        mutacionService.mutar(sarampion.id, mutacion.id)
       // val eventos = feedService.feedPatogeno(patogeno.tipo),
        assertTrue(patogenoService.esPandemia(sarampion.id))
    }

    @Test
    fun feedVectorTest() {
        ubicacionService.conectar("Cordoba", "Bariloche", "terrestre")
        ubicacionService.mover(1, "Cordoba")
        ubicacionService.mover(4, "Bariloche")
        val eventos = feedService.feedVector(4)
        assertEquals(4, eventos.size)
        assertEquals(TipoDeEventoEnum.Contagio, eventos.first().tipo)
        assertEquals(TipoDeEventoEnum.Contagio, eventos.last().tipo)
        assertEquals(5, eventos.last().especies!!.size)
    }

    @Test
    fun moverUnVectorGeneraEventoDeArriboYContagioTest() {
        ubicacionService.conectar("Cordoba", "Quilmes", "terrestre")
        ubicacionService.mover(1, "Cordoba")
        ubicacionService.mover(1, "Quilmes")
        val eventos = feedService.feedUbicacion("Cordoba")
        assertEquals(3, eventos.size)
        assertEquals(TipoDeEventoEnum.Arribo, eventos.first().tipo)
        assertEquals("Quilmes", eventos.first().nombreUbicacion)
        assertEquals(TipoDeEventoEnum.Contagio, eventos[1].tipo)
        assertEquals(TipoDeEventoEnum.Arribo, eventos.last().tipo)
        assertEquals("Cordoba", eventos.last().nombreUbicacion)
    }
}