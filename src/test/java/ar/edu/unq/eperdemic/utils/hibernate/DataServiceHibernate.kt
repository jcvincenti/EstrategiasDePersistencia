package ar.edu.unq.eperdemic.utils.hibernate

import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.modelo.Vector
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateDataDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.utils.DataService

class DataServiceHibernate : DataService {
    val hibernateDao = HibernateDataDAO()

    override fun crearSetDeDatosIniciales() {
        val vectores = mutableListOf("Buenos Aires", "Cordoba", "Bariloche")
        val ubicaciones = mutableListOf("Entre Rios", "La Pampa", "Catamarca")
        TransactionRunner.runTrx {
            vectores.forEach {
                locacionVector -> hibernateDao.create(Vector(locacionVector))
            }
            ubicaciones.forEach{
                ubicacion -> hibernateDao.create(Ubicacion(ubicacion))
            }
        }
    }

    override fun eliminarTodo() {
        TransactionRunner.runTrx {
            hibernateDao.clear()
        }
    }
}