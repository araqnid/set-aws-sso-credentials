@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.js.Promise

typealias Provider<T> = () -> Promise<T>

typealias ProvideCredentials = (suspend () -> Credentials)

fun ProvideCredentials.toCredentialsProvider(): Provider<Credentials> {
    return {
        Promise { resolve, reject ->
            val block: suspend () -> Credentials = this@toCredentialsProvider
            block.startCoroutine(Continuation(EmptyCoroutineContext) { result ->
                result.fold(
                    { resolve(it) },
                    { reject(it) }
                )
            })
        }
    }
}
