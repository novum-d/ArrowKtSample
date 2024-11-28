package io.novumd.arrowKtSample.lenses

data class Address(val city: String, val street: String) {
    companion object
}

data class Person(val name: String, val address: Address) {
    companion object
}


// S - the source of a Lens
// A - the focus of a Lens
data class Lens<S, A>(
    val get: (S) -> A,
    val set: (S, A) -> S,
)

inline val Person.Companion.address: Lens<Person, Address>
    inline get() = Lens(
        get = { person: Person -> person.`address` },
        set = { person: Person, value: Address -> person.copy(address = value) },
    )

