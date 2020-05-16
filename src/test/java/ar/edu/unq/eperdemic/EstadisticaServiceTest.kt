package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.impl.EstadisticaServiceImpl
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito


class EstadisticaServiceTest {
    val estadisticaService = EstadisticaServiceImpl()
    var vectorService = VectorServiceImpl(HibernateVectorDAO())
    val patogenoService = PatogenoServiceImpl(HibernatePatogenoDAO())
    val dataService = DataServiceHibernate()
    var virus: Patogeno? = null
    var paperas: Especie? = null
    var corona: Especie? = null
    var gripe: Especie? = null
    var cordobes: Vector? = null
    var animalCordobes: Vector? = null
    var portenho: Vector? = null
    var insectoBarilochense: Vector? = null

    @BeforeEach
    fun crearSetDeDatosIniciales() {
        dataService.crearSetDeDatosIniciales()
        virus = patogenoService.recuperarPatogeno(1)
        paperas = virus!!.crearEspecie("Paperas", "Yugoslavia")
        corona = virus!!.crearEspecie("Coronavirus","China")
        gripe = patogenoService.recuperarEspecie(1)
        cordobes = vectorService.recuperarVector(4)
        animalCordobes = vectorService.recuperarVector(2)
        portenho = vectorService.recuperarVector(1)
        insectoBarilochense = vectorService.recuperarVector(3)
        virus!!.setCapacidadDeContagio(TipoDeVectorEnum.Insecto, 100)
        virus!!.setCapacidadDeContagio(TipoDeVectorEnum.Animal, 100)
        virus!!.setCapacidadDeContagio(TipoDeVectorEnum.Persona, 100)
    }

    @AfterEach
    fun eliminarTodo() {
        dataService.eliminarTodo()
    }

    @Test
    fun reporteDeContagiosConUnVectorInfectadoTest(){
        vectorService.infectar(cordobes!!, paperas!!)
        val reporte = estadisticaService.reporteDeContagios("Cordoba")

        assertEquals(2, reporte.vectoresPresentes)
        assertEquals(1, reporte.vectoresInfecatods)
        assertEquals("Paperas", reporte.nombreDeEspecieMasInfecciosa)
    }

    @Test
    fun reporteDeContagiosConDosVectoresInfectadosTest(){
        vectorService.infectar(cordobes!!, paperas!!)
        vectorService.infectar(animalCordobes!!, corona!!)
        vectorService.infectar(cordobes!!, corona!!)

        val reporte = estadisticaService.reporteDeContagios("Cordoba")

        assertEquals(2, reporte.vectoresPresentes)
        assertEquals(2, reporte.vectoresInfecatods)
        assertEquals("Coronavirus", reporte.nombreDeEspecieMasInfecciosa)
    }

    @Test
    fun reporteDeContagiosSinVectoresInfectadosTest(){
        val reporte = estadisticaService.reporteDeContagios("Catamarca")

        assertEquals(0, reporte.vectoresPresentes)
        assertEquals(0, reporte.vectoresInfecatods)
        assertEquals("",reporte.nombreDeEspecieMasInfecciosa)
    }

    @Test
    fun lideresTest() {
        val gripeSpy = Mockito.spy(gripe)
        Mockito.doReturn(true).`when`(gripeSpy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(gripeSpy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        val h1n1Spy = Mockito.spy(patogenoService.recuperarEspecie(2))
        Mockito.doReturn(true).`when`(h1n1Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(h1n1Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        val anginasSpy = Mockito.spy(patogenoService.recuperarEspecie(3))
        Mockito.doReturn(true).`when`(anginasSpy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(anginasSpy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        vectorService.infectar(cordobes!!, gripeSpy!!)
        vectorService.infectar(animalCordobes!!, gripeSpy!!)
        vectorService.infectar(cordobes!!, h1n1Spy!!)
        vectorService.infectar(animalCordobes!!, h1n1Spy!!)
        vectorService.infectar(cordobes!!, anginasSpy!!)
        vectorService.infectar(animalCordobes!!, anginasSpy!!)
        val lideres = estadisticaService.lideres()
    }
}