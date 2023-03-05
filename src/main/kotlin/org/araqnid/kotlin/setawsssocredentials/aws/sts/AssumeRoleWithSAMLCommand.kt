@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.Command

external interface AssumeRoleWithSAMLCommandInput : STSServiceInput, AssumeRoleWithSAMLRequest
external interface AssumeRoleWithSAMLCommandOutput : STSServiceOutput, AssumeRoleWithSAMLResponse

external class AssumeRoleWithSAMLCommand(input: AssumeRoleWithSAMLCommandInput) :
    Command<AssumeRoleWithSAMLCommandInput, AssumeRoleWithSAMLCommandOutput> {
    override val input: AssumeRoleWithSAMLCommandInput
}
