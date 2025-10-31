@file:JsModule("@aws-sdk/client-sso")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

import org.araqnid.kotlin.setawsssocredentials.aws.Command
import org.araqnid.kotlin.setawsssocredentials.aws.MetadataBearer

external interface ListAccountsCommandInput : SSOServiceInput, ListAccountsRequest
external interface ListAccountsCommandOutput : SSOServiceOutput, ListAccountsResponse, MetadataBearer

/**
 * Lists all Amazon Web Services accounts assigned to the user. These Amazon Web Services accounts are assigned by the
 * administrator of the account. For more information, see [Assign User Access](https://docs.aws.amazon.com/singlesignon/latest/userguide/useraccess.html#assignusers)
 * in the _Amazon Web Services SSO User Guide_. This operation
 * returns a paginated response.
 *
 * @see ListAccountsCommandInput
 * @see ListAccountsCommandOutput
 * @see SSOClientResolvedConfig
 */
external class ListAccountsCommand(input: ListAccountsCommandInput) :
    Command<ListAccountsCommandInput, ListAccountsCommandOutput> {
    override val input: ListAccountsCommandInput
}
