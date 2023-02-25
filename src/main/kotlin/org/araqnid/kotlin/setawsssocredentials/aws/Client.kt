package org.araqnid.kotlin.setawsssocredentials.aws

import js.core.jso
import kotlinx.coroutines.await
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.job
import web.abort.AbortController
import kotlin.js.Promise

external interface Client<ServiceInputTypes, ServiceOutputTypes, ResolvedClientConfiguration> {
    val config: ResolvedClientConfiguration

    @JsName("send")
    fun <I : ServiceInputTypes, O : ServiceOutputTypes> sendAsync(
        input: Command<I, O>,
        options: HttpHandlerOptions = definedExternally
    ): Promise<O>

    @JsName("send")
    fun <I : ServiceInputTypes, O : ServiceOutputTypes> sendAsync(
        input: Command<I, O>,
        cb: (err: Any?, data: O?) -> Unit
    )

    @JsName("send")
    fun <I : ServiceInputTypes, O : ServiceOutputTypes> sendAsync(
        input: Command<I, O>,
        options: HttpHandlerOptions = definedExternally,
        cb: (err: Any?, data: O?) -> Unit
    )

    /**
     * Destroy underlying resources, like sockets. It's usually not necessary to do this.
     * However in Node.js, it's best to explicitly shut down the client's agent when it is no longer needed.
     * Otherwise, sockets might stay open for quite a long time before the server terminates them.
     */
    fun destroy()
}

suspend fun <I, O, CI : I, CO : O> Client<I, O, *>.send(command: Command<CI, CO>): CO {
    return coroutineScope {
        val abortController = AbortController()
        coroutineContext.job.invokeOnCompletion { ex ->
            abortController.abort(ex)
        }
        sendAsync(command, jso<HttpHandlerOptions> { abortSignal = abortController.signal }).await()
    }
}

inline fun <C : Client<*, *, *>, R> C.use(block: (C) -> R): R {
    return try {
        block(this)
    } finally {
        destroy()
    }
}