package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class VectorServiceImpl(val vectorDAO: VectorDAO) : VectorService {
    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun infectar(vector: Vector, especie: Especie) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enfermedades(vectorId: Int): List<Especie> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun crearVector(vector: Vector): Vector {
        TransactionRunner.runTrx {
            vectorDAO.guardar(vector)
        }
        return vector
    }

    override fun recuperarVector(vectorId: Int): Vector {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun borrarVector(vectorId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}