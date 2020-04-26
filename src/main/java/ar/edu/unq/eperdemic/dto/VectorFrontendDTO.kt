package ar.edu.unq.eperdemic.dto

import ar.edu.unq.eperdemic.modelo.ContagioStrategy
import ar.edu.unq.eperdemic.modelo.Vector

class VectorFrontendDTO(val tipoDeVector : TipoDeVector,
                        val nombreDeUbicacionPresente: String) {

    enum class TipoDeVector {
        Persona, Insecto, Animal
    }

    fun aModelo() : Vector {
        var vector = Vector()
        vector.nombreDeLocacionActual = nombreDeUbicacionPresente
        vector.tipo = tipoDeVector.toString()
        vector.contagioStrategy = Class.forName(tipoDeVector.toString())?.newInstance() as ContagioStrategy
        return vector
    }
}