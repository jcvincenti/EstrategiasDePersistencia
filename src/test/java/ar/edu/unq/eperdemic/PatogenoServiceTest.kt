package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVectorEnum
import ar.edu.unq.eperdemic.modelo.exceptions.CapacidadDeContagioInvalidaException
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.services.exceptions.*
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import ar.edu.unq.eperdemic.utils.jdbc.DataServiceJDBC
import org.junit.Assert
import org.junit.jupiter.api.*

class PatogenoServiceTest {

    private val patogenoService = PatogenoServiceImpl(HibernatePatogenoDAO())
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
        var patogeno = Patogeno("Hongo")
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
        val patogeno = Patogeno("bacteria")
        Assertions.assertThrows(NoSePudoCrearPatogenoException::class.java) {patogenoService.crearPatogeno(patogeno)}
        val exception = assertThrows<NoSePudoCrearPatogenoException> {patogenoService.crearPatogeno(patogeno)}
        Assert.assertEquals("Ya existe un patogeno de tipo ${patogeno.tipo}", exception.message )
    }

    @Disabled
    @Test
    fun testRecuperarATodosLosPatogenosConPatogenos() {
        var patogenos = mutableListOf<Patogeno>()
        patogenos.addAll(patogenoService.recuperarATodosLosPatogenos())
        Assert.assertEquals(5, patogenos.size)
        Assert.assertEquals("asarasa", patogenos.first().tipo)
        Assert.assertEquals("virus", patogenos.last().tipo)
    }

    @Disabled
    @Test
    fun testRecuperarATodosLosPatogenosSinPatogenos() {
        this.cleanUp()
        var patogenos = mutableListOf<Patogeno>()
        patogenos.addAll(patogenoService.recuperarATodosLosPatogenos())
        Assert.assertTrue(patogenos.isEmpty())
    }

    @Test
    fun testRecuperarPatogenoExistente(){
        var patogeno = patogenoService.recuperarPatogeno(1)
        Assert.assertEquals("Virus", patogeno.tipo)
        Assert.assertEquals(2, patogeno.cantidadDeEspecies)
    }

    @Disabled
    @Test
    fun testMensajeExceptionPatogenoInexistente() {
        val exception = assertThrows<NoSePudoRecuperarPatogenoException> {patogenoService.recuperarPatogeno(80)}
        Assert.assertEquals("Patogeno con id 80 inexistente", exception.message )
    }

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
}