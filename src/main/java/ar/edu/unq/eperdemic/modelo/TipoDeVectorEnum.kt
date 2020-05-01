package ar.edu.unq.eperdemic.modelo

enum class TipoDeVectorEnum() {
    Persona, Animal, Insecto;

    companion object TipoDeVectorBuilder {
        fun parseTipo(tipo: String): TipoDeVectorEnum? {
            return when(tipo) {
                "Persona" -> Persona
                "Animal" -> Animal
                "Insecto" -> Insecto
                else -> null
            }
        }
    }
}

