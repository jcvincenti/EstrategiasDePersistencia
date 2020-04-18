package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.exceptions.NoSePudoCrearPatogenoException
import ar.edu.unq.eperdemic.services.exceptions.PatogenoNoEncotradoException

class PatogenoServiceImpl(val patogenoDAO: PatogenoDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Int {
        try {
            return patogenoDAO.crear(patogeno)
        } catch(e: RuntimeException){
            throw NoSePudoCrearPatogenoException("No se pudo crear el patogeno $patogeno")
        }
    }

    override fun recuperarPatogeno(id: Int): Patogeno = patogenoDAO.recuperar(id)

    override fun recuperarATodosLosPatogenos(): List<Patogeno> = patogenoDAO.recuperarATodos()

    override fun agregarEspecie(id: Int, nombreEspecie: String, paisDeOrigen: String): Especie {
        val patogeno = recuperarPatogeno(id)
        try {
            return patogeno.crearEspecie(nombreEspecie,paisDeOrigen)
        } catch(e: RuntimeException){
            throw PatogenoNoEncotradoException("No se encontro el patogeno $patogeno")
        }
    }
}