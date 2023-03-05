@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import js.core.jso
import org.araqnid.kotlin.setawsssocredentials.aws.Credentials
import org.araqnid.kotlin.setawsssocredentials.aws.toCredentialsProvider

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
