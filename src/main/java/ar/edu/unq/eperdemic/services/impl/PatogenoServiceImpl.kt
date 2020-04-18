package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService

class PatogenoServiceImpl(val patogenoDAO: PatogenoDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Int = patogenoDAO.crear(patogeno)

    override fun recuperarPatogeno(id: Int): Patogeno = patogenoDAO.recuperar(id)

    override fun recuperarATodosLosPatogenos(): List<Patogeno> = patogenoDAO.recuperarATodos()

    override fun agregarEspecie(id: Int, nombreEspecie: String, paisDeOrigen: String): Especie {
        TODO("not implemented")
    }
}