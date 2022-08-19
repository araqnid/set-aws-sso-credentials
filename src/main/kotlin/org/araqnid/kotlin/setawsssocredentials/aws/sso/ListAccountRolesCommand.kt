@file:JsModule("@aws-sdk/client-sso")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

import org.araqnid.kotlin.setawsssocredentials.aws.Command
import org.araqnid.kotlin.setawsssocredentials.aws.MetadataBearer

external interface ListAccountRolesCommandInput : SSOServiceInput, ListAccountRolesRequest
external interface ListAccountRolesCommandOutput : SSOServiceOutput, ListAccountRolesResponse, MetadataBearer

/**
 * Lists all roles that are assigned to the user for a given Amazon Web Services account.
 *
 * @see ListAccountRolesCommandInput
 * @see ListAccountRolesCommandOutput
 * @see SSOClientResolvedConfig
 */
external class ListAccountRolesCommand(input: ListAccountRolesCommandInput) :
    Command<ListAccountRolesCommandInput, ListAccountRolesCommandOutput> {
    override val input: ListAccountRolesCommandInput
}
