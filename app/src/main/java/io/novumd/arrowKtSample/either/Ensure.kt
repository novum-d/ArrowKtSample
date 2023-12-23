package io.novumd.arrowKtSample.either

import arrow.core.raise.Raise
import arrow.core.raise.ensure

data class User(val id: Int)

data class UserNotFound(val message: String)

// fun User.isValid(): Either<UserNotFound, Unit> = either {
//     ensure(id > 0) { UserNotFound("User without a valid id: $id") }
// }

context(Raise<UserNotFound>)
fun User.isValid() = ensure(id > 0) { UserNotFound("User without a valid id: $id") }

fun Raise<UserNotFound>.isValid(user: User): User {
    ensure(user.id > 0) { UserNotFound("User without a valid id: ${user.id}") }
    return user
}
