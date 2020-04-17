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
        tiposDePatogenos.forEach{
            patogeno -> dao.crear(Patogeno(patogeno))
        }
    }

    override fun eliminarTodo() {
        TODO("hacer un eliminar solo de los datos que insertamos en el test, acá estoy limpiando toda la tabla")
        return JDBCConnector.execute { conn: Connection ->
            val ps = conn.prepareStatement("DELETE FROM patogeno")
            ps.executeUpdate()
            if (ps.updateCount != 1) {
                throw RuntimeException("No se pudo eliminar los datos de la tabla patogeno")
            }
            ps.close()
            null
        }
    }
}