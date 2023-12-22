package io.novumd.arrowKtSample

import arrow.core.raise.fold
import arrow.core.raise.recover
import io.kotest.assertions.fail
import io.kotest.matchers.shouldBe
import io.novumd.arrowKtSample.either.User
import io.novumd.arrowKtSample.either.UserNotFound
import io.novumd.arrowKtSample.either.isValid
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
}