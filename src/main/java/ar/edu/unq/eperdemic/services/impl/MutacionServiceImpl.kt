package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.persistencia.dao.MutacionDAO
import ar.edu.unq.eperdemic.services.MutacionService
import ar.edu.unq.eperdemic.services.exceptions.EntityNotFoundException
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.services.utils.ObjectStructureUtils

class MutacionServiceImpl(val mutacionDAO: MutacionDAO) : MutacionService {


    override fun mutar(especieId: Int, mutacionId: Int) {
        TODO("Not yet implemented")
    }

    override fun crearMutacion(mutacion: Mutacion): Mutacion {
        ObjectStructureUtils.checkEmptyAttributes(mutacion)
        TransactionRunner.runTrx {
            mutacionDAO.guardar(mutacion)
        }
        return mutacion
    }

    override fun recuperarMutacion(mutacionId: Int): Mutacion {
        return TransactionRunner.runTrx {
            mutacionDAO.recuperar(mutacionId)
        } ?: throw EntityNotFoundException("La entidad Mutacion con id ${mutacionId} no existe")
    }
}