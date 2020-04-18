package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.services.exceptions.NoSePudoRecuperarPatogenoException
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.utils.jdbc.DataServiceJDBC
import org.junit.Assert
import org.junit.jupiter.api.*

class PatogenoServiceTest {

    private val patogenoService = PatogenoServiceImpl(JDBCPatogenoDAO())
    private val dataService = DataServiceJDBC()

    @BeforeEach
    fun init() {
        dataService.crearSetDeDatosIniciales()
    }
    @AfterEach
    fun cleanUp() {
        dataService.eliminarTodo()
    }

    @Test
    fun testCrearPatogeno() {
        val id = patogenoService.crearPatogeno(Patogeno("test-tipo"))
        Assert.assertEquals(6, id)
    }

    @Test
    fun testRecuperarATodosLosPatogenosConPatogenos() {
        var patogenos = mutableListOf<Patogeno>()
        patogenos.addAll(patogenoService.recuperarATodosLosPatogenos())
        Assert.assertEquals(5, patogenos.size)
        Assert.assertEquals("asarasa", patogenos.first().tipo)
        Assert.assertEquals("virus", patogenos.last().tipo)
    }

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
        Assert.assertEquals("bacteria", patogeno.tipo)
        Assert.assertEquals(0, patogeno.cantidadDeEspecies)
    }

    @Test
    fun testRecuperarPatogenoInexistente(){
        Assertions.assertThrows(NoSePudoRecuperarPatogenoException::class.java, {patogenoService.recuperarPatogeno(75)})
    }
}