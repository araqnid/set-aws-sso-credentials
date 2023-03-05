@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.Command

external interface AssumeRoleWithWebIdentityCommandInput : STSServiceInput, AssumeRoleWithWebIdentityRequest
external interface AssumeRoleWithWebIdentityCommandOutput : STSServiceOutput, AssumeRoleWithWebIdentityResponse

external class AssumeRoleWithWebIdentityCommand(input: AssumeRoleWithWebIdentityCommandInput) :
    Command<AssumeRoleWithWebIdentityCommandInput, AssumeRoleWithWebIdentityCommandOutput> {
    override val input: AssumeRoleWithWebIdentityCommandInput
}
