package ar.edu.unq.eperdemic.persistencia.dao.neo4j

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.INeo4JUbicacionDAO
import ar.edu.unq.eperdemic.services.transactions.Neo4JTransaction
import org.neo4j.driver.Record
import org.neo4j.driver.Transaction
import org.neo4j.driver.Values

class Neo4JUbicacionDAO : INeo4JUbicacionDAO{
    override fun crearUbicacion(ubicacion: Ubicacion) : Ubicacion{
        val tx = Neo4JTransaction.transaction
        val query ="CREATE(ubicacion: Ubicacion {nombreUbicacion: \$nombre})"
        tx!!.run(query, Values.parameters("nombre", ubicacion.nombreUbicacion))
        return ubicacion
    }

    override fun conectar(nombreUbicacionOrigen: String, nombreUbicacionDestino: String, tipoDeCamino: String) {
        val tx = Neo4JTransaction.transaction
        val query = "MATCH (ubicacionOrigen: Ubicacion), (ubicacionDestino: Ubicacion)" +
                " WHERE ubicacionOrigen.nombreUbicacion = \$nombreUbicacionOrigen AND ubicacionDestino.nombreUbicacion = \$nombreUbicacionDestino" +
                " CREATE (ubicacionOrigen)-[:$tipoDeCamino]->(ubicacionDestino)"
        tx!!.run(query, Values.parameters(
                "nombreUbicacionOrigen", nombreUbicacionOrigen,
                "nombreUbicacionDestino", nombreUbicacionDestino))

    }
    override fun conectados(nombreUbicacion: String) : List<Ubicacion> {
        val tx = Neo4JTransaction.transaction
        val query = "MATCH (Ubicacion{nombreUbicacion: \$nombreUbicacion})-->(ubicacionConectada : Ubicacion)"+
                "RETURN ubicacionConectada"
        val res = tx!!.run(query, Values.parameters("nombreUbicacion", nombreUbicacion))
        return res.list { record: Record ->
            val ubicacion  = record.get(0)
            val nombre = ubicacion.get("nombreUbicacion").asString()
            Ubicacion(nombre)
        }
    }

    override fun esUbicacionMuyLejana(origen: String, destino: String) : Boolean {
        val tx = Neo4JTransaction.transaction
        val query = "MATCH (n:Ubicacion {nombreUbicacion: \$origen})-[r*]->(m:Ubicacion {nombreUbicacion: \$destino})" +
                " RETURN SIGN(COUNT(r)) = 0"
        val result = tx!!.run(query, Values.parameters(
                "origen", origen,
                "destino", destino))
        return result.single().values()[0].asBoolean()
    }

    fun clear() {
        val tx = Neo4JTransaction.transaction
        tx!!.run("MATCH (n) DETACH DELETE n")
    }

}