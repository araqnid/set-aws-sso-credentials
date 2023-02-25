package org.araqnid.kotlin.setawsssocredentials.aws

import js.core.jso
import kotlinx.coroutines.suspendCancellableCoroutine
import web.abort.AbortController
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
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
    return suspendCancellableCoroutine { cont ->
        val abortController = AbortController()
        cont.invokeOnCancellation(abortController::abort)
        sendAsync(command, jso { abortSignal = abortController.signal }) { err, data ->
            if (err != null)
                cont.resumeWithException(err.unsafeCast<Throwable>())
            else
                cont.resume(data.unsafeCast<CO>())
        }
    }
}

inline fun <C : Client<*, *, *>, R> C.use(block: (C) -> R): R {
    return try {
        block(this)
    } finally {
        destroy()
    }
}