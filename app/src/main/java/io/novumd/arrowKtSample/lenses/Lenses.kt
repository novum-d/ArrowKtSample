package io.novumd.arrowKtSample.lenses

import arrow.optics.copy
import arrow.optics.optics

@optics
data class Db(val cities: Map<String, City>) {
    companion object
}

@optics
data class City(val name: String, val country: String) {
    companion object
}

@optics
data class InputErrorState private constructor(val domainError: DomainError) {


    companion object {
        fun from (domainError: DomainError)  = InputErrorState(domainError)
    }
}


fun handleError(error: DomainError) {
    copy {
        when (error) {
            DomainError.WrongPasscode -> InputPasscodeState.isWrongPasscodeError set true
            DomainError.Session -> InputPasswor.isWrongPasscodeError set true
            else -> {}
        }
    }
}

sealed interface DomainError {

    interface WithMessage : DomainError {
        val message: String
    }

    data object WrongPasscode : DomainError

    data class Session(override val message: String) : WithMessage
}


val db = Db(
    mapOf(
        "Alejandro" to City("Hilversum", "Netherlands"),
        "Ambrosio" to City("Ciudad Real", "Spain")
    )
)
