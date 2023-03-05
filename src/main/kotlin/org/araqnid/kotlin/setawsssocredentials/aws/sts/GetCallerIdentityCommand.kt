@file:JsModule("@aws-sdk/client-sts")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.Command

external interface GetCallerIdentityCommandInput : STSServiceInput, GetCallerIdentityRequest
external interface GetCallerIdentityCommandOutput : STSServiceOutput, GetCallerIdentityResponse

/**
 * Returns details about the IAM user or role whose credentials are used to call the
 * operation.
 *
 * #### Note
 *
 * No permissions are required to perform this operation. If an administrator adds a
 * policy to your IAM user or role that explicitly denies access to the
 *    `sts:GetCallerIdentity` action, you can still perform this operation.
 *
 * Permissions are not required because the same information is returned when an IAM user
 * or role is denied access. To view an example response, see
 * [I Am Not Authorized to Perform: iam:DeleteVirtualMFADevice](https://docs.aws.amazon.com/IAM/latest/UserGuide/troubleshoot_general.html#troubleshoot_general_access-denied-delete-mfa)
 * in the *IAM User Guide*.
 *
 * ### Example
 *
 * Use a bare-bones client and the command you need to make an API call.
 *
 * ```javascript
 * import { STSClient, GetCallerIdentityCommand } from "@aws-sdk/client-sts"; // ES Modules import
 * // const { STSClient, GetCallerIdentityCommand } = require("@aws-sdk/client-sts"); // CommonJS import
 * const client = new STSClient(config);
 * const command = new GetCallerIdentityCommand(input);
 * const response = await client.send(command);
 * ```
 *
 * @see GetCallerIdentityCommandInput
 * @see GetCallerIdentityCommandOutput
 * @see STSClientResolvedConfig
 */
external class GetCallerIdentityCommand(input: GetCallerIdentityCommandInput) :
    Command<GetCallerIdentityCommandInput, GetCallerIdentityCommandOutput> {
    override val input: GetCallerIdentityCommandInput
}
