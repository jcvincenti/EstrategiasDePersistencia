package ar.edu.unq.eperdemic.utils.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCPatogenoDAO
import ar.edu.unq.eperdemic.utils.DataService
import java.sql.Connection

class DataServiceJDBC : DataService {
    val dao = JDBCPatogenoDAO()

    override fun crearSetDeDatosIniciales() {
        var tiposDePatogenos = mutableListOf("bacteria", "hongo", "protozo", "virus")
        tiposDePatogenos.forEach {
            tipo -> dao.crear(Patogeno(tipo))
        }
    }

    override fun eliminarTodo() {
        return JDBCConnector.execute { conn: Connection ->
            val st = conn.createStatement()
            st.executeUpdate("DELETE FROM patogeno")
            st.close()
            null
        }
    }
}