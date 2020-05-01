package ar.edu.unq.eperdemic.utils.hibernate

import ar.edu.unq.eperdemic.dto.VectorFrontendDTO
import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.modelo.TipoDeVectorEnum
import ar.edu.unq.eperdemic.modelo.Ubicacion
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateDataDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.utils.DataService

class DataServiceHibernate : DataService {
    val hibernateDao = HibernateDataDAO()

    override fun crearSetDeDatosIniciales() {
        val virus = Patogeno("Virus")
        val bacteria = Patogeno("Bacteria")
        val patogenos = listOf(virus, bacteria)
        patogenos.get(0).setCapacidadDeContagio(TipoDeVectorEnum.Persona,100)
        patogenos.get(1).setCapacidadDeContagio(TipoDeVectorEnum.Persona,100)
        val ubicaciones = mutableListOf("Entre Rios", "La Pampa", "Catamarca", "Buenos Aires", "Cordoba", "Bariloche", "Quilmes", "Berazategui", "Lanus")
        val vectores = listOf(
                VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Persona,"Buenos Aires")
                        .aModelo(),
                VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Animal, "Cordoba")
                        .aModelo(),
                VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Insecto,"Bariloche")
                        .aModelo(),
                VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Persona,"Cordoba")
                        .aModelo()
        )

        TransactionRunner.runTrx {
            vectores.get(0).especies.addAll(
                    listOf(virus.crearEspecie("Gripe","China"),
                            virus.crearEspecie("H1N1", "Uruguay"),
                            bacteria.crearEspecie("Angina", "Argentina"))
            )

            patogenos.forEach {
                patogeno -> hibernateDao.create(patogeno)
            }

            ubicaciones.forEach{
                ubicacion -> hibernateDao.create(Ubicacion(ubicacion))
            }

            vectores.forEach {
                vector -> hibernateDao.create(vector)
            }

        }
    }

    override fun eliminarTodo() {
        TransactionRunner.runTrx {
            hibernateDao.clear()
        }
    }
}