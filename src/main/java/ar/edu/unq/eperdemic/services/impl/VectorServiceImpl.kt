package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class VectorServiceImpl(val vectorDAO: VectorDAO) : VectorService {
    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        vectores.forEach {
            vector -> if (vector.puedeSerInfectadoPor(vectorInfectado)) {
                vectorInfectado.especies.forEach {
                    especie -> infectar(vector, especie)
                }
            }
        }
        TransactionRunner.runTrx {
            vectores.forEach{ vector -> vectorDAO.guardar(vector) }
        }
    }

    override fun infectar(vector: Vector, especie: Especie) {
        if (vector.esContagioExitoso(especie.getCapacidadDeContagio(vector.tipo!!)!!))
            vector.infectar(especie)
            TransactionRunner.runTrx {
                vectorDAO.guardar(vector)
            }
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
        return TransactionRunner.runTrx {
            vectorDAO.recuperar(vectorId)
        }
    }

    override fun borrarVector(vectorId: Int) {
        TransactionRunner.runTrx {
            val vector = Vector()
            vector.id = vectorId
            vectorDAO.borrar(vector)
        }
    }
}