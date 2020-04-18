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
    fun testCrearPatogeno() {
        
    }

}