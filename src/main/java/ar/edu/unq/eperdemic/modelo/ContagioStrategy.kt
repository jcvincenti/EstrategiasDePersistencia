package ar.edu.unq.eperdemic.modelo

abstract class ContagioStrategy {
    abstract fun puedeSerInfectadoPor(vector: Vector) : Boolean
}

class Animal : ContagioStrategy() {
    override fun puedeSerInfectadoPor(vector: Vector) : Boolean {
        return listOf("Insecto").contains(vector.tipo!!)
    }
}

class Persona  : ContagioStrategy() {
    override fun puedeSerInfectadoPor(vector: Vector) : Boolean {
        return listOf("Humano", "Insecto", "Animal").contains(vector.tipo!!)
    }
}

class Insecto  : ContagioStrategy() {
    override fun puedeSerInfectadoPor(vector: Vector) : Boolean {
        return listOf("Humano", "Animal").contains(vector.tipo!!)
    }
}