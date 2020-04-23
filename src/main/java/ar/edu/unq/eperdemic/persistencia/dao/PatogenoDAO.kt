package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Patogeno

interface PatogenoDAO {
    fun crear(patogeno: Patogeno): Int
    fun actualizar(patogeno: Patogeno )
    fun recuperar(idDelPatogeno: Int): Patogeno
    fun recuperarATodos() : List<Patogeno>
    fun existePatogenoConTipo(tipo: String) : Boolean
    fun existePatogenoConId(id: Int): Boolean
}