package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Mutacion
import ar.edu.unq.eperdemic.persistencia.dao.MutacionDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernatePatogenoDAO
import ar.edu.unq.eperdemic.services.MutacionService
import ar.edu.unq.eperdemic.services.utils.ObjectStructureUtils
import ar.edu.unq.eperdemic.services.utils.validateEntityExists

class MutacionServiceImpl(val mutacionDAO: MutacionDAO) : MutacionService {
    val patogenoService = PatogenoServiceImpl(HibernatePatogenoDAO())
    override fun mutar(especieId: Int, mutacionId: Int) {
        val especie = patogenoService.recuperarEspecie(especieId)
        especie.mutar(recuperarMutacion(mutacionId))
        patogenoService.actualizarEspecie(especie)
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
            validateEntityExists<Mutacion>(mutacionId)
            mutacionDAO.recuperar(mutacionId)
        }
    }
}