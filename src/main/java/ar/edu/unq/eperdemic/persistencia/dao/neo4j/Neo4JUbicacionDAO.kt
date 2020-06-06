package ar.edu.unq.eperdemic.persistencia.dao.neo4j

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.INeo4JUbicacionDAO
import org.neo4j.driver.Record
import org.neo4j.driver.Transaction
import org.neo4j.driver.Values

class Neo4JUbicacionDAO : INeo4JUbicacionDAO{
    override fun crearUbicacion(ubicacion: Ubicacion, tx: Transaction) : Ubicacion{
        val query ="CREATE(ubicacion: Ubicacion {nombreUbicacion: \$nombre})"
        tx.run(query, Values.parameters("nombre", ubicacion.nombreUbicacion))
        return ubicacion
    }

    override fun conectar(nombreUbicacionOrigen: String, nombreUbicacionDestino: String, tipoDeCamino: String, tx: Transaction) {
        val query = "MATCH (ubicacionOrigen: Ubicacion), (ubicacionDestino: Ubicacion)" +
                " WHERE ubicacionOrigen.nombreUbicacion = \$nombreUbicacionOrigen AND ubicacionDestino.nombreUbicacion = \$nombreUbicacionDestino" +
                " CREATE (ubicacionOrigen)-[:$tipoDeCamino]->(ubicacionDestino)"
        tx.run(query, Values.parameters(
                "nombreUbicacionOrigen", nombreUbicacionOrigen,
                "nombreUbicacionDestino", nombreUbicacionDestino))

    }
    override fun conectados(nombreUbicacion: String, tx: Transaction) : List<Ubicacion> {
        val query = "MATCH (Ubicacion{nombreUbicacion: \$nombreUbicacion})-->(ubicacionConectada : Ubicacion)"+
                "RETURN ubicacionConectada"
        val res = tx.run(query, Values.parameters("nombreUbicacion", nombreUbicacion))
        return res.list { record: Record ->
            val ubicacion  = record.get(0)
            val nombre = ubicacion.get("nombreUbicacion").asString()
            Ubicacion(nombre)
        }
    }

    override fun esUbicacionMuyLejana(origen: String, destino: String, tx: Transaction) : Boolean {
        val query = "MATCH (n:Ubicacion {nombreUbicacion: \$origen})-[r*]->(m:Ubicacion {nombreUbicacion: \$destino})" +
                " RETURN SIGN(COUNT(r)) = 0"
        val result = tx.run(query, Values.parameters(
                "origen", origen,
                "destino", destino))
        return result.single().values()[0].asBoolean()
    }

    fun clear(tx: Transaction) {
        tx.run("MATCH (n) DETACH DELETE n")
    }

}