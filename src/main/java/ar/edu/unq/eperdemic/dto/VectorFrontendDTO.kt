package ar.edu.unq.eperdemic.dto

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.services.impl.UbicacionServiceImpl

class VectorFrontendDTO(val tipoDeVector : TipoDeVector,
                        val nombreDeUbicacionPresente: String) {

    val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())

    enum class TipoDeVector {
        Persona, Insecto, Animal
    }

    fun aModelo() : Vector {
        var vector = Vector()
        val ubicacion = ubicacionService.recuperarUbicacion(nombreDeUbicacionPresente)
        vector.nombreDeLocacionActual = ubicacion
        vector.tipo = tipoDeVector.toString()
        return vector
    }
}