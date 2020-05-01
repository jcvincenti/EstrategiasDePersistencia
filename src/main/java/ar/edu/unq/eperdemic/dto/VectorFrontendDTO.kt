package ar.edu.unq.eperdemic.dto

import ar.edu.unq.eperdemic.modelo.*

class VectorFrontendDTO(val tipoDeVector : TipoDeVector,
                        val nombreDeUbicacionPresente: String) {


    enum class TipoDeVector {
        Persona, Insecto, Animal
    }

    fun aModelo() : Vector {
        var vector = Vector()
        var ubicacion = Ubicacion(nombreDeUbicacionPresente)
        vector.nombreDeLocacionActual = ubicacion
        vector.tipo = tipoDeVector.toString()
        return vector
    }
}