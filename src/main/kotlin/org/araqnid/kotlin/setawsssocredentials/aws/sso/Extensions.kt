package org.araqnid.kotlin.setawsssocredentials.aws.sso

import js.core.jso
import kotlinx.coroutines.flow.*
import org.araqnid.kotlin.setawsssocredentials.aws.flattenedAsFlow
import org.araqnid.kotlin.setawsssocredentials.aws.sendCancellable

fun createSSOClient(region: String? = null, defaultsMode: String? = null): SSOClient {
    return SSOClient(jso {
        if (region != null) this.region = region
        if (defaultsMode != null) this.defaultsMode = defaultsMode
    })
}

/**
 * Returns the STS short-term credentials for a given role name that is assigned to the user.
 * @param roleName The friendly name of the role that is assigned to the user.
 * @param accountId The identifier for the Amazon Web Services account that is assigned to the user.
 * @param accessToken
 */
suspend inline fun SSOClient.getRoleCredentials(
    accountId: String,
    accessToken: String,
    roleName: String
): GetRoleCredentialsCommandOutput = sendCancellable(GetRoleCredentialsCommand(jso {
    this.roleName = roleName
    this.accountId = accountId
    this.accessToken = accessToken
}))

/**
 * Lists all roles that are assigned to the user for a given Amazon Web Services account.
 * @param accessToken The token issued by the `CreateToken` API call. For more information, see
 * [CreateToken](https://docs.aws.amazon.com/singlesignon/latest/OIDCAPIReference/API_CreateToken.html) in the _Amazon Web Services SSO OIDC API Reference Guide_.
 * @param accountId The identifier for the Amazon Web Services account that is assigned to the user.
 * @param nextToken The page token from the previous response output when you request subsequent pages.
 * @param maxResults The number of items that clients can request per page.
 */
suspend inline fun SSOClient.listAccountRoles(
    accessToken: String,
    accountId: String,
    nextToken: String? = null,
    maxResults: Number? = null,
): ListAccountRolesCommandOutput = sendCancellable(ListAccountRolesCommand(jso {
    this.accessToken = accessToken
    this.accountId = accountId
    this.nextToken = nextToken
    this.maxResults = maxResults
}))

/**
 * Lists all roles that are assigned to the user for a given Amazon Web Services account.
 * @param accessToken The token issued by the `CreateToken` API call. For more information, see
 * [CreateToken](https://docs.aws.amazon.com/singlesignon/latest/OIDCAPIReference/API_CreateToken.html) in the _Amazon Web Services SSO OIDC API Reference Guide_.
 * @param accountId The identifier for the Amazon Web Services account that is assigned to the user.
 * @param nextToken The page token from the previous response output when you request subsequent pages.
 * @param maxResults The number of items that clients can request per page.
 */
fun SSOClient.listAccountRolesAsFlow(
    accessToken: String,
    accountId: String,
    pageSize: Int? = null,
    startingToken: String? = null,
    stopOnSameToken: Boolean? = false,
) = paginateListAccountRoles(
    jso {
        client = this@listAccountRolesAsFlow
        if (pageSize != null) this.pageSize = pageSize
        if (startingToken != null) this.startingToken = startingToken
        if (stopOnSameToken != null) this.stopOnSameToken = stopOnSameToken
    },
    jso {
        this.accessToken = accessToken
        this.accountId = accountId
    },
).flattenedAsFlow { it.roleList }

/**
 * Lists all Amazon Web Services accounts assigned to the user. These Amazon Web Services accounts are assigned by the
 * administrator of the account. For more information, see [Assign User Access](https://docs.aws.amazon.com/singlesignon/latest/userguide/useraccess.html#assignusers)
 * in the _Amazon Web Services SSO User Guide_. This operation
 * returns a paginated response.
 * @param accessToken The token issued by the `CreateToken` API call. For more information, see
 * [CreateToken](https://docs.aws.amazon.com/singlesignon/latest/OIDCAPIReference/API_CreateToken.html) in the _Amazon Web Services SSO OIDC API Reference Guide_.
 * @param nextToken The page token from the previous response output when you request subsequent pages.
 * @param maxResults The number of items that clients can request per page.
 */
suspend inline fun SSOClient.listAccounts(
    accessToken: String,
    nextToken: String? = null,
    maxResults: Number? = null,
): ListAccountsCommandOutput = sendCancellable(ListAccountsCommand(jso {
    this.accessToken = accessToken
    this.nextToken = nextToken
    this.maxResults = maxResults
}))

/**
 * Lists all Amazon Web Services accounts assigned to the user. These Amazon Web Services accounts are assigned by the
 * administrator of the account. For more information, see [Assign User Access](https://docs.aws.amazon.com/singlesignon/latest/userguide/useraccess.html#assignusers)
 * in the _Amazon Web Services SSO User Guide_. This operation
 * returns a paginated response.
 * @param accessToken The token issued by the `CreateToken` API call. For more information, see
 * [CreateToken](https://docs.aws.amazon.com/singlesignon/latest/OIDCAPIReference/API_CreateToken.html) in the _Amazon Web Services SSO OIDC API Reference Guide_.
 * @param nextToken The page token from the previous response output when you request subsequent pages.
 * @param maxResults The number of items that clients can request per page.
 */
fun SSOClient.listAccountsAsFlow(
    accessToken: String,
    pageSize: Int? = null,
    startingToken: String? = null,
    stopOnSameToken: Boolean? = null,
) = paginateListAccounts(
    jso {
        client = this@listAccountsAsFlow
        if (pageSize != null) this.pageSize = pageSize
        if (startingToken != null) this.startingToken = startingToken
        if (stopOnSameToken != null) this.stopOnSameToken = stopOnSameToken
    },
    jso {
        this.accessToken = accessToken
    },
).flattenedAsFlow { it.accountList }

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
 * @param accessToken The token issued by the `CreateToken` API call. For more information, see
 * [CreateToken](https://docs.aws.amazon.com/singlesignon/latest/OIDCAPIReference/API_CreateToken.html) in the _Amazon Web Services SSO OIDC API Reference Guide_.
 */
suspend inline fun SSOClient.logout(
    accessToken: String,
): LogoutCommandOutput = sendCancellable(LogoutCommand(jso {
    this.accessToken = accessToken
}))
