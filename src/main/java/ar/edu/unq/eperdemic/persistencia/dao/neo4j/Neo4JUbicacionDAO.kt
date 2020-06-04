package ar.edu.unq.eperdemic.persistencia.dao.neo4j

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.INeo4JUbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.neo4j.Neo4JConnector.execute
import org.neo4j.driver.Record
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
                val query = "MATCH (ubicacionOrigen: Ubicacion), (ubicacionDestino: Ubicacion)" +
                        " WHERE ubicacionOrigen.nombreUbicacion = \$nombreUbicacionOrigen AND ubicacionDestino.nombreUbicacion = \$nombreUbicacionDestino" +
                        " CREATE (ubicacionOrigen)-[:$tipoDeCamino]->(ubicacionDestino)"
                it.run(query, Values.parameters(
                        "nombreUbicacionOrigen", nombreUbicacionOrigen,
                        "nombreUbicacionDestino", nombreUbicacionDestino)
                )
            }
        }
    }
    override fun conectados(nombreUbicacion: String) : List<Ubicacion> {
        return execute { session ->
            session.writeTransaction {
                val query = "MATCH (Ubicacion{nombreUbicacion: \$nombreUbicacion})--(ubicacionConectada : Ubicacion)"+
                        "RETURN ubicacionConectada"
                val res = it.run(query, Values.parameters("nombreUbicacion", nombreUbicacion))
                res.list { record: Record ->
                    val ubicacion  = record.get(0)
                    val nombreUbicacion = ubicacion.get("nombreUbicacion").asString()
                    Ubicacion(nombreUbicacion)
                }
            }
        }
    }

    fun clear() {
        return execute { session ->
            session.run("MATCH (n) DETACH DELETE n")
        }
    }
}