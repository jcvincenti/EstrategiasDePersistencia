package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.services.utils.ObjectStructureUtils
import org.hibernate.exception.ConstraintViolationException
import kotlin.random.Random

open class VectorServiceImpl(val vectorDAO: VectorDAO) : VectorService {

    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        TransactionRunner.runTrx {
            vectores.forEach {
                vector -> vectorInfectado.contagiar(vector)
                actualizarVector(vector)
            }
        }
    }

     override fun infectar(vector: Vector, especie: Especie) {
        vector.infectar(especie)
        TransactionRunner.runTrx {
            vectorDAO.actualizar(vector)
        }
    }

    override fun enfermedades(vectorId: Int): List<Especie> {
        return TransactionRunner.runTrx {
            vectorDAO.recuperar(vectorId)!!.especies
        }
    }

    override fun crearVector(vector: Vector): Vector {
        ObjectStructureUtils.checkEmptyAttributes(vector)
        try {
            TransactionRunner.runTrx {
                vectorDAO.guardar(vector)
            }
        } catch (exception: ConstraintViolationException) {
            throw EntityNotFoundException("No se encontro la ubicacion ${vector.nombreDeLocacionActual}")
        }
        return vector
    }

    override fun recuperarVector(vectorId: Int): Vector {
        return TransactionRunner.runTrx {
            vectorDAO.recuperar(vectorId)
        } ?: throw EntityNotFoundException("No se encontro un vector con el id ${vectorId}")
    }

    override fun borrarVector(vectorId: Int) {
        TransactionRunner.runTrx {
            val vector = Vector()
            vector.id = vectorId
            vectorDAO.borrar(vector)
        }
    }

    fun actualizarVector(vector: Vector){
        TransactionRunner.runTrx {
            vectorDAO.actualizar(vector)
        }
    }

    fun getVectoresByLocacion(locacion: String?): List<Vector> {
        return TransactionRunner.runTrx {
            vectorDAO.getVectoresByLocacion(locacion)
        }
    }
}