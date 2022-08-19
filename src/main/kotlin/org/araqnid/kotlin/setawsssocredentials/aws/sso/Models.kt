@file:JsModule("@aws-sdk/client-sso")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

/**
 * Provides information about your Amazon Web Services account.
 */
external interface AccountInfo {
    /**
     * The identifier of the Amazon Web Services account that is assigned to the user.
     */
    var accountId: String?

    /**
     * The display name of the Amazon Web Services account that is assigned to the user.
     */
    var accountName: String?

    /**
     * The email address of the Amazon Web Services account that is assigned to the user.
     */
    var emailAddress: String?
}

external interface GetRoleCredentialsRequest {
    /**
     * The friendly name of the role that is assigned to the user.
     */
    var roleName: String?

    /**
     * The identifier for the Amazon Web Services account that is assigned to the user.
     */
    var accountId: String?

    /**
     * The token issued by the `CreateToken` API call. For more information, see
     * [CreateToken](https://docs.aws.amazon.com/singlesignon/latest/OIDCAPIReference/API_CreateToken.html)
     * in the _Amazon Web Services SSO OIDC API Reference Guide_.
     */
    var accessToken: String?
}

/**
 * Provides information about the role credentials that are assigned to the user.
 */
external interface RoleCredentials {
    /**
     * The identifier used for the temporary security credentials. For more information, see
     * [Using Temporary Security Credentials to Request Access to Amazon Web Services Resources](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp_use-resources.html)
     * in the
     * _Amazon Web Services IAM User Guide_.
     */
    var accessKeyId: String?

    /**
     * The key that is used to sign the request. For more information, see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp_use-resources.html">Using Temporary Security Credentials to Request Access to Amazon Web Services Resources</a> in the
     * <i>Amazon Web Services IAM User Guide</i>.
     */
    var secretAccessKey: String?

    /**
     * The token used for temporary credentials. For more information, see <a href="https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_temp_use-resources.html">Using Temporary Security Credentials to Request Access to Amazon Web Services Resources</a> in the
     * <i>Amazon Web Services IAM User Guide</i>.
     */
    var sessionToken: String?

    /**
     * The date on which temporary security credentials expire.
     */
    var expiration: Number?
}

external interface GetRoleCredentialsResponse {
    /**
     * The credentials for the role that is assigned to the user.
     */
    var roleCredentials: RoleCredentials?
}

external interface ListAccountRolesRequest {
    /**
     * The page token from the previous response output when you request subsequent pages.
     */
    var nextToken: String?

    /**
     * The number of items that clients can request per page.
     */
    var maxResults: Number?

    /**
     * The token issued by the `CreateToken` API call. For more information, see
     * [CreateToken](https://docs.aws.amazon.com/singlesignon/latest/OIDCAPIReference/API_CreateToken.html) in the _Amazon Web Services SSO OIDC API Reference Guide_.
     */
    var accessToken: String? // required

    /**
     * The identifier for the Amazon Web Services account that is assigned to the user.
     */
    var accountId: String? // required
}

external interface RoleInfo {
    /**
     * The friendly name of the role that is assigned to the user.
     */
    var roleName: String?

    /**
     * The identifier of the Amazon Web Services account assigned to the user.
     */
    var accountId: String?
}

external interface ListAccountRolesResponse {
    /**
     * The page token client that is used to retrieve the list of accounts.
     */
    var nextToken: String?

    /**
     * A paginated response with the list of roles and the next token if more results are
     * available.
     */
    var roleList: Array<RoleInfo>?
}

external interface ListAccountsRequest {
    /**
     * (Optional) When requesting subsequent pages, this is the page token from the previous
     * response output.
     */
    var nextToken: String?

    /**
     * This is the number of items clients can request per page.
     */
    var maxResults: Number?

    /**
     * The token issued by the <code>CreateToken</code> API call. For more information, see
     * [CreateToken](https://docs.aws.amazon.com/singlesignon/latest/OIDCAPIReference/API_CreateToken.html) in the _Amazon Web Services SSO OIDC API Reference Guide_.
     */
    var accessToken: String? // required
}

external interface ListAccountsResponse {
    /**
     * The page token client that is used to retrieve the list of accounts.
     */
    var nextToken: String?

    /**
     * A paginated response with the list of account information and the next token if more
     * results are available.
     */
    var accountList: Array<AccountInfo>?
}

external interface LogoutRequest {
    /**
     * The token issued by the <code>CreateToken</code> API call. For more information, see
     * [CreateToken](https://docs.aws.amazon.com/singlesignon/latest/OIDCAPIReference/API_CreateToken.html) in the _Amazon Web Services SSO OIDC API Reference Guide_.
     */
    var accessToken: String? // required
}
