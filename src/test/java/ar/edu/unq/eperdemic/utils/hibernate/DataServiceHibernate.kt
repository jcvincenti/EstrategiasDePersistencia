package ar.edu.unq.eperdemic.utils.hibernate

import ar.edu.unq.eperdemic.dto.VectorFrontendDTO
import ar.edu.unq.eperdemic.modelo.*
import ar.edu.unq.eperdemic.persistencia.dao.hibernate.HibernateDataDAO
import ar.edu.unq.eperdemic.services.runner.TransactionRunner
import ar.edu.unq.eperdemic.utils.DataService

class DataServiceHibernate : DataService {
    val hibernateDao = HibernateDataDAO()

    override fun crearSetDeDatosIniciales() {
        val patogenos = crearPatogenos()
        val ubicaciones = crearUbicaciones()
        val vectores = crearVectores()
        val especies = crearEspecies(patogenos)

        vectores.get(0).especies.addAll(especies)

        createDataSet(patogenos, ubicaciones, vectores)
    }

    private fun crearPatogenos() : List<Patogeno> {
        val virus = Patogeno("Virus")
        val bacteria = Patogeno("Bacteria")
        virus.setCapacidadDeContagio(TipoDeVectorEnum.Persona,100)
        bacteria.setCapacidadDeContagio(TipoDeVectorEnum.Persona,100)
        return listOf(virus, bacteria)
    }

    private fun crearUbicaciones() : List<String> {
        return mutableListOf("Entre Rios", "La Pampa", "Catamarca", "Buenos Aires", "Cordoba", "Bariloche", "Quilmes", "Berazategui", "Lanus")
    }

    private fun crearVectores() : List<Vector> {
        return listOf(
                VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Persona,"Buenos Aires")
                        .aModelo(),
                VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Animal, "Cordoba")
                        .aModelo(),
                VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Insecto,"Bariloche")
                        .aModelo(),
                VectorFrontendDTO(VectorFrontendDTO.TipoDeVector.Persona,"Cordoba")
                        .aModelo()
        )
    }

    private fun crearEspecies(patogenos : List<Patogeno>) : List<Especie> {
        return listOf(patogenos.get(0).crearEspecie("Gripe","China"),
                patogenos.get(0).crearEspecie("H1N1", "Uruguay"),
                patogenos.get(1).crearEspecie("Angina", "Argentina"))
    }

    private fun createDataSet(patogenos: List<Patogeno>, ubicaciones: List<String>, vectores: List<Vector>) {
        TransactionRunner.runTrx {
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