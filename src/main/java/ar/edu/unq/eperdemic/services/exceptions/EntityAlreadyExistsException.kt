package ar.edu.unq.eperdemic.services.exceptions

class EntityAlreadyExistsException(override val message: String?) : Exception(message) {
}