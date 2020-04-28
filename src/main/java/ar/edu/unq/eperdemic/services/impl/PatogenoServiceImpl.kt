package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.exceptions.*


class PatogenoServiceImpl(val patogenoDAO: PatogenoDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Int {
        if (patogenoDAO.existePatogenoConTipo(patogeno.tipo!!)) {
            throw NoSePudoCrearPatogenoException("Ya existe un patogeno de tipo ${patogeno.tipo}")
        }
        return patogenoDAO.crear(patogeno)
    }

    override fun recuperarPatogeno(id: Int): Patogeno {
        if (!patogenoDAO.existePatogenoConId(id)){
            throw NoSePudoRecuperarPatogenoException("Patogeno con id $id inexistente")
        }
        return patogenoDAO.recuperar(id)

    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> = patogenoDAO.recuperarATodos()

    override fun agregarEspecie(id: Int, nombreEspecie: String, paisDeOrigen: String): Especie {
        if (!patogenoDAO.existePatogenoConId(id)) {
            throw NoSePudoAgregarEspecieException("Patogeno con id $id inexistente")
        }
        val patogeno = patogenoDAO.recuperar(id)
        val especieCreada = patogeno.crearEspecie(nombreEspecie,paisDeOrigen)
        patogenoDAO.actualizar(patogeno)
        return especieCreada
    }

    override fun cantidadDeInfectados(especieId: Int): Int {
        TODO("Not yet implemented")
    }

    override fun esPandemia(especieId: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun recuperarEspecie(id: Int): Especie {
        TODO("Not yet implemented")
    }
}