package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateEspecieDAO
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.services.EstadisticasService
import ar.edu.unq.eperdemic.services.runner.TransactionRunner

class EstadisticaServiceImpl() : EstadisticasService {

    val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())
    val especieHibernateDao = HibernateEspecieDAO()

    override fun especieLider(): Especie {
        TODO("Not yet implemented")
    }

    override fun lideres(): List<Especie> {
        return TransactionRunner.runTrx { especieHibernateDao.lideres() }
    }

    override fun reporteDeContagios(nombreUbicacion: String): ReporteDeContagios {
        val ubicacion = ubicacionService.recuperarUbicacion(nombreUbicacion)

        return ubicacion!!.generarReporte()
    }
}