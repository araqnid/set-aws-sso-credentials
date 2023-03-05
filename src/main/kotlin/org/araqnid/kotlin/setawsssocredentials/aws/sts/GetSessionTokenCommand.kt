@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.Command

external interface GetSessionTokenCommandInput : STSServiceInput, GetSessionTokenRequest
external interface GetSessionTokenCommandOutput : STSServiceOutput, GetSessionTokenResponse

external class GetSessionTokenCommand(input: GetSessionTokenCommandInput) :
    Command<GetSessionTokenCommandInput, GetSessionTokenCommandOutput> {
    override val input: GetSessionTokenCommandInput
}
