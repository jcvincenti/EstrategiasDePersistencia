package ar.edu.unq.eperdemic.services.exceptions

import java.lang.Exception

class NoSePudoCrearPatogenoException(override val message: String?) : Exception() {
}