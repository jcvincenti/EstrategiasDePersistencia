package ar.edu.unq.eperdemic.persistencia.dao.hibernate

import ar.edu.unq.eperdemic.modelo.Patogeno
import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO

open class HibernatePatogenoDAO: HibernateDAO<Patogeno>(Patogeno::class.java), PatogenoDAO {

    //TODO: Ver despues si cambiamos la interface. Esto quedo del tp de JDBC
    override fun crear(patogeno: Patogeno): Int {
        TODO("Not yet implemented")
    }

    override fun recuperarATodos(): List<Patogeno> {
        TODO("Not yet implemented")
    }

    override fun existePatogenoConTipo(tipo: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun existePatogenoConId(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}