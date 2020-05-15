package ar.edu.unq.eperdemic.services.impl

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.ReporteDeContagios
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateUbicacionDAO
import ar.edu.unq.eperdemic.services.EstadisticasService

class EstadisticaServiceImpl() : EstadisticasService {

    val ubicacionService = UbicacionServiceImpl(HibernateUbicacionDAO())

    override fun especieLider(): Especie {
        TODO("Not yet implemented")
    }

    override fun lideres(): List<Especie> {
        TODO("Not yet implemented")
    }

    override fun reporteDeContagios(nombreUbicacion: String): ReporteDeContagios {
        val ubicacion = ubicacionService.recuperarUbicacion(nombreUbicacion)
        val totales = ubicacion!!.vectoresTotales()
        val infectados = ubicacion.vectoresInfectados()
        val especie = ubicacion.especieConMasVectoresInfectados()

        return ReporteDeContagios(totales, infectados, especie)
    }
}