@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.Command

external interface DecodeAuthorizationMessageCommandInput : STSServiceInput, DecodeAuthorizationMessageRequest
external interface DecodeAuthorizationMessageCommandOutput : STSServiceOutput, DecodeAuthorizationMessageResponse

external class DecodeAuthorizationMessageCommand(input: DecodeAuthorizationMessageCommandInput) :
    Command<DecodeAuthorizationMessageCommandInput, DecodeAuthorizationMessageCommandOutput> {
    override val input: DecodeAuthorizationMessageCommandInput
}
