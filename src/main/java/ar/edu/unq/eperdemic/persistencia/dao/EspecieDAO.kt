package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie

interface EspecieDAO {
    fun recuperar(especieId: Int) : Especie
    fun actualizar(especie: Especie)
}