package com.andrei.wegroszta.randomusers.networking

import com.andrei.wegroszta.randomusers.entities.RandomUsersException
import kotlinx.coroutines.CancellationException

suspend fun <T> runWithNetworkErrorHandling(action: suspend () -> T): T {
    try {
        return action()
    } catch (ex: Exception) {
        if (ex is CancellationException) {
            throw ex
        }

        //todo parse the exception and throw some more complex exception hierarchy
        throw RandomUsersException(ex.message ?: "unknown error", ex)

    }
}
