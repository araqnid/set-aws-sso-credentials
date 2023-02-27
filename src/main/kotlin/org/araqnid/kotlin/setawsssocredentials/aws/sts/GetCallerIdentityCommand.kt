@file:JsModule("@aws-sdk/client-sts")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.Command

external interface GetCallerIdentityCommandInput : STSServiceInput, GetCallerIdentityRequest
external interface GetCallerIdentityCommandOutput : STSServiceOutput, GetCallerIdentityResponse

external class GetCallerIdentityCommand(input: GetCallerIdentityCommandInput) :
    Command<GetCallerIdentityCommandInput, GetCallerIdentityCommandOutput> {
    override val input: GetCallerIdentityCommandInput
}
