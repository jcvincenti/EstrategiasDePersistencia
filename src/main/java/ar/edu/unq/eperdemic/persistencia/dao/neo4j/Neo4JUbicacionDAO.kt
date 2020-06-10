package ar.edu.unq.eperdemic.persistencia.dao.neo4j

import ar.edu.unq.eperdemic.modelo.TipoCaminoEnum
import ar.edu.unq.eperdemic.modelo.TipoDeVectorEnum
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.INeo4JUbicacionDAO
import ar.edu.unq.eperdemic.services.transactions.Neo4JTransaction
import org.neo4j.driver.Record
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
                "RETURN DISTINCT(ubicacionConectada)"
        val res = tx!!.run(query, Values.parameters("nombreUbicacion", nombreUbicacion))
        return res.list { record: Record ->
            val ubicacion  = record.get(0)
            val nombre = ubicacion.get("nombreUbicacion").asString()
            Ubicacion(nombre)
        }
    }

    override fun esUbicacionMuyLejana(origen: String, destino: String) : Boolean {
        val tx = Neo4JTransaction.transaction
        val query = "MATCH (n:Ubicacion {nombreUbicacion: \$origen}), (m:Ubicacion {nombreUbicacion: \$destino})" +
                " RETURN not exists ((n)-->(m)) "
        val exc = tx!!.run(query, Values.parameters(
                "origen", origen,
                "destino", destino))
        val result = exc.list { record: Record ->
            record[0].asBoolean()
        }
        return result[0]
    }

    override fun capacidadDeExpansion(nombreDeUbicacion: String, movimientos: Int, tiposDeCamino: List<String>): Int {
        val tx = Neo4JTransaction.transaction
        var caminos = ""
        tiposDeCamino.forEach { caminos = "$caminos$it>|" }
        val query = "MATCH (u:Ubicacion {nombreUbicacion: \$ubicacion}) " +
                "CALL apoc.path.subgraphNodes(u, {" +
                "    relationshipFilter: \$tiposDeCamino," +
                "    minLevel: 1," +
                "    maxLevel: \$movimientos" +
                "}) " +
                "YIELD node " +
                "RETURN count(node);"
        val result = tx!!.run(query, Values.parameters(
                "ubicacion", nombreDeUbicacion,
                "movimientos", movimientos,
                "tiposDeCamino", caminos))
        return result.single()[0].asInt()
    }

    //TODO: Refactorizar para que devuelva el bool directamente en la query
    override fun conexiones(origen: String, destino: String): List<TipoCaminoEnum> {
        val tx = Neo4JTransaction.transaction
        val query = "MATCH (n:Ubicacion {nombreUbicacion: \$origen})-[r*]->(m:Ubicacion {nombreUbicacion: \$destino})"+
                "RETURN [x in r | type(x)]"
        val exc = tx!!.run(query, Values.parameters(
                "origen", origen,
                "destino", destino))

        val result = exc.list { record: Record ->
            record[0].values().map {
                it.asString()
            }.toList()
        }
        return result[0].map {
            TipoCaminoEnum.parseTipo(it)!!
        }
    }

    fun clear() {
        val tx = Neo4JTransaction.transaction
        tx!!.run("MATCH (n) DETACH DELETE n")
    }

}
