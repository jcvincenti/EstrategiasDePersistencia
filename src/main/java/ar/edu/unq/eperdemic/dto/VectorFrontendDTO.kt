package ar.edu.unq.eperdemic.dto

import ar.edu.unq.eperdemic.modelo.*

class VectorFrontendDTO(val tipoDeVector : TipoDeVector,
                        val nombreDeUbicacionPresente: String) {

    enum class TipoDeVector {
        Persona, Insecto, Animal
    }

    fun aModelo() : Vector {
        var vector = Vector()
        vector.nombreDeLocacionActual = nombreDeUbicacionPresente
        vector.tipo = tipoDeVector.toString()
        vector.contagioStrategy = buildContagioStrategy()
        return vector
    }

    private fun buildContagioStrategy() : ContagioStrategy{
        return when(tipoDeVector) {
            TipoDeVector.Persona -> Persona()
            TipoDeVector.Animal -> Animal()
            TipoDeVector.Insecto -> Insecto()
        }
    }
}