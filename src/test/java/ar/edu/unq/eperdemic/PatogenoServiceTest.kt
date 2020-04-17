package ar.edu.unq.eperdemic

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.services.impl.PatogenoServiceImpl
import ar.edu.unq.eperdemic.utils.jdbc.DataServiceJDBC
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test


class PatogenoServiceTest {
    val patogenoService = PatogenoServiceImpl(JDBCPatogenoDAO())
    val dataService = DataServiceJDBC()

    @Before
    fun init() {
        dataService.crearSetDeDatosIniciales()
    }
    @Test
    fun testCrearPatogeno(){
        var patogeno = Patogeno("coronavirus")
        Assert.assertEquals(1, patogenoService.crearPatogeno(patogeno));
    }

    @After
    fun cleanUp() {
        dataService.eliminarTodo()
    }
}