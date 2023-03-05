@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

import js.core.jso

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
