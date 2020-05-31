package ar.edu.unq.eperdemic.utils.neo4j

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.neo4j.Neo4JUbicacionDAO
import ar.edu.unq.eperdemic.utils.DataService

class DataServiceNeo4j : DataService {

    val ubicaciones= crearUbicaciones()
    val dao = Neo4JUbicacionDAO()
    private fun crearUbicaciones() : List<Ubicacion> {
        val nombres = mutableListOf("Entre Rios", "La Pampa", "Catamarca", "Buenos Aires", "Cordoba", "Bariloche", "Quilmes", "Berazategui", "Lanus")
        return nombres.map{u-> Ubicacion(u) }
    }

    override fun crearSetDeDatosIniciales() {
        ubicaciones.forEach{
            dao.crearUbicacion(it)
        }
    }

    override fun eliminarTodo() {
        dao.clear()
    }

}