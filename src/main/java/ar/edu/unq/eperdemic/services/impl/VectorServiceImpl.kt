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

                if (vectorInfectado.contagiar(vector)) {
                    vector.especies.forEach {
                        if (patogenoService.esPandemia(it.id)) {
                            mongoDao.logearEvento(Evento.buildEventoContagio(
                                    null,
                                    null,
                                    it.patogeno.tipo,
                                    it.nombre,
                                    null,
                                    "La especie ${it.nombre} del patogeno ${it.patogeno.tipo} es pandemia"
                            ))
                        }
                    }

                    mongoDao.logearEvento(Evento.buildEventoContagio(
                            vectorInfectado.id,
                            vector.nombreDeLocacionActual,
                            null,
                            null,
                            null,
                            "El vector ${vectorInfectado.id} contagio al vector ${vector.id} en la ubicacion ${vector.nombreDeLocacionActual}")
                    )
                    var nombreEspecies = vector.especies.map{it.nombre}
                    mongoDao.logearEvento(Evento.buildEventoContagio(
                            vector.id,
                            null,
                            null,
                            null,
                            nombreEspecies,
                            "El vector id ${vector.id} está infectado con las siguientes especies: ${nombreEspecies}")
                    )
                    val especiesALogear = vector.especies

                    especiesALogear.removeAll(especiesAnteriores)
                    especiesALogear.forEach {
                        mongoDao.logearEvento(Evento.buildEventoContagio(
                                null,
                                vector.nombreDeLocacionActual,
                                it.patogeno.tipo,
                                it.nombre,
                                null,
                                "La especie ${it.nombre} del patogeno ${it.patogeno.tipo} aparecio en ${vectorInfectado.nombreDeLocacionActual}"
                        ))
                    }
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
        if (patogenoService.esPandemia(especie.id)) {
            mongoDao.logearEvento(Evento.buildEventoContagio(
                    null,
                    null,
                    especie.patogeno.tipo,
                    especie.nombre,
                    null,
                    "La especie ${especie.nombre} del patogeno ${especie.patogeno.tipo} es pandemia"
            ))
        }
        if (!existeEspecieEnUbicacion(vector.nombreDeLocacionActual, especie)) {
            mongoDao.logearEvento(Evento.buildEventoContagio(
                    null,
                    vector.nombreDeLocacionActual,
                    especie.patogeno.tipo,
                    especie.nombre,
                    null,
                    "La especie ${especie.nombre} del patogeno ${especie.patogeno.tipo} aparecio en ${vector.nombreDeLocacionActual}"
            ))
        }
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

    fun existeEspecieEnUbicacion(locacion: String, especie: Especie) : Boolean{
        val vectores = getVectoresByLocacion(locacion)
        return vectores.map { it.especies }.flatten().contains(especie)
    }
}