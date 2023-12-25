package io.novumd.arrowKtSample

import arrow.core.raise.fold
import arrow.core.raise.recover
import arrow.core.some
import arrow.core.toOption
import arrow.optics.dsl.index
import arrow.optics.typeclasses.Index
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.novumd.arrowKtSample.lenses.Db
import io.novumd.arrowKtSample.lenses.cities
import io.novumd.arrowKtSample.lenses.country
import io.novumd.arrowKtSample.lenses.db
import io.novumd.arrowKtSample.model.User
import io.novumd.arrowKtSample.model.UserNotFound
import io.novumd.arrowKtSample.typedErrors.raise.isValid

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

// @RunWith(KotestTestRunner::class)
class ExampleUnitTest : FreeSpec({
    "TypedErrors" - {
        "Raise" {
            //  User(-1).isValid() shouldBe UserNotFound("User without a valid id: -1").left()
            fold(
                { isValid(User(1)) },
                { _: UserNotFound -> fail("No logical failure occurred!") },
                { user: User -> user.id shouldBe 1 }
            )

            recover({ User(-1).isValid() }) { }
        }
    }

    "Lenses" {
        Db.cities.index(Index.map(), "Alejandro").country
            .getOrNull(db)
            .toOption() shouldBe "Netherlands".some()

        Db.cities.index(Index.map(), "Jack").country.getOrNull(db).toOption()
    }
})
