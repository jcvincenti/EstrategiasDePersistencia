package ar.edu.unq.eperdemic.services.exceptions

import java.lang.Exception

class NullEntityException(override val message: String?) : Exception()