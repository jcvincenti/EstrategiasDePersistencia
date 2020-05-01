package ar.edu.unq.eperdemic.modelo

abstract class ContagioStrategy {
    abstract fun puedeSerInfectadoPor(vector: Vector) : Boolean
}

class Animal : ContagioStrategy() {
    override fun puedeSerInfectadoPor(vector: Vector) : Boolean {
        return listOf(TipoDeVectorEnum.Insecto).contains(vector.tipo!!)
    }
}

class Persona  : ContagioStrategy() {
    override fun puedeSerInfectadoPor(vector: Vector) : Boolean {
        return listOf(TipoDeVectorEnum.Persona,
                TipoDeVectorEnum.Insecto,
                TipoDeVectorEnum.Animal).contains(vector.tipo!!)
    }
}

class Insecto  : ContagioStrategy() {
    override fun puedeSerInfectadoPor(vector: Vector) : Boolean {
        return listOf(TipoDeVectorEnum.Persona,
                TipoDeVectorEnum.Animal).contains(vector.tipo!!)
    }
}