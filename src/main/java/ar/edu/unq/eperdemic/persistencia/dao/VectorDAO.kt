package ar.edu.unq.eperdemic.persistencia.dao

import ar.edu.unq.eperdemic.modelo.Especie
import ar.edu.unq.eperdemic.modelo.Vector

interface VectorDAO {
    fun guardar(vector: Vector)
    fun recuperar(id: Int) : Vector
    fun borrar(vector: Vector)
    fun enfermedades(vectorId: Int) : List<Especie>
    fun actualizar(vector: Vector)
}