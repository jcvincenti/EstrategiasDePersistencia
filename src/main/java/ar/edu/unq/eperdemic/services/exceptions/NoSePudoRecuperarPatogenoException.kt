package ar.edu.unq.eperdemic.services.exceptions

import java.lang.Exception

class NoSePudoRecuperarPatogenoException(override val message: String?) : Exception()