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
        val ubicacion = recuperarUbicacion(nombreUbicacion)

        if (ubicacion == null){
            //TODO: exception personalizada
            throw RuntimeException("NO ESITE")
        }
        vector.nombreDeLocacionActual = ubicacion
        TransactionRunner.runTrx {
            if (vector.estaInfectado()){
                this.contagiarZona(vector, nombreUbicacion)
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

    override fun recuperarUbicacion(nombreUbicacion: String): Ubicacion? {
        return TransactionRunner.runTrx {
            ubicacionDAO.recuperar(nombreUbicacion)
        }
    }
}