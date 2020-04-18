package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import java.sql.Connection
import java.sql.Statement


class JDBCPatogenoDAO : PatogenoDAO {

    override fun crear(patogeno: Patogeno): Int {
        return execute { conn: Connection ->
            val ps = conn.prepareStatement("INSERT INTO patogeno (tipo, cantidad_de_especies) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, patogeno.tipo)
            ps.setInt(2, patogeno.cantidadDeEspecies)
            val id = ps.executeUpdate()
            if (ps.updateCount != 1) {
                throw RuntimeException("No se inserto el patogeno $patogeno")
            }
            ps.close()
            id
            }
    }

    override fun actualizar(patogeno: Patogeno) {
        TODO("not implemented")
    }

    override fun recuperar(patogenoId: Int): Patogeno {
        TODO("not implemented")
    }

    override fun recuperarATodos(): List<Patogeno> {
        return execute { conn: Connection ->
            val ps = conn.prepareStatement("SELECT id, tipo, cantidad_de_especies FROM patogeno ORDER BY tipo ASC")
            val resultSet = ps.executeQuery()
            val patogenos = mutableListOf<Patogeno>()
            while (resultSet.next()) {
                var patogeno = Patogeno(resultSet.getString("tipo"))
                patogeno.cantidadDeEspecies = resultSet.getInt("cantidad_de_especies")
                patogeno.id = resultSet.getInt("id")
                patogenos.add(patogeno)
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