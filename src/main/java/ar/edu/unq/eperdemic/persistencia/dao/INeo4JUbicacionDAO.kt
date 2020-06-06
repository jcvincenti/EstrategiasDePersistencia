package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Ubicacion
import org.neo4j.driver.Transaction

interface INeo4JUbicacionDAO {
    fun crearUbicacion(ubicacion: Ubicacion, tx: Transaction) : Ubicacion
    fun conectar(nombreUbicacionOrigen:String, nombreUbicacionDestino:String, tipoDeCamino:String, tx: Transaction)
    fun conectados(nombreUbicacion: String, tx: Transaction) : List<Ubicacion>
    fun esUbicacionMuyLejana(origen:String, destino:String, tx: Transaction) : Boolean
}