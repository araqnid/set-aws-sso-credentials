@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.HttpHandlerOptions
import kotlin.js.Promise

external class STS(config: STSClientConfig) : STSClient {
    fun assumeRole(
        input: AssumeRoleCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<AssumeRoleCommandOutput>

    fun assumeRoleWithSAML(
        input: AssumeRoleWithSAMLCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<AssumeRoleWithSAMLCommandOutput>

    fun assumeRoleWithWebIdentity(
        input: AssumeRoleWithWebIdentityCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<AssumeRoleWithWebIdentityCommandOutput>

    fun decodeAuthorizationMessage(
        input: DecodeAuthorizationMessageCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<DecodeAuthorizationMessageCommandOutput>

    fun getAccessKeyInfo(
        input: GetAccessKeyInfoCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<GetAccessKeyInfoCommandOutput>

    fun getCallerIdentity(
        input: GetCallerIdentityCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<GetCallerIdentityCommandOutput>

    fun getFederationToken(
        input: GetFederationTokenCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<GetFederationTokenCommandOutput>

    fun getSessionToken(
        input: GetSessionTokenCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<GetSessionTokenCommandOutput>
}
