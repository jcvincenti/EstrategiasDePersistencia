package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.EspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.services.PatogenoService
import ar.edu.unq.eperdemic.services.exceptions.*
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.services.utils.ObjectStructureUtils
import ar.edu.unq.eperdemic.services.utils.validateEntityExists
import javax.validation.ValidationException


class PatogenoServiceImpl(val patogenoDAO: PatogenoDAO) : PatogenoService {

    val especieDAO: EspecieDAO = HibernateEspecieDAO()

    override fun crearPatogeno(patogeno: Patogeno): Int {
        ObjectStructureUtils.checkEmptyAttributes(patogeno)
        return TransactionRunner.runTrx {
                patogenoDAO.crear(patogeno)
        }
    }

    override fun recuperarPatogeno(id: Int): Patogeno {
        return TransactionRunner.runTrx {
            patogenoDAO.recuperar(id)
        }?: throw EntityNotFoundException("No se encontro un patogeno con el id ${id}")
    }

    override fun recuperarATodosLosPatogenos(): List<Patogeno> = patogenoDAO.recuperarATodos()

    override fun agregarEspecie(id: Int, nombreEspecie: String, paisDeOrigen: String): Especie {
        var especieCreada: Especie? = null
        TransactionRunner.runTrx {
            validateEntityExists<Patogeno>(id)
            val patogeno = patogenoDAO.recuperar(id)
            especieCreada = patogeno!!.crearEspecie(nombreEspecie,paisDeOrigen)
            especieDAO.guardar(especieCreada!!)
            patogenoDAO.actualizar(patogeno)
        }
        return especieCreada!!
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
        return TransactionRunner.runTrx {
            validateEntityExists<Especie>(id)
            especieDAO.recuperar(id)
        }
    }

    override fun actualizarEspecie(especie: Especie) {
        TransactionRunner.runTrx {
            especieDAO.actualizar(especie)
        }
    }
}