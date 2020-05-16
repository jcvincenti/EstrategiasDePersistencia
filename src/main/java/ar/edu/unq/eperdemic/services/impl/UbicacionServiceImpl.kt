package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.services.utils.ObjectStructureUtils
import ar.edu.unq.eperdemic.services.utils.validateEntityDoesNotExists
import ar.edu.unq.eperdemic.services.utils.validateEntityExists

class UbicacionServiceImpl(val ubicacionDAO: UbicacionDAO) : UbicacionService {

    val vectorService = VectorServiceImpl(HibernateVectorDAO())

    override fun mover(vectorId: Int, nombreUbicacion: String) {
        val vector = vectorService.recuperarVector(vectorId)
        val ubicacion = recuperarUbicacion(nombreUbicacion)

        if (vector.puedeMoverse(ubicacion)) {
            vector.moverse(ubicacion!!.nombreUbicacion)
            TransactionRunner.runTrx {
                contagiarZona(vector, nombreUbicacion)
                vectorService.actualizarVector(vector)
            }
        }
    }

    private fun contagiarZona(vector: Vector?, locacion: String?) {
        if (vector != null && vector.estaInfectado()) {
            val vectores = vectorService.getVectoresByLocacion(locacion).toMutableList()
            vectores.removeIf { v -> v.id == vector.id }
            vectorService.contagiar(vector, vectores)
        }
    }

    @ExperimentalStdlibApi
    override fun expandir(nombreUbicacion: String) {
        val vectoresEnUbicacion = vectorService.getVectoresByLocacion(nombreUbicacion)
        val vectorInfectado = vectoresEnUbicacion.filter {vector -> vector.estaInfectado()}.randomOrNull()
        contagiarZona(vectorInfectado, nombreUbicacion)
    }


    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        val ubicacion = Ubicacion(nombreUbicacion)
        ObjectStructureUtils.checkEmptyAttributes(ubicacion)
        TransactionRunner.runTrx {
            validateEntityDoesNotExists<Ubicacion>(nombreUbicacion)
            ubicacionDAO.guardar(ubicacion)
        }
        return ubicacion
    }

    override fun recuperarUbicacion(nombreUbicacion: String): Ubicacion? {
        return TransactionRunner.runTrx {
            validateEntityExists<Ubicacion>(nombreUbicacion)
            ubicacionDAO.recuperar(nombreUbicacion)
        }
    }

    fun cantidadUbicaciones(): Long {
        return TransactionRunner.runTrx {
            ubicacionDAO.cantidadUbicaciones()
        }
    }
}
