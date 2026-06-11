@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.js.Promise

typealias Provider<T> = () -> Promise<T>

typealias AwsCredentialIdentityProvider = Provider<AwsCredentialIdentity>

typealias ProvideCredentials = (suspend () -> AwsCredentialIdentity)

fun ProvideCredentials.toCredentialsProvider(): Provider<AwsCredentialIdentity> {
    return {
        Promise { resolve, reject ->
            val block: suspend () -> AwsCredentialIdentity = this@toCredentialsProvider
            block.startCoroutine(Continuation(EmptyCoroutineContext) { result ->
                result.fold(
                    { resolve(it) },
                    { reject(it) }
                )
            })
        }
    }
}
