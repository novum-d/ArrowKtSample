package io.novumd.arrowKtSample.typedErrors.raise

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import io.novumd.arrowKtSample.model.User
import io.novumd.arrowKtSample.model.UserNotFound

context(Raise<UserNotFound>)
fun User.isValid() = ensure(id > 0) { UserNotFound("User without a valid id: $id") }

fun Raise<UserNotFound>.isValid(user: User): User {
    ensure(user.id > 0) { UserNotFound("User without a valid id: ${user.id}") }
    return user
}
