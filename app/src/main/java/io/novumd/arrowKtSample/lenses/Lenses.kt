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
data class InputErrorState(val sessionErrMsg: String? = null) {

    fun handleError(error: DomainErr) {
        copy {
            when (error) {
                DomainErr.WrongPasscode -> {}
                is DomainErr.Session -> InputErrorState.sessionErrMsg set error.errMsg
                else -> {}
            }
        }
    }

    companion object
}


sealed interface DomainErr {

    interface WithMessage : DomainErr {
        val errMsg: String
    }

    data object WrongPasscode : DomainErr

    data class Session(override val errMsg: String) : WithMessage
}


val db = Db(
    mapOf(
        "Alejandro" to City("Hilversum", "Netherlands"),
        "Ambrosio" to City("Ciudad Real", "Spain")
    )
)
