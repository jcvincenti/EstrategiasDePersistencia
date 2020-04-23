package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.exceptions.*


class PatogenoServiceImpl(val patogenoDAO: PatogenoDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Int {
        var id =  patogenoDAO.crear(patogeno)
        if (id == 0) {
            throw NoSePudoCrearPatogenoException("Ya existe un patogeno de tipo ${patogeno.tipo}")
        } else {
            return id
        }
    }

    override fun recuperarPatogeno(id: Int): Patogeno {
        try {
            return patogenoDAO.recuperar(id)
        }
        catch(e: RuntimeException) {
            throw NoSePudoRecuperarPatogenoException("Patogeno con id $id inexistente")
        }
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> = patogenoDAO.recuperarATodos()

    override fun agregarEspecie(id: Int, nombreEspecie: String, paisDeOrigen: String): Especie {
        try {
            val patogeno = patogenoDAO.recuperar(id)
            val especieCreada = patogeno.crearEspecie(nombreEspecie,paisDeOrigen)
            patogenoDAO.actualizar(patogeno)
            return especieCreada
        } catch(e: RuntimeException){
            throw NoSePudoAgregarEspecieException(e.message)
        }
    }
}