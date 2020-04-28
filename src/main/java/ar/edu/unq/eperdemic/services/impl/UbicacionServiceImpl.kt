package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.UbicacionDAO
import ar.edu.unq.eperdemic.services.UbicacionService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class UbicacionServiceImpl(val ubicacionDAO: UbicacionDAO) : UbicacionService {

    override fun mover(vectorId: Int, nombreUbicacion: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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