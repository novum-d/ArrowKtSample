package io.novumd.arrowKtSample.typedErrors.either

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import io.novumd.arrowKtSample.model.User
import io.novumd.arrowKtSample.model.UserNotFound

fun User.isValid(): Either<UserNotFound, Unit> = either {
    ensure(id > 0) { UserNotFound("User without a valid id: $id") }
}
