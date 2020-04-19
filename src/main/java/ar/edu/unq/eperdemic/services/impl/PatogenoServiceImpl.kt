package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.exceptions.*


class PatogenoServiceImpl(val patogenoDAO: PatogenoDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Int {
        try {
            return patogenoDAO.crear(patogeno)
        } catch(e: RuntimeException){
            throw NoSePudoCrearPatogenoException("No se pudo crear el patogeno $patogeno")
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