@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import org.araqnid.kotlin.setawsssocredentials.aws.*
import kotlin.js.Promise

external interface STSClientConfig {
    var region: String?
    var defaultsMode: Any? // String | Promise<String>
    var credentialDefaultProvider: Provider<Credentials>? // Provider<Credentials>
}

external interface STSClientResolvedConfig {
    val region: () -> Promise<String>
    val defaultsMode: () -> Promise<String>
    val credentialsDefaultProvider: Provider<Credentials>
}

external interface STSServiceInput

external interface STSServiceOutput

open external class STSClient(config: STSClientConfig) :
    Client<STSServiceInput, STSServiceOutput, STSClientResolvedConfig> {
    override val config: STSClientResolvedConfig

    override fun <I : STSServiceInput, O : STSServiceOutput> sendAsync(
        input: Command<I, O>,
        options: HttpHandlerOptions
    ): Promise<O>

    override fun <I : STSServiceInput, O : STSServiceOutput> sendAsync(
        input: Command<I, O>,
        cb: (err: Any?, data: O?) -> Unit
    )

    override fun <I : STSServiceInput, O : STSServiceOutput> sendAsync(
        input: Command<I, O>,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: O?) -> Unit
    )

    override fun destroy()
}
