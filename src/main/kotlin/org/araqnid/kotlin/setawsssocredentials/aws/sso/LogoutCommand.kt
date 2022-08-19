@file:JsModule("@aws-sdk/client-sso")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

import org.araqnid.kotlin.setawsssocredentials.aws.Command
import org.araqnid.kotlin.setawsssocredentials.aws.MetadataBearer

external interface LogoutCommandInput : SSOServiceInput, LogoutRequest
external interface LogoutCommandOutput : SSOServiceOutput, MetadataBearer

/**
 * Removes the locally stored SSO tokens from the client-side cache and sends an API call to
 * the Amazon Web Services SSO service to invalidate the corresponding server-side Amazon Web Services SSO sign in
 * session.
 *
 * If a user uses Amazon Web Services SSO to access the AWS CLI, the user’s Amazon Web Services SSO sign in session is
 * used to obtain an IAM session, as specified in the corresponding Amazon Web Services SSO permission set.
 * More specifically, Amazon Web Services SSO assumes an IAM role in the target account on behalf of the user,
 * and the corresponding temporary Amazon Web Services credentials are returned to the client.
 *
 * After user logout, any existing IAM role sessions that were created by using Amazon Web Services SSO
 * permission sets continue based on the duration configured in the permission set.
 * For more information, see [User authentications](https://docs.aws.amazon.com/singlesignon/latest/userguide/authconcept.html)
 * in the _Amazon Web Services SSO User Guide_.
 *
 * @see LogoutCommandInput
 * @see LogoutCommandOutput
 * @see SSOClientResolvedConfig
 */
external class LogoutCommand(input: LogoutCommandInput) : Command<LogoutCommandInput, LogoutCommandOutput> {
    override val input: LogoutCommandInput
}
