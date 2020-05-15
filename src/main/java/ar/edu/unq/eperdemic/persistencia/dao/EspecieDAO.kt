package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Ubicacion

interface EspecieDAO {
    fun recuperar(especieId: Int) : Especie
    fun actualizar(especie: Especie)
    fun getLugaresPresenteEspecie(especieId: Int): List<Ubicacion>
}