@file:JsModule("@aws-sdk/client-sso")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

import org.araqnid.kotlin.setawsssocredentials.aws.Command
import org.araqnid.kotlin.setawsssocredentials.aws.MetadataBearer

external interface GetRoleCredentialsCommandInput : SSOServiceInput, GetRoleCredentialsRequest
external interface GetRoleCredentialsCommandOutput : SSOServiceOutput, GetRoleCredentialsResponse, MetadataBearer

/**
 * Returns the STS short-term credentials for a given role name that is assigned to the
 * user.
 *
 * @see GetRoleCredentialsCommandInput
 * @see GetRoleCredentialsCommandOutput
 * @see SSOClientResolvedConfig
 */
external class GetRoleCredentialsCommand(input: GetRoleCredentialsCommandInput) :
    Command<GetRoleCredentialsCommandInput, GetRoleCredentialsCommandOutput> {
    val input: GetRoleCredentialsCommandInput
}