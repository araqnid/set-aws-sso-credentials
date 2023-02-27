@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

external interface GetCallerIdentityRequest {

}

external interface GetCallerIdentityResponse {
    val UserId: String?
    val Account: String?
    val Arn: String?
}
