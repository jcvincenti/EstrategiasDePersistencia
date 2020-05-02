package ar.edu.unq.eperdemic.modelo

abstract class ContagioStrategy {
    abstract fun puedeSerInfectadoPor(tipo: TipoDeVectorEnum) : Boolean
}

class Animal : ContagioStrategy() {
    override fun puedeSerInfectadoPor(tipo: TipoDeVectorEnum) : Boolean {
        return listOf(TipoDeVectorEnum.Insecto).contains(tipo)
    }
}

class Persona  : ContagioStrategy() {
    override fun puedeSerInfectadoPor(tipo: TipoDeVectorEnum) : Boolean {
        return listOf(TipoDeVectorEnum.Persona,
                TipoDeVectorEnum.Insecto,
                TipoDeVectorEnum.Animal).contains(tipo)
    }
}

class Insecto  : ContagioStrategy() {
    override fun puedeSerInfectadoPor(tipo: TipoDeVectorEnum) : Boolean {
        return listOf(TipoDeVectorEnum.Persona,
                TipoDeVectorEnum.Animal).contains(tipo)
    }
}