package org.araqnid.kotlin.setawsssocredentials.aws

import js.core.jso
import kotlin.js.Date

fun fixedCredentials(accessKeyId: String, secretAccessKey: String, sessionToken: String? = null, expiration: Date? = null): Credentials {
    return jso<MutableCredentials> {
       this.accessKeyId = accessKeyId
       this.secretAccessKey = secretAccessKey
       this.sessionToken = sessionToken
       this.expiration = expiration
    }
}

external interface MutableCredentials : Credentials {
    override var accessKeyId: String
    override var secretAccessKey: String
    override var sessionToken: String?
    override var expiration: Date?
}
