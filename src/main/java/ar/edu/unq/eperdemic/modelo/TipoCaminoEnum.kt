package ar.edu.unq.eperdemic.modelo

enum class TipoCaminoEnum {
    Terrestre, Maritimo, Aereo;

    companion object TipoDeCaminoBuilder {
        fun parseTipo(tipo: String): TipoCaminoEnum? {
            return when(tipo) {
                "terrestre" -> Terrestre
                "maritimo" -> Maritimo
                "aereo" -> Aereo
                else -> null
            }
        }
    }

    fun puedeSerAtravesadoPor(tipoVector: TipoDeVectorEnum) : Boolean {
        return when(this) {
            Terrestre -> listOf(TipoDeVectorEnum.Persona, TipoDeVectorEnum.Insecto, TipoDeVectorEnum.Animal).contains(tipoVector)
            Maritimo -> listOf(TipoDeVectorEnum.Persona, TipoDeVectorEnum.Animal).contains(tipoVector)
            Aereo -> listOf(TipoDeVectorEnum.Insecto, TipoDeVectorEnum.Animal).contains(tipoVector)
        }
    }
}

