@file:JsModule("@aws-sdk/token-providers")

package org.araqnid.kotlin.setawsssocredentials.aws

external interface FromSsoInit {
    var profile: String?
}

external interface TokenIdentity : Identity {
    val token: String
}

external fun fromSso(init: FromSsoInit = definedExternally): Provider<TokenIdentity>
