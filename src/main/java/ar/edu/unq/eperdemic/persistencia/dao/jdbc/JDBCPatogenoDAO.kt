package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import java.sql.Connection


class JDBCPatogenoDAO : PatogenoDAO {

    override fun crear(patogeno: Patogeno): Int {
        TODO("not implemented")
    }

    override fun actualizar(patogeno: Patogeno) {
        TODO("not implemented")
    }

    override fun recuperar(patogenoId: Int): Patogeno {
        TODO("not implemented")
    }

    override fun recuperarATodos(): List<Patogeno> {
        return execute {
            conn: Connection ->
                val ps = conn.prepareStatement("SELECT id, tipo, cantidadDeEspecies FROM patogenos")
                val resultSet = ps.executeQuery()
                val patogenos = mutableListOf<Patogeno>()
                while (resultSet.next()) {
                    var patogeno = Patogeno(resultSet.getString("tipo"))
                    patogeno.cantidadDeEspecies = resultSet.getInt("cantidadDeEspecies")
                    patogeno.id = resultSet.getInt("id")
                }
                ps.close()
                patogenos
        }
    }

    init {
        val initializeScript = javaClass.classLoader.getResource("createAll.sql").readText()
        execute {
            val ps = it.prepareStatement(initializeScript)
            ps.execute()
            ps.close()
            null
        }
    }
}