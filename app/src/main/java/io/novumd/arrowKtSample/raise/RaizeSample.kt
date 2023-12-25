package io.novumd.arrowKtSample.raise

import arrow.core.raise.Raise
import arrow.core.raise.ensure
import io.novumd.arrowKtSample.User
import io.novumd.arrowKtSample.UserNotFound

context(Raise<UserNotFound>)
fun User.isValid() = ensure(id > 0) { UserNotFound("User without a valid id: $id") }

fun Raise<UserNotFound>.isValid(user: User): User {
    ensure(user.id > 0) { UserNotFound("User without a valid id: ${user.id}") }
    fold
    return user
}
