package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService

class PatogenoServiceImpl(patogenoDAO: PatogenoDAO) : PatogenoService {
    override fun crearPatogeno(patogeno: Patogeno): Int {
        TODO("not implemented")
    }

    override fun recuperarPatogeno(id: Int): Patogeno {
        TODO("not implemented")
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> {
        TODO("not implemented")
    }

    override fun agregarEspecie(id: Int, nombreEspecie: String, paisDeOrigen: String): Especie {
        TODO("not implemented")
    }
}