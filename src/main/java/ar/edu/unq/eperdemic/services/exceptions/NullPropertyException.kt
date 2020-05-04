package ar.edu.unq.eperdemic.services.exceptions

import java.lang.Exception

class NullPropertyException(override val message: String?) : Exception() {
}