package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.persistencia.dao.mongo.MongoEventoDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException
import ar.edu.unq.eperdemic.services.utils.ObjectStructureUtils
import ar.edu.unq.eperdemic.services.utils.*

open class VectorServiceImpl(val vectorDAO: VectorDAO) : VectorService {
    val mongoDao = MongoEventoDAO()
    val patogenoService = PatogenoServiceImpl(HibernatePatogenoDAO())

    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        TransactionRunner.runTrx {
            vectores.forEach { vector ->

                val vectores = getVectoresByLocacion(vector.nombreDeLocacionActual)
                val especiesAnteriores = vectores.map { it.especies }.flatten()
                val especiesNoPandemicas: List<Especie> = vectorInfectado.especies.filter { !patogenoService.esPandemia(it.id) }
                
                if (vectorInfectado.contagiar(vector)) {
                    logearContagio(vectorInfectado, vector)
                    logearEnfermedadesQuePadece(vector)

                    val especiesALogear = vector.especies

                    especiesALogear.removeAll(especiesAnteriores)
                    especiesALogear.forEach {
                        logearAparicion(it, vector.nombreDeLocacionActual)
                    }
                }
                actualizarVector(vector)
                especiesNoPandemicas.forEach {
                    if (patogenoService.esPandemia(it.id)) {
                        logearPandemia(it)
                    }
                }
            }
        }
    }

    private fun logearContagio(vectorInfectado: Vector, vector: Vector) {
        mongoDao.logearEvento(Evento.buildEventoContagio(
                vectorInfectado.id,
                vector.nombreDeLocacionActual,
                null,
                null,
                null,
                "El vector ${vectorInfectado.id} contagio al vector ${vector.id} en la ubicacion ${vector.nombreDeLocacionActual}")
        )
    }

    private fun logearEnfermedadesQuePadece(vector: Vector) {
        val nombreEspecies = vector.especies.map{it.nombre}

        mongoDao.logearEvento(Evento.buildEventoContagio(
                vector.id,
                null,
                null,
                null,
                nombreEspecies,
                "El vector id ${vector.id} está infectado con las siguientes especies: ${nombreEspecies}")
        )
    }

    override fun infectar(vector: Vector, especie: Especie) {
        val vectores = getVectoresByLocacion(vector.nombreDeLocacionActual)
        val especiesAnteriores = vectores.map { it.especies }.flatten()
        val eraPandemia = patogenoService.esPandemia(especie.id)

        TransactionRunner.runTrx {
            vector.infectar(especie)
            vectorDAO.actualizar(vector)
        }
        val especiesPosteriores = vectores.map { it.especies }.flatten()

        // Si la lista es mayor, quiere decir que la especie no estaba previamente y que el contagio fue exitoso
        if (especiesPosteriores.size > especiesAnteriores.size) {
            logearAparicion(especie, vector.nombreDeLocacionActual)
        }

        if (!eraPandemia && patogenoService.esPandemia(especie.id)) {
            logearPandemia(especie)
        }
    }

    private fun logearAparicion(especie: Especie, ubicacion: String) {
        mongoDao.logearEvento(Evento.buildEventoContagio(
                null,
                null,
                especie.patogeno.tipo,
                especie.nombre,
                null,
                "La especie ${especie.nombre} del patogeno ${especie.patogeno.tipo} aparecio en ${ubicacion}"
        ))
    }

    private fun logearPandemia(especie: Especie) {
        mongoDao.logearEvento(Evento.buildEventoContagio(
                null,
                null,
                especie.patogeno.tipo,
                especie.nombre,
                null,
                "La especie ${especie.nombre} del patogeno ${especie.patogeno.tipo} es pandemia"
        ))
    }

    override fun enfermedades(vectorId: Int): List<Especie> {
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