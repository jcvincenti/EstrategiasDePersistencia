package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.utils.jdbc.DataServiceJDBC
import org.junit.Assert
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class PatogenoServiceTest {

    private val patogenoService = PatogenoServiceImpl(JDBCPatogenoDAO())

    companion object {
        private val dataService = DataServiceJDBC()
        @BeforeAll
        @JvmStatic
        fun init() {
            dataService.crearSetDeDatosIniciales()
        }
        @AfterAll
        @JvmStatic
        fun cleanUp() {
            dataService.eliminarTodo()
        }
    }

    @Test
    fun testCrearPatogeno(){
        var patogeno = Patogeno("coronavirus")
        patogenoService.crearPatogeno(patogeno)
        TODO("cuando este pusheado el get de patogeno, se hace el assert del crear")
    }

    @Test
    fun testRecuperarPatogenos() {
        var patogenos = mutableListOf<Patogeno>()
        patogenos.addAll(patogenoService.recuperarATodosLosPatogenos())
        Assert.assertEquals(6, patogenos.size)
    }
}