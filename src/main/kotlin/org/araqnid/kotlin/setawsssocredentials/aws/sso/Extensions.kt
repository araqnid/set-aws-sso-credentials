@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

import js.objects.jso
import kotlinx.coroutines.suspendCancellableCoroutine
import org.araqnid.kotlin.setawsssocredentials.aws.toClientCallback
import web.abort.AbortController

fun createSSOClient(region: String? = null, defaultsMode: String? = null): SSOClient {
    return SSOClient(jso {
        if (region != null) this.region = region
        if (defaultsMode != null) this.defaultsMode = defaultsMode
    })
}

fun createSSO(region: String? = null, defaultsMode: String? = null): SSO {
    return SSO(jso {
        if (region != null) this.region = region
        if (defaultsMode != null) this.defaultsMode = defaultsMode
    })
}

suspend fun SSO.getRoleCredentials(input: GetRoleCredentialsCommandInput): GetRoleCredentialsCommandOutput {
    return suspendCancellableCoroutine { cont ->
        val abortController = AbortController()
        cont.invokeOnCancellation { abortController.abort() }
        getRoleCredentialsAsync(input, jso { abortSignal = abortController.signal }, cont.toClientCallback())
    }
}

suspend inline fun SSO.getRoleCredentials(block: GetRoleCredentialsCommandInput.() -> Unit): GetRoleCredentialsCommandOutput {
    return getRoleCredentials(input = jso(block))
}
