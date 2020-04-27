package ar.edu.unq.eperdemic.dto

import ar.edu.unq.eperdemic.modelo.*
import kotlin.reflect.full.primaryConstructor

class VectorFrontendDTO(val tipoDeVector : TipoDeVector,
                        val nombreDeUbicacionPresente: String) {

    enum class TipoDeVector {
        Persona, Insecto, Animal;
    }

    fun aModelo() : Vector {
        var vector = Vector()
        vector.nombreDeLocacionActual = nombreDeUbicacionPresente
        vector.tipo = tipoDeVector.toString()
        vector.contagioStrategy = buildContagioStrategy(tipoDeVector)
        return vector
    }

    private fun buildContagioStrategy(tipo: TipoDeVector) : ContagioStrategy{
        return when(tipoDeVector) {
            TipoDeVector.Persona -> Persona()
            TipoDeVector.Animal -> Animal()
            TipoDeVector.Insecto -> Insecto()
        }
    }
}