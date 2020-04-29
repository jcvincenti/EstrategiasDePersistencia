package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateVectorDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class UbicacionServiceImpl(val ubicacionDAO: UbicacionDAO) : UbicacionService {

    val vectorService = VectorServiceImpl(HibernateVectorDAO())

    override fun mover(vectorId: Int, nombreUbicacion: String) {
        val vector = vectorService.recuperarVector(vectorId)
        vector.nombreDeLocacionActual = nombreUbicacion
        TransactionRunner.runTrx {
            if (vector.estaInfectado()){
                this.contagiarZona(vector, vector.nombreDeLocacionActual)
            }
            vectorService.actualizarVector(vector)
        }
    }

    private fun contagiarZona(vectorInfectado: Vector, locacion: String?) {
        val vectores = vectorService.getVectoresByLocacion(locacion)
        vectorService.contagiar(vectorInfectado, vectores)
    }

    override fun expandir(nombreUbicacion: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun crearUbicacion(nombreUbicacion: String): Ubicacion {
        val ubicacion = Ubicacion(nombreUbicacion)
        TransactionRunner.runTrx {
            ubicacionDAO.guardar(ubicacion)
        }
        return ubicacion
    }
}