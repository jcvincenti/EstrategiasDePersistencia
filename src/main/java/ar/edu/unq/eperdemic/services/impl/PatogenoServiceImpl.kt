package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.exceptions.*
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import javax.validation.ValidationException


class PatogenoServiceImpl(val patogenoDAO: PatogenoDAO) : PatogenoService {

    override fun crearPatogeno(patogeno: Patogeno): Int {
        return TransactionRunner.runTrx {
            try {
                patogenoDAO.crear(patogeno)
            } catch (exception: ValidationException) {
                throw NoSePudoCrearPatogenoException("La capacidad de contagio debe ser menor o igual a 100")
            }
        }
    }

    override fun recuperarPatogeno(id: Int): Patogeno {
        return TransactionRunner.runTrx {
            patogenoDAO.recuperar(id)
        }
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

    override fun actualizarPatogeno(patogeno: Patogeno) {
        TransactionRunner.runTrx {
            patogenoDAO.actualizar(patogeno)
        }
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