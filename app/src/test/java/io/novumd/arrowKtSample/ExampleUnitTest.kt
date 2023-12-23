package io.novumd.arrowKtSample

import arrow.core.raise.fold
import arrow.core.raise.recover
import arrow.core.toOption
import arrow.optics.dsl.index
import arrow.optics.typeclasses.Index
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.assertions.fail
import io.kotest.matchers.shouldBe
import io.novumd.arrowKtSample.either.User
import io.novumd.arrowKtSample.either.UserNotFound
import io.novumd.arrowKtSample.either.isValid
import io.novumd.arrowKtSample.lenses.Db
import io.novumd.arrowKtSample.lenses.cities
import io.novumd.arrowKtSample.lenses.country
import io.novumd.arrowKtSample.lenses.db
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        //  User(-1).isValid() shouldBe UserNotFound("User without a valid id: -1").left()

        fold(
            { isValid(User(1)) },
            { _: UserNotFound -> fail("No logical failure occurred!") },
            { user: User -> user.id shouldBe 1 }
        )

        recover({ User(-1).isValid() }) { }
    }

    @Test
    fun hoge() {
        Db.cities.index(Index.map(), "Alejandro").country
            .getOrNull(db)
            .toOption() shouldBeSome "Alejandro"

        Db.cities.index(Index.map(), "Jack").country.getOrNull(db).toOption()
    }
}
