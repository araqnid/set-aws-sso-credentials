@file:JsModule("@aws-sdk/client-sso")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

import org.araqnid.kotlin.setawsssocredentials.aws.HttpHandlerOptions
import kotlin.js.Promise

/**
 * Amazon Web Services Single Sign On Portal is a web service that makes it easy for you to assign user access to
 * Amazon Web Services SSO resources such as the AWS access portal. Users can get Amazon Web Services account applications and roles
 * assigned to them and get federated into the application.
 *
 * Although Amazon Web Services Single Sign-On was renamed, the `sso` and
 * `identitystore` API namespaces will continue to retain their original name for
 * backward compatibility purposes. For more information, see [Amazon Web Services SSO rename](https://docs.aws.amazon.com/singlesignon/latest/userguide/what-is.html#renamed).
 *
 * This API reference guide describes the Amazon Web Services SSO Portal operations that you can call
 * programatically and includes detailed information on data types and errors.
 *
 * Amazon Web Services provides SDKs that consist of libraries and sample code for various programming
 * languages and platforms, such as Java, Ruby, .Net, iOS, or Android. The SDKs provide a
 * convenient way to create programmatic access to Amazon Web Services SSO and other Amazon Web Services services. For more
 * information about the Amazon Web Services SDKs, including how to download and install them, see [Tools for Amazon Web Services](http://aws.amazon.com/tools/).
 */
external class SSO(config: SSOClientConfig) : SSOClient {
    /**
     * Returns the STS short-term credentials for a given role name that is assigned to the user.
     */
    fun getRoleCredentials(
        input: GetRoleCredentialsCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<GetRoleCredentialsCommandOutput>

    /**
     * Returns the STS short-term credentials for a given role name that is assigned to the user.
     */
    fun getRoleCredentials(
        input: GetRoleCredentialsCommandInput,
        cb: (err: Any?, data: GetRoleCredentialsCommandOutput?) -> Unit
    )

    /**
     * Returns the STS short-term credentials for a given role name that is assigned to the user.
     */
    fun getRoleCredentials(
        input: GetRoleCredentialsCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: GetRoleCredentialsCommandOutput?) -> Unit
    )

    /**
     * Lists all roles that are assigned to the user for a given Amazon Web Services account.
     */
    fun listAccountRoles(
        input: ListAccountRolesCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<ListAccountRolesCommandOutput>

    /**
     * Lists all roles that are assigned to the user for a given Amazon Web Services account.
     */
    fun listAccountRoles(
        input: ListAccountRolesCommandInput,
        cb: (err: Any?, data: ListAccountRolesCommandOutput?) -> Unit
    )

    /**
     * Lists all roles that are assigned to the user for a given Amazon Web Services account.
     */
    fun listAccountRoles(
        input: ListAccountRolesCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: ListAccountRolesCommandOutput?) -> Unit
    )

    /**
     * Lists all Amazon Web Services accounts assigned to the user. These Amazon Web Services accounts are assigned by the
     * administrator of the account. For more information, see [Assign User Access](https://docs.aws.amazon.com/singlesignon/latest/userguide/useraccess.html#assignusers)
     * in the _Amazon Web Services SSO User Guide_. This operation
     * returns a paginated response.
     */
    fun listAccounts(
        input: ListAccountsCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<ListAccountsCommandOutput>

    /**
     * Lists all Amazon Web Services accounts assigned to the user. These Amazon Web Services accounts are assigned by the
     * administrator of the account. For more information, see [Assign User Access](https://docs.aws.amazon.com/singlesignon/latest/userguide/useraccess.html#assignusers)
     * in the _Amazon Web Services SSO User Guide_. This operation
     * returns a paginated response.
     */
    fun listAccounts(
        input: ListAccountsCommandInput,
        cb: (err: Any?, data: ListAccountRolesCommandOutput?) -> Unit
    )

    /**
     * Lists all Amazon Web Services accounts assigned to the user. These Amazon Web Services accounts are assigned by the
     * administrator of the account. For more information, see [Assign User Access](https://docs.aws.amazon.com/singlesignon/latest/userguide/useraccess.html#assignusers)
     * in the _Amazon Web Services SSO User Guide_. This operation
     * returns a paginated response.
     */
    fun listAccounts(
        input: ListAccountsCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: ListAccountRolesCommandOutput?) -> Unit
    )

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
     * in the _Amazon Web Services SSO User Guide_.</p>
     */
    fun logout(
        input: LogoutCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<LogoutCommandOutput>

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
     * in the _Amazon Web Services SSO User Guide_.</p>
     */
    fun logout(
        input: LogoutCommandInput,
        cb: (err: Any?, data: ListAccountRolesCommandOutput?) -> Unit
    )

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
     * in the _Amazon Web Services SSO User Guide_.</p>
     */
    fun logout(
        input: LogoutCommandInput,
        options: HttpHandlerOptions = definedExternally,
        cb: (err: Any?, data: ListAccountRolesCommandOutput?) -> Unit
    )
}
