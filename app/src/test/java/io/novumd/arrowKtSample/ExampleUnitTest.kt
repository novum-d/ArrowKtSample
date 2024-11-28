package io.novumd.arrowKtSample

import arrow.core.NonEmptyList
import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.core.raise.fold
import arrow.core.raise.recover
import arrow.core.raise.zipOrAccumulate
import arrow.core.some
import arrow.core.toOption
import arrow.optics.copy
import arrow.optics.dsl.index
import arrow.optics.optics
import arrow.optics.typeclasses.Index
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.novumd.arrowKtSample.lenses.Address
import io.novumd.arrowKtSample.lenses.Db
import io.novumd.arrowKtSample.lenses.Person
import io.novumd.arrowKtSample.lenses.cities
import io.novumd.arrowKtSample.lenses.country
import io.novumd.arrowKtSample.lenses.db
import io.novumd.arrowKtSample.lenses.set
import io.novumd.arrowKtSample.model.User
import io.novumd.arrowKtSample.model.UserNotFound
import io.novumd.arrowKtSample.typedErrors.raise.isValid

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


class ExampleUnitTest : FreeSpec({
    "TypedErrors" - {
        "Raise" {
            //  User(-1).isValid() shouldBe UserNotFound("User without a valid id: -1").left()
            fold(
                { isValid(User(1)) },
                { _: UserNotFound -> fail("No logical failure occurred!") },
                { user: User -> user.id shouldBe 1 }
            )

            recover({ User(-1).isValid() }) {
                it.message shouldBe "User without a valid id: -1"
            }

            recover({ UserUpdateCommand("", "3333", "hoge@gmail.com", "").validate() }) {
                it.all.size shouldBe 2
            }
        }
    }

    "Lenses" - {
        "Cities DB" {
            Db.cities.index(Index.map(), "Alejandro").country
                .getOrNull(db)
                .toOption() shouldBe "Netherlands".some()

            Db.cities.index(Index.map(), "Jack").country.getOrNull(db).toOption()
        }

        "Person" {
            val expected = "Osaka"
            val person = Person("novumd", Address("Shibuya", "hogehoge-1-14-514"))
            person.set(person.copy(expected).address)
        }
    }
})


@optics
data class UserUpdateCommand(
    val id: String,
    val name: String?,
    val email: String?,
    val password: String,
) {

    operator fun invoke(
        name: String,
        email: String,
    ) {
        copy {
            UserUpdateCommand.name set name
            UserUpdateCommand.email set email
        }
    }

    companion object
}

context(Raise<NonEmptyList<Err.Domain.UserInvalidError>>)
fun UserUpdateCommand.validate() {
    zipOrAccumulate(
        { id.let(::UserId).validate() },
        { name?.let(::UserName)?.validate() },
        { email?.let(::UserEmail)?.validate() },
        { password.let(::UserPassword).validate() },
    ) { _, _, _, _ -> }
}

/** Error Type */
sealed interface Err {

    /** Domain Layer */
    sealed interface Domain : Err {
        sealed interface UserInvalidError : Domain {
            data object UserIdInvalid : UserInvalidError
            data object UserNameInvalid : UserInvalidError
            data object UserPasswordInvalid : UserInvalidError
            data object UserEmailInvalid : UserInvalidError
        }
    }
}

@JvmInline
value class UserId(val value: String) {

    context(Raise<Err.Domain.UserInvalidError.UserIdInvalid>)
    fun validate() {
        val pattern = """[a-z]{4}"""
        ensure("""^$pattern-$pattern$""".toRegex().matches(value)) {
            Err.Domain.UserInvalidError.UserIdInvalid
        }
    }
}

@JvmInline
value class UserName(val value: String) {

    context(Raise<Err.Domain.UserInvalidError.UserNameInvalid>)
    fun validate() {
        val maxLength = 3
        ensure(value.length > maxLength) {
            Err.Domain.UserInvalidError.UserNameInvalid
        }
    }
}

@JvmInline
value class UserEmail(val value: String) {

    context(Raise<Err.Domain.UserInvalidError.UserEmailInvalid>)
    fun validate() {
        val pattern = """^[a-zA-Z0-9_.+-]+@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\.)+[a-zA-Z]{2,}$"""
        ensure("""^$pattern$""".toRegex().matches(value)) {
            Err.Domain.UserInvalidError.UserEmailInvalid
        }
    }
}

@JvmInline
value class UserPassword(val value: String) {

    context(Raise<Err.Domain.UserInvalidError.UserPasswordInvalid>)
    fun validate() {
        val minLength = 22
        val pattern = """[\d]{4}"""
        ensure(value.length > minLength && """^$pattern$""".toRegex().matches(value)) {
            Err.Domain.UserInvalidError.UserPasswordInvalid
        }
    }
}
