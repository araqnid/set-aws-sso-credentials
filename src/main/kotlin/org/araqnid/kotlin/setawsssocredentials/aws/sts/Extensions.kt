@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import js.core.jso
import kotlinx.coroutines.suspendCancellableCoroutine
import org.araqnid.kotlin.setawsssocredentials.aws.Credentials
import org.araqnid.kotlin.setawsssocredentials.aws.toClientCallback
import org.araqnid.kotlin.setawsssocredentials.aws.toCredentialsProvider
import web.abort.AbortController

typealias ProvideCredentials = (suspend () -> Credentials)

fun createSTSClient(
    region: String? = null,
    defaultsMode: String? = null,
    credentialDefaultProvider: ProvideCredentials? = null
): STSClient {
    return STSClient(jso {
        if (region != null) this.region = region
        if (defaultsMode != null) this.defaultsMode = defaultsMode
        if (credentialDefaultProvider != null) this.credentialDefaultProvider =
            credentialDefaultProvider.toCredentialsProvider()
    })
}

fun createSTS(
    region: String? = null,
    defaultsMode: String? = null,
    credentialDefaultProvider: ProvideCredentials? = null
): STS {
    return STS(jso {
        if (region != null) this.region = region
        if (defaultsMode != null) this.defaultsMode = defaultsMode
        if (credentialDefaultProvider != null) this.credentialDefaultProvider =
            credentialDefaultProvider.toCredentialsProvider()
    })
}

suspend fun STS.getCallerIdentity(input: GetCallerIdentityCommandInput): GetCallerIdentityCommandOutput {
    return suspendCancellableCoroutine { cont ->
        val abortController = AbortController()
        cont.invokeOnCancellation(abortController::abort)
        getCallerIdentityAsync(input, jso { abortSignal = abortController.signal }, cont.toClientCallback())
    }
}

suspend fun STS.getCallerIdentity(block: GetCallerIdentityCommandInput.() -> Unit): GetCallerIdentityCommandOutput {
    return getCallerIdentity(input = jso(block))
}
