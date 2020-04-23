package ar.edu.unq.eperdemic.persistencia.dao.jdbc

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.jdbc.JDBCConnector.execute
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Statement


class JDBCPatogenoDAO : PatogenoDAO {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun crear(patogeno: Patogeno): Int {

        return execute { conn: Connection ->
            var id = 0
            val ps = conn.prepareStatement("INSERT INTO patogeno (tipo, cantidad_de_especies) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, patogeno.tipo)
            ps.setInt(2, patogeno.cantidadDeEspecies)
            try {
                ps.executeUpdate()
            } catch(e: SQLIntegrityConstraintViolationException){
                logger.error(e.message)
            }
            val rs = ps.generatedKeys
            if (rs.next()) {
                id = rs.getInt(1)
            }
            ps.close()
            id
        }
    }

    override fun actualizar(patogeno: Patogeno) {
        execute { conn: Connection ->
            val ps = conn.prepareStatement("UPDATE patogeno SET tipo = ?, cantidad_de_especies = ? WHERE id = ?")
            ps.setString(1, patogeno.tipo)
            ps.setInt(2, patogeno.cantidadDeEspecies)
            patogeno.id?.let { ps.setInt(3, it) }
            ps.executeUpdate()
            if (ps.updateCount != 1) {
                throw RuntimeException("No se actualizo el patogeno $patogeno")
            }
            ps.close()
        }
    }

    override fun recuperar(patogenoId: Int): Patogeno {
        return execute { conn: Connection ->
            val ps = conn.prepareStatement("SELECT * FROM patogeno WHERE id = ?")
            ps.setInt(1, patogenoId)
            val resultSet = ps.executeQuery()

            if (!resultSet.next()){
                throw RuntimeException("No existe un patogeno con el id $patogenoId")
            }

            val patogeno = Patogeno(resultSet.getString("tipo"))
            patogeno.id = resultSet.getInt("id")
            patogeno.cantidadDeEspecies = resultSet.getInt("cantidad_de_especies")

            ps.close()
            patogeno
        }
    }

    override fun recuperarATodos(): List<Patogeno> {
        return execute { conn: Connection ->
            val ps = conn.prepareStatement("SELECT * FROM patogeno ORDER BY tipo ASC")
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