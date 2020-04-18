package ar.edu.unq.eperdemic.modelo

import java.io.Serializable

class Patogeno(val tipo: String) : Serializable{

    var id : Int? = null
    var cantidadDeEspecies: Int = 0

    override fun toString(): String {
        return tipo
    }

    fun crearEspecie(nombreEspecie: String, paisDeOrigen: String) : Especie{
        cantidadDeEspecies++
        return Especie(this, nombreEspecie, paisDeOrigen)
    }
}