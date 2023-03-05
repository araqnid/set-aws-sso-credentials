@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.HttpHandlerOptions
import kotlin.js.Promise

external class STS(config: STSClientConfig) : STSClient {
    @JsName("assumeRole")
    fun assumeRoleAsync(
        input: AssumeRoleCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<AssumeRoleCommandOutput>

    @JsName("assumeRole")
    fun assumeRoleAsync(
        input: AssumeRoleCommandInput,
        cb: (err: Any?, data: AssumeRoleCommandOutput?) -> Unit
    )

    @JsName("assumeRole")
    fun assumeRoleAsync(
        input: AssumeRoleCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: AssumeRoleCommandOutput?) -> Unit
    )

    @JsName("assumeRoleWithSAML")
    fun assumeRoleWithSAMLAsync(
        input: AssumeRoleWithSAMLCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<AssumeRoleWithSAMLCommandOutput>

    @JsName("assumeRoleWithSAML")
    fun assumeRoleWithSAMLAsync(
        input: AssumeRoleWithSAMLCommandInput,
        cb: (err: Any?, data: AssumeRoleCommandOutput?) -> Unit
    )

    @JsName("assumeRoleWithSAML")
    fun assumeRoleWithSAMLAsync(
        input: AssumeRoleWithSAMLCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: AssumeRoleWithSAMLCommandOutput?) -> Unit
    )

    @JsName("assumeRoleWithWebIdentity")
    fun assumeRoleWithWebIdentityAsync(
        input: AssumeRoleWithWebIdentityCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<AssumeRoleWithWebIdentityCommandOutput>

    @JsName("assumeRoleWithWebIdentity")
    fun assumeRoleWithWebIdentityAsync(
        input: AssumeRoleWithWebIdentityCommandInput,
        cb: (err: Any?, data: AssumeRoleWithWebIdentityCommandOutput?) -> Unit
    )

    @JsName("assumeRoleWithWebIdentity")
    fun assumeRoleWithWebIdentityAsync(
        input: AssumeRoleWithWebIdentityCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: AssumeRoleWithWebIdentityCommandOutput?) -> Unit
    )

    @JsName("decodeAuthorizationMessage")
    fun decodeAuthorizationMessageAsync(
        input: DecodeAuthorizationMessageCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<DecodeAuthorizationMessageCommandOutput>

    @JsName("decodeAuthorizationMessage")
    fun decodeAuthorizationMessageAsync(
        input: DecodeAuthorizationMessageCommandInput,
        cb: (err: Any?, data: DecodeAuthorizationMessageCommandOutput?) -> Unit,
    )

    @JsName("decodeAuthorizationMessage")
    fun decodeAuthorizationMessageAsync(
        input: DecodeAuthorizationMessageCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: DecodeAuthorizationMessageCommandOutput?) -> Unit
    )

    @JsName("getAccessKeyInfo")
    fun getAccessKeyInfoAsync(
        input: GetAccessKeyInfoCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<GetAccessKeyInfoCommandOutput>

    @JsName("getAccessKeyInfo")
    fun getAccessKeyInfoAsync(
        input: GetAccessKeyInfoCommandInput,
        cb: (err: Any?, data: GetAccessKeyInfoCommandOutput?) -> Unit
    )

    @JsName("getAccessKeyInfo")
    fun getAccessKeyInfoAsync(
        input: GetAccessKeyInfoCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: GetAccessKeyInfoCommandOutput?) -> Unit
    )

    @JsName("getCallerIdentity")
    fun getCallerIdentityAsync(
        input: GetCallerIdentityCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<GetCallerIdentityCommandOutput>

    @JsName("getCallerIdentity")
    fun getCallerIdentityAsync(
        input: GetCallerIdentityCommandInput,
        cb: (err: Any?, data: GetCallerIdentityCommandOutput?) -> Unit
    )

    @JsName("getCallerIdentity")
    fun getCallerIdentityAsync(
        input: GetCallerIdentityCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: GetCallerIdentityCommandOutput?) -> Unit

    )

    @JsName("getFederationToken")
    fun getFederationTokenAsync(
        input: GetFederationTokenCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<GetFederationTokenCommandOutput>

    @JsName("getFederationToken")
    fun getFederationTokenAsync(
        input: GetFederationTokenCommandInput,
        cb: (err: Any?, data: GetFederationTokenCommandOutput?) -> Unit
    )

    @JsName("getFederationToken")
    fun getFederationTokenAsync(
        input: GetFederationTokenCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: GetFederationTokenCommandOutput?) -> Unit
    )

    @JsName("getSessionToken")
    fun getSessionTokenAsync(
        input: GetSessionTokenCommandInput,
        options: HttpHandlerOptions = definedExternally
    ): Promise<GetSessionTokenCommandOutput>

    @JsName("getSessionToken")
    fun getSessionTokenAsync(
        input: GetSessionTokenCommandInput,
        cb: (err: Any?, data: GetSessionTokenCommandOutput?) -> Unit
    )

    @JsName("getSessionToken")
    fun getSessionTokenAsync(
        input: GetSessionTokenCommandInput,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: GetSessionTokenCommandOutput?) -> Unit
    )
}
