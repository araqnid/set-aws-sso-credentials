@file:JsModule("@aws-sdk/types")

package org.araqnid.kotlin.setawsssocredentials.aws

import kotlin.js.Date

external interface Credentials : AwsCredentialIdentity

external interface AwsCredentialIdentity : Identity {
    val accessKeyId: String
    val secretAccessKey: String
    val sessionToken: String?
}

external interface Identity {
    val expiration: Date?
}
