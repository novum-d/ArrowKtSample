package io.novumd.arrowKtSample.lenses

import arrow.optics.optics

@optics
data class Db(val cities: Map<String, City>) {
    companion object
}

@optics
data class City(val name: String, val country: String) {
    companion object
}

val db = Db(
    mapOf(
        "Alejandro" to City("Hilversum", "Netherlands"),
        "Ambrosio" to City("Ciudad Real", "Spain")
    )
)
