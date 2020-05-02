package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.VectorDAO
import ar.edu.unq.eperdemic.services.VectorService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import kotlin.random.Random

open class VectorServiceImpl(val vectorDAO: VectorDAO) : VectorService {

    override fun contagiar(vectorInfectado: Vector, vectores: List<Vector>) {
        TransactionRunner.runTrx {
            vectores.forEach {
                vector -> if (puedeSerInfectadoPor(vector, vectorInfectado)) {
                    vectorInfectado.especies.forEach {
                        especie -> infectar(vector, especie)
                        }
                }
            }
        }
    }

     override fun infectar(vector: Vector, especie: Especie) {
        if (esContagioExitoso(especie.getCapacidadDeContagio(vector.tipo!!)!!))
            vector.infectar(especie)
            TransactionRunner.runTrx {
                vectorDAO.actualizar(vector)
            }
    }

    override fun enfermedades(vectorId: Int): List<Especie> {
        return TransactionRunner.runTrx {
            vectorDAO.enfermedades(vectorId)
        }
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

    fun actualizarVector(vector: Vector){
        TransactionRunner.runTrx {
            vectorDAO.actualizar(vector)
        }
    }

    open fun esContagioExitoso(factorDeContagio: Int) : Boolean {
        val factorDeContagioExitoso = factorDeContagio.plus(Random.nextInt(1, 10))
        return if (factorDeContagioExitoso > 50) {
            Random.nextInt(factorDeContagioExitoso-50, 100) < factorDeContagioExitoso
        } else {
            Random.nextInt(1, 100) < factorDeContagioExitoso
        }
    }

    private fun puedeSerInfectadoPor(vector: Vector, vectorInfectado: Vector) : Boolean {
        return when(vector.tipo) {
            TipoDeVectorEnum.Persona -> Persona().puedeSerInfectadoPor(vectorInfectado)
            TipoDeVectorEnum.Animal -> Animal().puedeSerInfectadoPor(vectorInfectado)
            TipoDeVectorEnum.Insecto -> Insecto().puedeSerInfectadoPor(vectorInfectado)
            else -> false
        }
    }

    fun getVectoresByLocacion(locacion: String?): List<Vector> {
        return TransactionRunner.runTrx {
            vectorDAO.getVectoresByLocacion(locacion)
        }
    }
}