@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.Command

external interface GetAccessKeyInfoCommandInput : STSServiceInput, GetAccessKeyInfoRequest
external interface GetAccessKeyInfoCommandOutput : STSServiceOutput, GetAccessKeyInfoResponse

external class GetAccessKeyInfoCommand(input: GetAccessKeyInfoCommandInput) :
    Command<GetAccessKeyInfoCommandInput, GetAccessKeyInfoCommandOutput> {
    override val input: GetAccessKeyInfoCommandInput
}
