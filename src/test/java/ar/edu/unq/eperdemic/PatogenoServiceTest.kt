package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.dto.VectorFrontendDTO
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVectorEnum
import ar.edu.unq.eperdemic.modelo.exceptions.CapacidadDeContagioInvalidaException
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.exceptions.EmptyPropertyException
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl
import ar.edu.unq.eperdemic.services.impl.VectorServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import junit.framework.Assert.*
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.jupiter.api.*

class PatogenoServiceTest {

    private val patogenoService = PatogenoServiceImpl(HibernatePatogenoDAO())
    private val vectorService = VectorServiceImpl(HibernateVectorDAO())
    private val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())
    private val dataService = DataServiceHibernate()

    @BeforeEach
    fun init() {
        dataService.crearSetDeDatosIniciales()
    }
    @AfterEach
    fun cleanUp() {
        dataService.eliminarTodo()
    }

    @Test
    fun setFactorDeContagioMayorACienTest() {
        val patogeno = Patogeno("Hongo")
        val exception = assertThrows<CapacidadDeContagioInvalidaException> {patogeno.setCapacidadDeContagio(TipoDeVectorEnum.Persona, 150)}
        Assert.assertEquals("La capacidad de contagio debe ser menor o igual a 100", exception.message )
    }

    @Test
    fun crearPatogenoTest() {
        val patogeno = Patogeno("test-tipo")
        patogeno.id = patogenoService.crearPatogeno(patogeno)
        Assert.assertEquals(3, patogeno.id)
        Assert.assertEquals(patogeno.id, patogenoService.recuperarPatogeno(3).id)
    }

    @Test
    fun crearPatogenoSinTipoTest(){
        val patogeno = Patogeno()
        val exception = assertThrows<EmptyPropertyException> { patogenoService.crearPatogeno(patogeno) }

        Assert.assertEquals("La propiedad tipo esta vacia", exception.message)
    }

    @Disabled
    @Test
    fun crearPatogenoExistenteTest() {
        //TODO: Reimplementer con Hibernate
        /*
        val patogeno = Patogeno("bacteria")
        Assertions.assertThrows(NoSePudoCrearPatogenoException::class.java) {patogenoService.crearPatogeno(patogeno)}
        val exception = assertThrows<NoSePudoCrearPatogenoException> {patogenoService.crearPatogeno(patogeno)}
        Assert.assertEquals("Ya existe un patogeno de tipo ${patogeno.tipo}", exception.message )
        */
    }

    @Disabled
    @Test
    fun testRecuperarATodosLosPatogenosConPatogenos() {
        //TODO: Reimplementar con Hibernate
        val patogenos = mutableListOf<Patogeno>()
        patogenos.addAll(patogenoService.recuperarATodosLosPatogenos())
        Assert.assertEquals(5, patogenos.size)
        Assert.assertEquals("asarasa", patogenos.first().tipo)
        Assert.assertEquals("virus", patogenos.last().tipo)
    }

    @Disabled
    @Test
    fun testRecuperarATodosLosPatogenosSinPatogenos() {
        //TODO: Reimplementar con Hibernate
        this.cleanUp()
        val patogenos = mutableListOf<Patogeno>()
        patogenos.addAll(patogenoService.recuperarATodosLosPatogenos())
        Assert.assertTrue(patogenos.isEmpty())
    }

    @Test
    fun testRecuperarPatogenoExistente(){
        val patogeno = patogenoService.recuperarPatogeno(1)
        Assert.assertEquals("Virus", patogeno.tipo)
        Assert.assertEquals(2, patogeno.cantidadDeEspecies)
    }

    @Disabled
    @Test
    fun testAgregarEspecieConPatogenoExistente(){
        var especie = patogenoService.agregarEspecie(1, "sarampion", "indefinido")
        var patogeno = patogenoService.recuperarPatogeno(1)
        Assert.assertEquals(patogeno.id, especie.patogeno!!.id)
        Assert.assertEquals("sarampion",especie.nombre)
        Assert.assertEquals("indefinido", especie.paisDeOrigen)
        Assert.assertEquals(3,patogeno.cantidadDeEspecies)
    }

    @Test
    fun testAgregarEspecieConPatogenoInexistente(){
        val exception = assertThrows<EntityNotFoundException> {patogenoService.agregarEspecie(99, "test-especie", "test-pais")}
        Assert.assertEquals("La entidad Patogeno con id 99 no existe", exception.message)
    }

    @Test
    fun recuperarEspecieTest() {
        val especie = patogenoService.recuperarEspecie(1)

        Assert.assertEquals("Gripe", especie.nombre)
        Assert.assertEquals("China", especie.paisDeOrigen)
    }

    @Test
    fun recuperarEspecieInexistenteTest() {
        val exception = assertThrows<EntityNotFoundException> {patogenoService.recuperarEspecie(100)}
        Assert.assertEquals("La entidad Especie con id 100 no existe", exception.message)
    }

    @Test
    fun noEsPandemiaTest() {
        assertFalse(patogenoService.esPandemia(1))
    }

    @Test
    fun esPandemiaTest(){
        val gripe = patogenoService.recuperarEspecie(1)
        val cordobes = vectorService.recuperarVector(4)
        val barilochense = vectorService.recuperarVector(3)
        val pampeano = VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Persona,"La Pampa")
                .aModelo()
        val quilmenho = VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Persona,"Quilmes")
                .aModelo()
        val vectores = listOf(cordobes, barilochense, pampeano, quilmenho)
        vectores.forEach {
                it.especies.add(gripe)
        }

        vectorService.crearVector(pampeano)
        vectorService.crearVector(quilmenho)
        vectorService.actualizarVector(cordobes)
        vectorService.actualizarVector(barilochense)

        assertEquals(5, patogenoService.cantidadUbicacionesDeEspecie(1))
        assertEquals(9, ubicacionService.cantidadUbicaciones())
        assertTrue(patogenoService.esPandemia(1))
    }

}
