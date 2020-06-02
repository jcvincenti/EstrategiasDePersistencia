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

    override fun conectar(nombreUbicacionOrigen: String, nombreUbicacionDestino: String, tipoDeCamino: String) {
        execute { session ->
            session.writeTransaction{
                val query = "MATCH (ubicacion1: Ubicacion), (ubicacion2: Ubicacion)" +
                        " WHERE ubicacion1.nombreUbicacion = \$nombreUbicacion1 AND ubicacion2.nombreUbicacion = \$nombreUbicacion2" +
                        " CREATE (ubicacion1)-[:" + tipoDeCamino + "]->(ubicacion2)"
                it.run(query, Values.parameters(
                        "nombreUbicacion1", nombreUbicacionOrigen,
                        "nombreUbicacion2", nombreUbicacionDestino)
                )
            }
        }
    }

    fun clear() {
        return execute { session ->
            session.run("MATCH (n) DETACH DELETE n")
        }
    }
}