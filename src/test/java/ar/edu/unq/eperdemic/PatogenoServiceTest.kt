package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVectorEnum
import ar.edu.unq.eperdemic.modelo.exceptions.CapacidadDeContagioInvalidaException
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.services.exceptions.EmptyPropertyException
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.utils.hibernate.DataServiceHibernate
import junit.framework.Assert.assertFalse
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
        val patogeno = Patogeno("Hongo")
        val exception = assertThrows<CapacidadDeContagioInvalidaException> {patogeno.setCapacidadDeContagio(TipoDeVectorEnum.Persona, 150)}
        Assert.assertEquals("La capacidad de contagio debe ser menor o igual a 100", exception.message )
    }

    @Disabled
    @Test
    fun crearPatogenoTest() {
        //TODO: Reimplementar con Hibernate
        val patogeno = Patogeno("test-tipo")
        patogeno.id = patogenoService.crearPatogeno(patogeno)
        Assert.assertEquals(6, patogeno.id)
        Assert.assertEquals(patogeno.id, patogenoService.recuperarPatogeno(6).id)
    }

    @Test
    fun crearPatogenoSinTipoTest(){
        val patogeno = Patogeno()
        val exception = assertThrows<EmptyPropertyException> { patogenoService.crearPatogeno(patogeno) }

        Assert.assertEquals("La propiedad tipo esta vacia", exception.message)
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

    @Disabled
    @Test
    fun testRecuperarPatogenoExistente(){
        //TODO: Reimplementar con Hibernate
        val patogeno = patogenoService.recuperarPatogeno(1)
        Assert.assertEquals("bacteria", patogeno.tipo)
        Assert.assertEquals(0, patogeno.cantidadDeEspecies)
    }

    @Disabled
    @Test
    fun testAgregarEspecieConPatogenoExistente(){
        //TODO: Reimplementar con Hibernate
        val especie = patogenoService.agregarEspecie(4, "sarampion", "indefinido")
        val patogeno = patogenoService.recuperarPatogeno(4)
        Assert.assertEquals(patogeno.id, especie.patogeno.id)
        Assert.assertEquals("sarampion",especie.nombre)
        Assert.assertEquals("indefinido", especie.paisDeOrigen)
        Assert.assertEquals(1,patogeno.cantidadDeEspecies)
    }

    @Disabled
    @Test
    fun testAgregarEspecieConPatogenoInexistente(){
        // TODO reimplementar
        //val exception = assertThrows<NoSePudoAgregarEspecieException> {patogenoService.agregarEspecie(99, "test-especie", "test-pais")}
        //Assert.assertEquals("Patogeno con id 99 inexistente", exception.message)
    }

    @Test
    fun noEsPandemiaTest() {
        assertFalse(patogenoService.esPandemia(1))
    }

    @Disabled
    @Test
    fun esPandemiaTest(){
        // TODO implementar
    }

}
