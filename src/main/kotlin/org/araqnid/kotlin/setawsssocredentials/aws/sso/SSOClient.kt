@file:JsModule("@aws-sdk/client-sso")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

import org.araqnid.kotlin.setawsssocredentials.aws.Client
import org.araqnid.kotlin.setawsssocredentials.aws.Command
import org.araqnid.kotlin.setawsssocredentials.aws.HttpHandlerOptions
import kotlin.js.Promise

external interface SSOClientConfig {
    var region: String?
    var defaultsMode: Any? // String | Promise<String>
}

external interface SSOClientResolvedConfig {
    val region: () -> Promise<String>
    val defaultsMode: () -> Promise<String>
}

external interface SSOServiceInput

external interface SSOServiceOutput

open external class SSOClient(config: SSOClientConfig) :
    Client<SSOServiceInput, SSOServiceOutput, SSOClientResolvedConfig> {
    override val config: SSOClientResolvedConfig

    override fun <I : SSOServiceInput, O : SSOServiceOutput> sendAsync(
        input: Command<I, O>,
        options: HttpHandlerOptions
    ): Promise<O>

    override fun <I : SSOServiceInput, O : SSOServiceOutput> sendAsync(
        input: Command<I, O>,
        cb: (err: Any?, data: O?) -> Unit
    )

    override fun <I : SSOServiceInput, O : SSOServiceOutput> sendAsync(
        input: Command<I, O>,
        options: HttpHandlerOptions,
        cb: (err: Any?, data: O?) -> Unit
    )

    override fun destroy()
}
