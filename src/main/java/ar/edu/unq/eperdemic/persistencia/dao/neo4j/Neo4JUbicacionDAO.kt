package ar.edu.unq.eperdemic.persistencia.dao.neo4j

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.INeo4JUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.neo4j.Neo4JConnector.execute
import org.neo4j.driver.Values

class Neo4JUbicacionDAO : INeo4JUbicacionDAO{
    override fun crearUbicacion(ubicacion: Ubicacion) : Ubicacion{
        execute { session ->
            session.writeTransaction{
                val query ="CREATE(ubicacion: Ubicacion {nombreUbicacion: \$nombre})"
                it.run(query, Values.parameters("nombre", ubicacion.nombreUbicacion))
            }
        }
        return ubicacion
    }

    fun clear() {
        return execute { session ->
            session.run("MATCH (n) DETACH DELETE n")
        }
    }
}