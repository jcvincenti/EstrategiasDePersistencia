package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.impl.EstadisticaServiceImpl
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
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
    var barilochense: Vector? = null

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
        barilochense = vectorService.recuperarVector(5)
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
        var especie1 = virus!!.crearEspecie("Especie1", "Argentina")
        val especie1Spy = Mockito.spy(especie1)
        Mockito.doReturn(true).`when`(especie1Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(especie1Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        val especie2 = virus!!.crearEspecie("Especie2", "Argentina")
        val especie2Spy = Mockito.spy(especie2)
        Mockito.doReturn(true).`when`(especie2Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(especie2Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        val especie3 = virus!!.crearEspecie("Especie3", "Argentina")
        val especie3Spy = Mockito.spy(especie3)
        Mockito.doReturn(true).`when`(especie3Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(especie3Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        val especie4 = virus!!.crearEspecie("Especie4", "Argentina")
        val especie4Spy = Mockito.spy(especie4)
        Mockito.doReturn(true).`when`(especie4Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(especie4Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        val especie5 = virus!!.crearEspecie("Especie5", "Argentina")
        val especie5Spy = Mockito.spy(especie5)
        Mockito.doReturn(true).`when`(especie5Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(especie5Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        val especie6 = virus!!.crearEspecie("Especie6", "Argentina")
        val especie6Spy = Mockito.spy(especie6)
        Mockito.doReturn(true).`when`(especie6Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(especie6Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        val especie7 = virus!!.crearEspecie("Especie7", "Argentina")
        val especie7Spy = Mockito.spy(especie7)
        Mockito.doReturn(true).`when`(especie7Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(especie7Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        val especie8 = virus!!.crearEspecie("Especie8", "Argentina")
        val especie8Spy = Mockito.spy(especie8)
        Mockito.doReturn(true).`when`(especie8Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(especie8Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        vectorService.infectar(cordobes!!, especie1Spy!!)
        vectorService.infectar(animalCordobes!!, especie1Spy)
        // especie 2 será la que haya infectado mas vectores
        vectorService.infectar(cordobes!!, especie2Spy!!)
        vectorService.infectar(animalCordobes!!, especie2Spy)
        vectorService.infectar(portenho!!, especie2Spy)
        vectorService.infectar(cordobes!!, especie3Spy!!)
        vectorService.infectar(animalCordobes!!, especie3Spy)
        vectorService.infectar(cordobes!!, especie4Spy!!)
        vectorService.infectar(animalCordobes!!, especie4Spy)
        vectorService.infectar(cordobes!!, especie5Spy!!)
        vectorService.infectar(animalCordobes!!, especie5Spy)
        vectorService.infectar(cordobes!!, especie6Spy!!)
        vectorService.infectar(animalCordobes!!, especie6Spy)
        vectorService.infectar(cordobes!!, especie7Spy!!)
        vectorService.infectar(animalCordobes!!, especie7Spy)
        vectorService.infectar(cordobes!!, especie8Spy!!)
        vectorService.infectar(animalCordobes!!, especie8Spy)
        val lideres = estadisticaService.lideres()
        assertEquals(lideres.size, 10)
        assertEquals(lideres.first().nombre, "Especie2")
        assertEquals(lideres.last().nombre, "H1N1")
    }

    @Test
    fun especieLiderTest(){
        var especie1 = virus!!.crearEspecie("Especie1", "Argentina")
        val especie1Spy = Mockito.spy(especie1)
        Mockito.doReturn(true).`when`(especie1Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(especie1Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)
        val especie2 = virus!!.crearEspecie("Especie2", "Argentina")
        val especie2Spy = Mockito.spy(especie2)
        Mockito.doReturn(true).`when`(especie2Spy)!!.esContagioExitoso(TipoDeVectorEnum.Persona)
        Mockito.doReturn(true).`when`(especie2Spy)!!.esContagioExitoso(TipoDeVectorEnum.Animal)

        vectorService.infectar(cordobes!!, especie1Spy)
        vectorService.infectar(cordobes!!, especie2Spy)
        vectorService.infectar(portenho!!, especie1Spy)

        assertEquals("Especie1", estadisticaService.especieLider().nombre)

        vectorService.infectar(portenho!!, especie2Spy)
        vectorService.infectar(barilochense!!, especie2Spy)

        assertEquals("Especie2", estadisticaService.especieLider().nombre)
    }

}