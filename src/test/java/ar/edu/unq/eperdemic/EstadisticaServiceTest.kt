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


class EstadisticaServiceTest {
    val estadisticaService = EstadisticaServiceImpl()
    var vectorService = VectorServiceImpl(HibernateVectorDAO())
    val patogenoService = PatogenoServiceImpl(HibernatePatogenoDAO())
    val dataService = DataServiceHibernate()
    var virus: Patogeno? = null
    var paperas: Especie? = null
    var corona: Especie? = null
    var cordobes: Vector? = null
    var otroCordobes: Vector? = null

    @BeforeEach
    fun crearSetDeDatosIniciales() {
        dataService.crearSetDeDatosIniciales()
        virus = patogenoService.recuperarPatogeno(1)
        paperas = virus!!.crearEspecie("Paperas", "Yugoslavia")
        corona = virus!!.crearEspecie("Coronavirus","China")
        cordobes = vectorService.recuperarVector(4)
        otroCordobes = vectorService.recuperarVector(2)
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
        vectorService.infectar(otroCordobes!!, corona!!)
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
}