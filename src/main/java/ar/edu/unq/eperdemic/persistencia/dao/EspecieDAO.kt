package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie

interface EspecieDAO {
    fun guardar(especie: Especie)
    fun recuperar(especieId: Int) : Especie
    fun actualizar(especie: Especie)
    fun cantidadUbicacionesDeEspecie(especieId: Int): Long
    fun lideres() : List<Especie>
    fun findEspecieLider() : Especie
    fun cantidadDeInfectados(especieId: Int) : Int
 }