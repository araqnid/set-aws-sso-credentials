@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.Command

external interface AssumeRoleCommandInput : STSServiceInput, AssumeRoleRequest
external interface AssumeRoleCommandOutput : STSServiceOutput, AssumeRoleResponse

/**
 * Returns a set of temporary security credentials that you can use to access Amazon Web Services
 * resources. These temporary credentials consist of an access key ID, a secret access key,
 * and a security token. Typically, you use `AssumeRole` within your account or for
 * cross-account access. For a comparison of `AssumeRole` with other API operations
 * that produce temporary credentials, see
 * [Requesting Temporary Security Credentials](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp_request.)
 * and
 * [Comparing the Amazon Web Services STS API operations](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp_request.html#stsapi_comparison)
 * in the _IAM User Guide_.
 *
 * ### Permissions
 *
 * The temporary security credentials created by <code>AssumeRole</code> can be used to
 * make API calls to any Amazon Web Services service with the following exception: You cannot call the
 * Amazon Web Services STS `GetFederationToken` or `GetSessionToken` API
 * operations.
 *
 * (Optional) You can pass inline or managed
 * [session policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies.html#policies_session)
 * to
 * this operation. You can pass a single JSON policy document to use as an inline session
 * policy. You can also specify up to 10 managed policy Amazon Resource Names (ARNs) to use as
 * managed session policies. The plaintext that you use for both inline and managed session
 * policies can't exceed 2,048 characters. Passing policies to this operation returns new
 * temporary credentials. The resulting session's permissions are the intersection of the
 * role's identity-based policy and the session policies. You can use the role's temporary
 * credentials in subsequent Amazon Web Services API calls to access resources in the account that owns
 * the role. You cannot use session policies to grant more permissions than those allowed
 * by the identity-based policy of the role that is being assumed. For more information, see
 * [Session Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies.html#policies_session)
 * in the _IAM User Guide_.
 *
 * When you create a role, you create two policies: A role trust policy that specifies
 *    _who_ can assume the role and a permissions policy that specifies
 *    _what_ can be done with the role. You specify the trusted principal
 * who is allowed to assume the role in the role trust policy.
 *
 * To assume a role from a different account, your Amazon Web Services account must be trusted by the
 * role. The trust relationship is defined in the role's trust policy when the role is
 * created. That trust policy states which accounts are allowed to delegate that access to
 * users in the account.
 *
 * A user who wants to access a role in a different account must also have permissions that
 * are delegated from the user account administrator. The administrator must attach a policy
 * that allows the user to call `AssumeRole` for the ARN of the role in the other
 * account.
 *
 * To allow a user to assume a role in the same account, you can do either of the
 * following:
 *
 * - Attach a policy to the user that allows the user to call `AssumeRole`
 *   (as long as the role's trust policy trusts the account).
 * - Add the user as a principal directly in the role's trust policy.
 *
 * You can do either because the role’s trust policy acts as an IAM resource-based
 * policy. When a resource-based policy grants access to a principal in the same account, no
 * additional identity-based policy is required. For more information about trust policies and
 * resource-based policies, see
 * [IAM Policies](https://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies.html)
 * in the _IAM User Guide_.
 *
 * ### Tags
 *
 * (Optional) You can pass tag key-value pairs to your session. These tags are called
 * session tags. For more information about session tags, see
 * [Passing Session Tags in STS](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_session-tags.html)
 * in the _IAM User Guide_.
 *
 * An administrator must grant you the permissions necessary to pass session tags. The
 * administrator can also create granular permissions to allow you to pass only specific
 * session tags. For more information, see
 * [Tutorial: Using Tags for Attribute-Based Access Control](https://docs.aws.amazon.com/IAM/latest/UserGuide/tutorial_attribute-based-access-control.html)
 * in the _IAM User Guide_.
 *
 * You can set the session tags as transitive. Transitive tags persist during role
 * chaining. For more information, see
 * [Chaining Roles with Session Tags](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_session-tags.html#id_session-tags_role-chaining)
 * in the _IAM User Guide_.
 *
 * ### Using MFA with AssumeRole
 *
 * (Optional) You can include multi-factor authentication (MFA) information when you call
 * `AssumeRole`. This is useful for cross-account scenarios to ensure that the
 * user that assumes the role has been authenticated with an Amazon Web Services MFA device. In that
 * scenario, the trust policy of the role being assumed includes a condition that tests for
 * MFA authentication. If the caller does not include valid MFA information, the request to
 * assume the role is denied. The condition in a trust policy that tests for MFA
 * authentication might look like the following example.
 *
 * ```
 * "Condition": {"Bool": {"aws:MultiFactorAuthPresent": true}}
 * ```
 *
 * For more information, see
 * [Configuring MFA-Protected API Access](https://docs.aws.amazon.com/IAM/latest/UserGuide/MFAProtectedAPI.html)
 * in the _IAM User Guide_ guide.
 *
 * To use MFA with `AssumeRole`, you pass values for the
 * `SerialNumber` and `TokenCode` parameters. The
 * `SerialNumber` value identifies the user's hardware or virtual MFA device.
 * The `TokenCode` is the time-based one-time password (TOTP) that the MFA device
 * produces.
 *
 * ### Example
 *
 * Use a bare-bones client and the command you need to make an API call.
 *
 * ```javascript
 * import { STSClient, AssumeRoleCommand } from "@aws-sdk/client-sts"; // ES Modules import
 * // const { STSClient, AssumeRoleCommand } = require("@aws-sdk/client-sts"); // CommonJS import
 * const client = new STSClient(config);
 * const command = new AssumeRoleCommand(input);
 * const response = await client.send(command);
 * ```
 *
 * @see AssumeRoleCommandInput
 * @see AssumeRoleCommandOutput
 * @see STSClientResolvedConfig
 */
external class AssumeRoleCommand(input: AssumeRoleCommandInput) :
    Command<AssumeRoleCommandInput, AssumeRoleCommandOutput> {
    override val input: AssumeRoleCommandInput
}
