package ar.edu.unq.eperdemic.services

import ar.edu.unq.eperdemic.modelo.Evento

interface FeedService {
    fun feedPatogeno(tipoDePatogeno: String): List<Evento>
    fun feedVector(vectorId: Int): List<Evento>
    fun feedUbicacion(nombreDeUbicacion: String): List<Evento>
}