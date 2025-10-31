@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.Command

external interface GetFederationTokenCommandInput : STSServiceInput, GetFederationTokenRequest
external interface GetFederationTokenCommandOutput : STSServiceOutput, GetFederationTokenResponse

external class GetFederationTokenCommand(input: GetFederationTokenCommandInput) :
    Command<GetFederationTokenCommandInput, GetFederationTokenCommandOutput> {
    override val input: GetFederationTokenCommandInput
}
