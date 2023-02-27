package org.araqnid.kotlin.setawsssocredentials.aws.sts

import js.core.jso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.araqnid.kotlin.setawsssocredentials.aws.Credentials
import org.araqnid.kotlin.setawsssocredentials.aws.Provider
import org.araqnid.kotlin.setawsssocredentials.aws.send

@OptIn(DelicateCoroutinesApi::class)
fun createSTSClient(
    region: String? = null,
    defaultsMode: String? = null,
    credentialDefaultProvider: (suspend () -> Credentials)? = null
): STSClient {
    return STSClient(jso {
        if (region != null) this.region = region
        if (defaultsMode != null) this.defaultsMode = defaultsMode
        if (credentialDefaultProvider != null) this.credentialDefaultProvider =
            { GlobalScope.promise { credentialDefaultProvider() } }.unsafeCast<Provider<Credentials>>()
    })
}

suspend inline fun STSClient.getCallerIdentity(): GetCallerIdentityResponse = send(GetCallerIdentityCommand(jso {
}))
