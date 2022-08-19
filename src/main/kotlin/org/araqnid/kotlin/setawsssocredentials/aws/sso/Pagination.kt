@file:JsModule("@aws-sdk/client-sso")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

import org.araqnid.kotlin.setawsssocredentials.aws.PaginationConfiguration
import org.araqnid.kotlin.setawsssocredentials.aws.Paginator

external interface SSOPaginationConfiguration : PaginationConfiguration {
    override var client: SSOClient?
}

external fun paginateListAccounts(
    config: SSOPaginationConfiguration,
    input: ListAccountsCommandInput,
    vararg additionalArguments: Any?
): Paginator<ListAccountsCommandOutput>

external fun paginateListAccountRoles(
    config: SSOPaginationConfiguration,
    input: ListAccountRolesCommandInput,
    vararg additionalArguments: Any?
): Paginator<ListAccountRolesCommandOutput>
