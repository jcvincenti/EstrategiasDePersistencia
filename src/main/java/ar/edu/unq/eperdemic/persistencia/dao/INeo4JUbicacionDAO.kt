package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Ubicacion

interface INeo4JUbicacionDAO {
    fun crearUbicacion(ubicacion: Ubicacion) : Ubicacion
}