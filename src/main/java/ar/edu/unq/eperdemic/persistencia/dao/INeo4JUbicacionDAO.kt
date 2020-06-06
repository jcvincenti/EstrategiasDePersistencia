package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Ubicacion
import org.neo4j.driver.Transaction

interface INeo4JUbicacionDAO {
    fun crearUbicacion(ubicacion: Ubicacion) : Ubicacion
    fun conectar(nombreUbicacionOrigen:String, nombreUbicacionDestino:String, tipoDeCamino:String)
    fun conectados(nombreUbicacion: String) : List<Ubicacion>
    fun esUbicacionMuyLejana(origen:String, destino:String) : Boolean
}