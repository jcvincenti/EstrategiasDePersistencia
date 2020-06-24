package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.mongo.MongoEventoDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException
import ar.edu.unq.eperdemic.services.utils.ObjectStructureUtils
import ar.edu.unq.eperdemic.services.utils.*

open class VectorServiceImpl(val vectorDAO: VectorDAO) : VectorService {
    val mongoDao = MongoEventoDAO()

    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        TransactionRunner.runTrx {
            vectores.forEach { vector ->
                if (vectorInfectado.contagiar(vector)) {
                    mongoDao.logearEvento(Evento.buildEventoContagio(
                            vectorInfectado.id,
                            vector.nombreDeLocacionActual,
                            null,
                            null,
                            null,
                            "El vector ${vectorInfectado.id} contagio al vector ${vector.id} en la ubicacion ${vector.nombreDeLocacionActual}")
                    )
                }
                actualizarVector(vector)
            }
        }
    }

    override fun infectar(vector: Vector, especie: Especie) {
        TransactionRunner.runTrx {
            vector.infectar(especie)
            vectorDAO.actualizar(vector)
        }
    }

    override fun enfermedades(vectorId: Int): List<Especie> {
        val enfermedades = vectorDAO.enfermedades(vectorId)
        val nombreEspecies = enfermedades.map{ especie -> especie.nombre }
        mongoDao.logearEvento(Evento.buildEventoContagio(
                vectorId,
                null,
                null,
                null,
                nombreEspecies,
                "El vector id ${vectorId} está infectado con las siguientes especies: ${nombreEspecies}")
                )
        return TransactionRunner.runTrx {
            vectorDAO.enfermedades(vectorId)
        }
    }

    override fun crearVector(vector: Vector): Vector {
        ObjectStructureUtils.checkEmptyAttributes(vector)
        TransactionRunner.runTrx {
            validateEntityExists<Ubicacion>(vector.nombreDeLocacionActual)
            vectorDAO.guardar(vector)
        }
        return vector
    }

    override fun recuperarVector(vectorId: Int): Vector {
        return TransactionRunner.runTrx {
            vectorDAO.recuperar(vectorId)
        } ?: throw EntityNotFoundException("La entidad Vector con id ${vectorId} no existe")
    }

    override fun borrarVector(vectorId: Int) {
        TransactionRunner.runTrx { validateEntityExists<Vector>(vectorId) }
        TransactionRunner.runTrx {
            val vector = Vector()
            vector.id = vectorId
            vectorDAO.borrar(vector)
        }
    }

    fun actualizarVector(vector: Vector) {
        TransactionRunner.runTrx {
            vectorDAO.actualizar(vector)
        }
    }

    fun getVectoresByLocacion(locacion: String?): List<Vector> {
        return TransactionRunner.runTrx {
            validateEntityExists<Ubicacion>(locacion!!)
            vectorDAO.getVectoresByLocacion(locacion)
        }
    }

    fun getVectorRandomEnLocacion(locacion: String?): Vector? {
        return TransactionRunner.runTrx {
            validateEntityExists<Ubicacion>(locacion!!)
            vectorDAO.getVectorRandomEnLocacion(locacion)
        }
    }
}